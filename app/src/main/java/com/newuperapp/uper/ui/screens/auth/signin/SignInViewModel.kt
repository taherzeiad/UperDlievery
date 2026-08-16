package com.newuperapp.uper.ui.screens.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.uper.domain.repository.AuthRepository
import com.newuperapp.uper.domain.repository.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignInUiState(
    val dialCode: String = "+84",
    val countryFlag: String = "🇻🇳",
    val phoneNumber: String = "905070017",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
) {
    val isNextEnabled: Boolean get() = phoneNumber.length >= 8 && !isSubmitting
}

sealed interface SignInEvent {
    data class NavigateToOtp(val fullPhoneNumber: String) : SignInEvent
}

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SignInEvent>()
    val events: SharedFlow<SignInEvent> = _events

    fun onDigitPressed(digit: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = _uiState.value.phoneNumber + digit,
            errorMessage = null
        )
    }

    fun onBackspace() {
        val current = _uiState.value.phoneNumber
        if (current.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(phoneNumber = current.dropLast(1))
        }
    }

    fun onClearClick() {
        _uiState.value = _uiState.value.copy(phoneNumber = "")
    }

    fun onCountrySelected(dialCode: String, flag: String) {
        val formattedDialCode = if (dialCode.startsWith("+")) dialCode else "+$dialCode"
        _uiState.value = _uiState.value.copy(dialCode = formattedDialCode, countryFlag = flag)
    }

    fun onNextClick() {
        val state = _uiState.value
        if (!state.isNextEnabled) return

        viewModelScope.launch {
            _uiState.value = state.copy(isSubmitting = true, errorMessage = null)
            val fullPhone = state.dialCode + state.phoneNumber
            when (val result = authRepository.requestOtp(fullPhone)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false)
                    _events.emit(SignInEvent.NavigateToOtp(fullPhone))
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, errorMessage = result.message)
                }
            }
        }
    }
}
