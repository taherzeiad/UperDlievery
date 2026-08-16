package com.newuperapp.uper.ui.screens.home.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.uper.domain.model.DriverProfile
import com.newuperapp.uper.domain.repository.RideRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val rideRequestRepository: RideRequestRepository
) : ViewModel() {

    val profile: StateFlow<DriverProfile?> = rideRequestRepository.observeDriverProfile()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
