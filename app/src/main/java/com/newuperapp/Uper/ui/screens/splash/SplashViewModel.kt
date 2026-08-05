package com.newuperapp.Uper.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.Uper.domain.onboarding.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

private const val MIN_SPLASH_DURATION_MS = 1200L

sealed interface SplashDestination {
    data object Onboarding : SplashDestination
    data object Home : SplashDestination
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _destination = Channel<SplashDestination>(Channel.BUFFERED)
    val destination = _destination.receiveAsFlow()

    init {
        viewModelScope.launch {
            val minDuration = async { delay(MIN_SPLASH_DURATION_MS.milliseconds) }
            val hasCompletedOnboarding = onboardingRepository.hasCompletedOnboarding.first()
            minDuration.await()

            _destination.send(
                if (hasCompletedOnboarding) SplashDestination.Home else SplashDestination.Onboarding
            )
        }
    }
}
