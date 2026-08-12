package com.newuperapp.Uper.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.Uper.domain.home.DriverProfile
import com.newuperapp.Uper.domain.home.RideRequest
import com.newuperapp.Uper.domain.home.RideRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isOnline: Boolean = false,
    val driverProfile: DriverProfile? = null,
    val activeRequest: RideRequest? = null,
    val currentLocation: com.newuperapp.Uper.domain.home.LatLng = com.newuperapp.Uper.domain.home.LatLng(60.1699, 24.9384)
)

sealed interface HomeEvent {
    data class NavigateToBookingDetails(val rideId: String) : HomeEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val rideRequestRepository: RideRequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvent>()
    val events: SharedFlow<HomeEvent> = _events

    init {
        viewModelScope.launch {
            rideRequestRepository.observeIncomingRequests().collect { request ->
                _uiState.value = _uiState.value.copy(activeRequest = request)
            }
        }
    }

    fun onToggleOnline() {
        _uiState.value = _uiState.value.copy(isOnline = !_uiState.value.isOnline)
    }

    fun onAcceptRequest() {
        val request = _uiState.value.activeRequest ?: return
        viewModelScope.launch {
            _events.emit(HomeEvent.NavigateToBookingDetails("ride_123")) // Fake ID
        }
    }

    fun onIgnoreRequest() {
        // In real app, this would notify the backend to stop showing this request to this driver
    }
}
