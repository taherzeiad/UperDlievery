package com.newuperapp.Uper.ui.screens.auth.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.Uper.domain.auth.AuthRepository
import com.newuperapp.Uper.domain.auth.AuthResult
import com.newuperapp.Uper.navigation.AberDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val OTP_LENGTH = 4

data class PhoneVerifyUiState(
    val fullPhoneNumber: String = "",
    val code: String = "",
    val isVerifying: Boolean = false,
    val isResending: Boolean = false,
    val errorMessage: String? = null
) {
    val isVerifyEnabled: Boolean get() = code.length == OTP_LENGTH && !isVerifying
}

sealed interface PhoneVerifyEvent {
    data object NavigateToHome : PhoneVerifyEvent
}

@HiltViewModel
class PhoneVerifyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PhoneVerifyUiState(
            fullPhoneNumber = savedStateHandle.get<String>(AberDestination.ARG_PHONE).orEmpty()
        )
    )
    val uiState: StateFlow<PhoneVerifyUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PhoneVerifyEvent>()
    val events: SharedFlow<PhoneVerifyEvent> = _events

    fun onDigitPressed(digit: String) {
        val state = _uiState.value
        if (state.code.length >= OTP_LENGTH) return
        _uiState.value = state.copy(code = state.code + digit, errorMessage = null)
    }

    fun onBackspace() {
        val current = _uiState.value.code
        if (current.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(code = current.dropLast(1))
        }
    }

    fun onVerifyClick() {
        val state = _uiState.value
        if (!state.isVerifyEnabled) return

        viewModelScope.launch {
            _uiState.value = state.copy(isVerifying = true, errorMessage = null)
            when (val result = authRepository.verifyOtp(state.fullPhoneNumber, state.code)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(isVerifying = false)
                    _events.emit(PhoneVerifyEvent.NavigateToHome)
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isVerifying = false,
                        code = "",
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun onResendClick() {
        val state = _uiState.value
        if (state.isResending) return
        viewModelScope.launch {
            _uiState.value = state.copy(isResending = true)
            authRepository.resendOtp(state.fullPhoneNumber)
            _uiState.value = _uiState.value.copy(isResending = false)
        }
    }
}
