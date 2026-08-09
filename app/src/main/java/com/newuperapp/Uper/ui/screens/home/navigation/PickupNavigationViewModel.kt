package com.newuperapp.Uper.ui.screens.home.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.Uper.domain.home.PickupNavigationState
import com.newuperapp.Uper.domain.home.RideRequestRepository
import com.newuperapp.Uper.navigation.AberDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PickupNavigationEvent {
    data object NavigateToDropoffFlow : PickupNavigationEvent
}

@HiltViewModel
class PickupNavigationViewModel @Inject constructor(
    private val rideRequestRepository: RideRequestRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val rideId: String = savedStateHandle.get<String>(AberDestination.ARG_RIDE_ID).orEmpty()

    val uiState: StateFlow<PickupNavigationState?> = rideRequestRepository
        .observePickupNavigation(rideId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _events = MutableSharedFlow<PickupNavigationEvent>()
    val events: SharedFlow<PickupNavigationEvent> = _events

    /** Driver reached the rider — "DROP OFF" CTA on the expanded sheet. */
    fun onArrivedAtPickupClick() {
        viewModelScope.launch {
            rideRequestRepository.markArrivedAtPickup(rideId)
            _events.emit(PickupNavigationEvent.NavigateToDropoffFlow)
        }
    }
}
