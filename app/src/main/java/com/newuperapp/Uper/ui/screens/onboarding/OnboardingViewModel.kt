package com.newuperapp.Uper.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

sealed interface OnboardingEvent {
    data object NavigateNext : OnboardingEvent
}

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _events = Channel<OnboardingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onOnboardingFinished() {
        _events.trySend(OnboardingEvent.NavigateNext)
    }
}
