package com.newuperapp.Uper.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.Uper.domain.model.DriverProfile
import com.newuperapp.Uper.domain.model.RideRequest
import com.newuperapp.Uper.domain.repository.RideRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isOnline: Boolean = false,
    val driverProfile: DriverProfile? = null,
    val pendingRequests: List<RideRequest> = emptyList(),
    /** Which card in the "swipe up" list is expanded to reveal its Accept button. */
    val expandedRequestId: String? = null,
    val isLoading: Boolean = true
)

sealed interface HomeEvent {
    data class NavigateToBookingDetails(val rideId: String) : HomeEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val rideRequestRepository: RideRequestRepository
) : ViewModel() {

    private val expandedRequestId = MutableStateFlow<String?>(null)

    private val _events = MutableSharedFlow<HomeEvent>()
    val events: SharedFlow<HomeEvent> = _events

    val uiState: StateFlow<HomeUiState> = combine(
        rideRequestRepository.observeOnlineState(),
        rideRequestRepository.observeDriverProfile(),
        rideRequestRepository.observePendingRequests(),
        expandedRequestId
    ) { isOnline, profile, requests, expandedId ->
        HomeUiState(
            isOnline = isOnline,
            driverProfile = profile,
            pendingRequests = if (isOnline) requests else emptyList(),
            expandedRequestId = expandedId,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun onToggleOnline(isOnline: Boolean) {
        viewModelScope.launch { rideRequestRepository.setOnline(isOnline) }
    }

    /** Tapping a collapsed card in the list expands it to reveal Accept (or collapses it back). */
    fun onRequestCardClick(rideId: String) {
        expandedRequestId.value = if (expandedRequestId.value == rideId) null else rideId
    }

    fun onAcceptRide(rideId: String) {
        viewModelScope.launch {
            rideRequestRepository.acceptRide(rideId)
            _events.emit(HomeEvent.NavigateToBookingDetails(rideId))
        }
    }

    fun onIgnoreRide(rideId: String) {
        viewModelScope.launch { rideRequestRepository.ignoreRide(rideId) }
    }
}
