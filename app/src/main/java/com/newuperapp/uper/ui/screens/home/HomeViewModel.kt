package com.newuperapp.uper.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.uper.domain.model.DriverProfile
import com.newuperapp.uper.domain.model.RideRequest
import com.newuperapp.uper.domain.repository.RideRequestRepository
import com.newuperapp.uper.domain.utils.Resource
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
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

sealed interface HomeEvent {
    data class NavigateToBookingDetails(val rideId: String) : HomeEvent
    data class ShowError(val message: String) : HomeEvent
    data object NavigateToAuth : HomeEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val rideRequestRepository: RideRequestRepository,
    private val onboardingRepository: com.newuperapp.uper.domain.onboarding.OnboardingRepository
) : ViewModel() {

    private val expandedRequestId = MutableStateFlow<String?>(null)

    private val _events = MutableSharedFlow<HomeEvent>()
    val events: SharedFlow<HomeEvent> = _events

    val uiState: StateFlow<HomeUiState> = combine(
        rideRequestRepository.observeOnlineState(),
        rideRequestRepository.observeDriverProfile(),
        rideRequestRepository.observePendingRequests(),
        expandedRequestId
    ) { isOnline: Boolean, profileRes: Resource<DriverProfile>, requestsRes: Resource<List<RideRequest>>, expandedId: String? ->
        val profile = if (profileRes is Resource.Success) profileRes.data else null
        val requests = if (requestsRes is Resource.Success) requestsRes.data else emptyList()
        val error = (profileRes as? Resource.Error)?.message ?: (requestsRes as? Resource.Error)?.message

        HomeUiState(
            isOnline = isOnline,
            driverProfile = profile,
            pendingRequests = if (isOnline) requests else emptyList(),
            expandedRequestId = expandedId,
            isLoading = profileRes is Resource.Loading || (isOnline && requestsRes is Resource.Loading),
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun onToggleOnline(isOnline: Boolean) {
        viewModelScope.launch {
            when (val result = rideRequestRepository.setOnline(isOnline)) {
                is Resource.Error -> _events.emit(HomeEvent.ShowError(result.message))
                else -> {}
            }
        }
    }

    /** Tapping a collapsed card in the list expands it to reveal Accept (or collapses it back). */
    fun onRequestCardClick(rideId: String) {
        expandedRequestId.value = if (expandedRequestId.value == rideId) null else rideId
    }

    fun onAcceptRide(rideId: String) {
        viewModelScope.launch {
            when (val result = rideRequestRepository.acceptRide(rideId)) {
                is Resource.Success -> _events.emit(HomeEvent.NavigateToBookingDetails(rideId))
                is Resource.Error -> _events.emit(HomeEvent.ShowError(result.message))
                else -> {}
            }
        }
    }

    fun onIgnoreRide(rideId: String) {
        viewModelScope.launch {
            when (val result = rideRequestRepository.ignoreRide(rideId)) {
                is Resource.Error -> _events.emit(HomeEvent.ShowError(result.message))
                else -> {}
            }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            // In a real app, we would also clear auth tokens
            _events.emit(HomeEvent.NavigateToAuth)
        }
    }
}
