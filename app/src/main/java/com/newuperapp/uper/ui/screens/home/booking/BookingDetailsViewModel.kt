package com.newuperapp.uper.ui.screens.home.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.uper.domain.model.BookingDetails
import com.newuperapp.uper.domain.repository.RideRequestRepository
import com.newuperapp.uper.domain.utils.Resource
import com.newuperapp.uper.navigation.AberDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookingDetailsUiState(
    val rideId: String = "",
    val details: BookingDetails? = null,
    val isLoading: Boolean = true,
    val isCancelling: Boolean = false,
    val errorMessage: String? = null
)

sealed interface BookingDetailsEvent {
    data class NavigateToPickupNavigation(val rideId: String) : BookingDetailsEvent
    data object NavigateBackAfterCancel : BookingDetailsEvent
    data class LaunchDialer(val phoneNumber: String) : BookingDetailsEvent
    data class LaunchMessenger(val phoneNumber: String) : BookingDetailsEvent
    data class ShowError(val message: String) : BookingDetailsEvent
}

@HiltViewModel
class BookingDetailsViewModel @Inject constructor(
    private val rideRequestRepository: RideRequestRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val rideId: String = savedStateHandle.get<String>(AberDestination.ARG_RIDE_ID).orEmpty()

    private val _uiState = MutableStateFlow(BookingDetailsUiState(rideId = rideId))
    val uiState: StateFlow<BookingDetailsUiState> = _uiState.asStateFlow()

    private val _events = Channel<BookingDetailsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadBookingDetails()
    }

    private fun loadBookingDetails() {
        if (rideId.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = rideRequestRepository.getBookingDetails(rideId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(details = result.data, isLoading = false)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                    _events.send(BookingDetailsEvent.ShowError(result.message))
                }
                else -> {}
            }
        }
    }

    fun onCallClick() {
        _uiState.value.details?.let {
            viewModelScope.launch { _events.send(BookingDetailsEvent.LaunchDialer(it.riderPhone)) }
        }
    }

    fun onMessageClick() {
        _uiState.value.details?.let {
            viewModelScope.launch { _events.send(BookingDetailsEvent.LaunchMessenger(it.riderPhone)) }
        }
    }

    fun onCancelClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCancelling = true, errorMessage = null)
            when (val result = rideRequestRepository.cancelBooking(rideId)) {
                is Resource.Success -> {
                    _events.send(BookingDetailsEvent.NavigateBackAfterCancel)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(isCancelling = false, errorMessage = result.message)
                    _events.send(BookingDetailsEvent.ShowError(result.message))
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isCancelling = false)
                }
            }
        }
    }

    fun onGoToPickupClick() {
        viewModelScope.launch {
            _events.send(BookingDetailsEvent.NavigateToPickupNavigation(rideId))
        }
    }
}
