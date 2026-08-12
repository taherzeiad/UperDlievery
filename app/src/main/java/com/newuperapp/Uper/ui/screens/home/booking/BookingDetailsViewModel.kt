package com.newuperapp.Uper.ui.screens.home.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.Uper.domain.model.BookingDetails
import com.newuperapp.Uper.domain.repository.RideRequestRepository
import com.newuperapp.Uper.navigation.AberDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookingDetailsUiState(
    val rideId: String = "",
    val details: BookingDetails? = null,
    val isLoading: Boolean = true,
    val isCancelling: Boolean = false
)

sealed interface BookingDetailsEvent {
    data class NavigateToPickupNavigation(val rideId: String) : BookingDetailsEvent
    data object NavigateBackAfterCancel : BookingDetailsEvent
    data class LaunchDialer(val phoneNumber: String) : BookingDetailsEvent
    data class LaunchMessenger(val phoneNumber: String) : BookingDetailsEvent
}

@HiltViewModel
class BookingDetailsViewModel @Inject constructor(
    private val rideRequestRepository: RideRequestRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val rideId: String = savedStateHandle.get<String>(AberDestination.ARG_RIDE_ID).orEmpty()

    private val _uiState = MutableStateFlow(BookingDetailsUiState(rideId = rideId))
    val uiState: StateFlow<BookingDetailsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BookingDetailsEvent>()
    val events: SharedFlow<BookingDetailsEvent> = _events

    init {
        viewModelScope.launch {
            val details = rideRequestRepository.getBookingDetails(rideId)
            _uiState.value = _uiState.value.copy(details = details, isLoading = false)
        }
    }

    fun onCallClick() {
        _uiState.value.details?.let {
            viewModelScope.launch { _events.emit(BookingDetailsEvent.LaunchDialer(it.riderPhone)) }
        }
    }

    fun onMessageClick() {
        _uiState.value.details?.let {
            viewModelScope.launch { _events.emit(BookingDetailsEvent.LaunchMessenger(it.riderPhone)) }
        }
    }

    fun onCancelClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCancelling = true)
            rideRequestRepository.cancelBooking(rideId)
            _uiState.value = _uiState.value.copy(isCancelling = false)
            _events.emit(BookingDetailsEvent.NavigateBackAfterCancel)
        }
    }

    fun onGoToPickupClick() {
        viewModelScope.launch {
            _events.emit(BookingDetailsEvent.NavigateToPickupNavigation(rideId))
        }
    }
}
