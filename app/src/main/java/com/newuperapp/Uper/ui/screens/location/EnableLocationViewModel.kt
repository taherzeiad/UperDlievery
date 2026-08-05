package com.newuperapp.Uper.ui.screens.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.Uper.domain.onboarding.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EnableLocationEvent {
    data object Continue : EnableLocationEvent
}

@HiltViewModel
class EnableLocationViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _events = Channel<EnableLocationEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onPermissionResult(granted: Boolean) = finishFirstRunFlow()

    fun onSkip() = finishFirstRunFlow()

    private fun finishFirstRunFlow() {
        viewModelScope.launch {
            onboardingRepository.setOnboardingCompleted()
            _events.send(EnableLocationEvent.Continue)
        }
    }
}
