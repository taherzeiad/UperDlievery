package com.newuperapp.Uper.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.Uper.domain.home.DriverProfile
import com.newuperapp.Uper.domain.home.DriverProfileRepository
import com.newuperapp.Uper.domain.home.LatLng
import com.newuperapp.Uper.domain.home.RideRequest
import com.newuperapp.Uper.domain.home.RideRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class HomeUiState(
    val isOnline: Boolean = false,
    val currentLocation: LatLng = LatLng(24.7136, 46.6753),
    val driverProfile: DriverProfile? = null,
    val activeRequest: RideRequest? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val driverProfileRepository: DriverProfileRepository,
    private val rideRequestRepository: RideRequestRepository
) : ViewModel() {

    private val _isOnline = MutableStateFlow(false)

    private val incomingRequest: Flow<RideRequest?> = _isOnline.flatMapLatest { online ->
        if (online) rideRequestRepository.observeIncomingRequests()
        else flowOf(null)
    }

    val uiState: StateFlow<HomeUiState> = combine(
        _isOnline,
        driverProfileRepository.observeProfile(),
        incomingRequest
    ) { online, profile, request ->
        HomeUiState(
            isOnline = online,
            driverProfile = profile,
            activeRequest = request
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun onToggleOnline() {
        _isOnline.value = !_isOnline.value
    }

    fun onAcceptRequest() {
    }

    fun onIgnoreRequest() {
    }
}
