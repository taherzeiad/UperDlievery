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

private const val MAX_PHONE_DIGITS = 12

data class SignInUiState(
    val country: CountryCode = defaultCountryCodes.first(),
    val phoneNumber: String = "",
    val isSubmitting: Boolean = false
) {
    val isFormValid: Boolean
        get() = phoneNumber.length >= 6
}

sealed interface SignInEvent {
    data object NavigateToVerification : SignInEvent
}

@HiltViewModel
class SignInViewModel @Inject constructor(
    // private val authRepository: AuthRepository -- وصليها بالـ backend الحقيقي هون
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _events = Channel<SignInEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onCountrySelected(country: CountryCode) {
        _uiState.update { it.copy(country = country) }
    }

    /** تتنفذ من [com.aber.driver.ui.components.AberNumericKeypad] — رقم واحد بكل ضغطة. */
    fun onDigitEntered(digit: String) {
        _uiState.update { state ->
            if (state.phoneNumber.length >= MAX_PHONE_DIGITS) state
            else state.copy(phoneNumber = state.phoneNumber + digit)
        }
    }

    fun onBackspace() {
        _uiState.update { it.copy(phoneNumber = it.phoneNumber.dropLast(1)) }
    }

    fun onClear() {
        _uiState.update { it.copy(phoneNumber = "") }
    }

    fun onNextClick() {
        val state = _uiState.value
        if (!state.isFormValid) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            // TODO: نداء authRepository.requestOtp(state.country.dialCode + state.phoneNumber)
            _uiState.update { it.copy(isSubmitting = false) }
            _events.send(SignInEvent.NavigateToVerification)
        }
    }
}