package com.newuperapp.Uper.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.Uper.domain.repository.AuthRepository
import com.newuperapp.Uper.domain.repository.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpUiState(
    val email: String = "",
    val phoneNumber: String = "",
    val dialCode: String = "+84",
    val countryFlag: String = "🇻🇳",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
) {
    val isSubmitEnabled: Boolean
        get() = email.contains("@") && phoneNumber.length >= 8 && !isSubmitting
}

sealed interface SignUpEvent {
    data class NavigateToOtp(val fullPhoneNumber: String) : SignUpEvent
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SignUpEvent>()
    val events: SharedFlow<SignUpEvent> = _events

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onPhoneChange(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value, errorMessage = null)
    }

    fun onCountrySelected(dialCode: String, flag: String) {
        _uiState.value = _uiState.value.copy(dialCode = dialCode, countryFlag = flag)
    }

    fun onSignUpClick() {
        val state = _uiState.value
        if (!state.isSubmitEnabled) return

        viewModelScope.launch {
            _uiState.value = state.copy(isSubmitting = true, errorMessage = null)
            val fullPhone = state.dialCode + state.phoneNumber
            when (val result = authRepository.signUp(state.email, fullPhone)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false)
                    _events.emit(SignUpEvent.NavigateToOtp(fullPhone))
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, errorMessage = result.message)
                }
            }
        }
    }
}
