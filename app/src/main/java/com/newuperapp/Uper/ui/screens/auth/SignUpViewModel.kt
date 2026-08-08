package com.newuperapp.Uper.ui.screens.auth

import com.newuperapp.Uper.domain.auth.CountryCode
import com.newuperapp.Uper.domain.auth.defaultCountryCodes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpUiState(
    val email: String = "",
    val country: CountryCode = defaultCountryCodes.first(),
    val phoneNumber: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
) {
    val isFormValid: Boolean
        get() = email.contains("@") && email.contains(".") && phoneNumber.length >= 6
}

sealed interface SignUpEvent {
    data object NavigateToVerification : SignUpEvent
    data object NavigateToSignIn : SignUpEvent
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    // private val authRepository: AuthRepository -- وصليها بالـ backend الحقيقي هون
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _events = Channel<SignUpEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPhoneNumberChange(value: String) {
        _uiState.update { it.copy(phoneNumber = value, errorMessage = null) }
    }

    fun onCountrySelected(country: CountryCode) {
        _uiState.update { it.copy(country = country) }
    }

    fun onSignUpClick() {
        val state = _uiState.value
        if (!state.isFormValid) {
            _uiState.update { it.copy(errorMessage = "Please enter a valid email and phone number") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            // TODO: نداء authRepository.signUp(state.email, state.country.dialCode + state.phoneNumber)
            _uiState.update { it.copy(isSubmitting = false) }
            _events.send(SignUpEvent.NavigateToVerification)
        }
    }

    fun onSignInClick() {
        _events.trySend(SignUpEvent.NavigateToSignIn)
    }
}