package com.newuperapp.Uper.ui.screens.auth

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

private const val OTP_LENGTH = 4

data class PhoneVerifyUiState(
    val code: String = "", val isSubmitting: Boolean = false, val errorMessage: String? = null
) {
    val isComplete: Boolean
        get() = code.length == OTP_LENGTH
}

sealed interface PhoneVerifyEvent {
    data object NavigateToHome : PhoneVerifyEvent
    data object NavigateBack : PhoneVerifyEvent
}

@HiltViewModel
class PhoneVerifyViewModel @Inject constructor(
    // private val authRepository: AuthRepository -- وصليها بالـ backend الحقيقي هون
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhoneVerifyUiState())
    val uiState: StateFlow<PhoneVerifyUiState> = _uiState.asStateFlow()

    private val _events = Channel<PhoneVerifyEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    /** تتنفذ من [com.aber.driver.ui.components.AberNumericKeypad]. */
    fun onDigitEntered(digit: String) {
        _uiState.update { state ->
            if (state.code.length >= OTP_LENGTH) state
            else state.copy(code = state.code + digit, errorMessage = null)
        }
    }

    fun onBackspace() {
        _uiState.update { it.copy(code = it.code.dropLast(1), errorMessage = null) }
    }

    fun onBackClick() {
        _events.trySend(PhoneVerifyEvent.NavigateBack)
    }

    fun onVerifyClick() {
        val state = _uiState.value
        if (!state.isComplete) {
            _uiState.update { it.copy(errorMessage = "Enter the 4-digit code") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            // TODO: نداء authRepository.verifyOtp(state.code) والتحقق من الرد
            _uiState.update { it.copy(isSubmitting = false) }
            _events.send(PhoneVerifyEvent.NavigateToHome)
        }
    }
}