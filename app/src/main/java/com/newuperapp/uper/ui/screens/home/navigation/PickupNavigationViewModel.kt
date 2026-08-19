package com.newuperapp.uper.ui.screens.home.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.uper.domain.model.PickupNavigationState
import com.newuperapp.uper.domain.repository.RideRequestRepository
import com.newuperapp.uper.domain.utils.Resource
import com.newuperapp.uper.navigation.AberDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PickupNavigationUiState(
    val navigationState: PickupNavigationState? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

sealed interface PickupNavigationEvent {
    data object NavigateToDropoffFlow : PickupNavigationEvent
    data class ShowError(val message: String) : PickupNavigationEvent
}

@HiltViewModel
class PickupNavigationViewModel @Inject constructor(
    private val rideRequestRepository: RideRequestRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val rideId: String = savedStateHandle.get<String>(AberDestination.ARG_RIDE_ID).orEmpty()

    val uiState: StateFlow<PickupNavigationUiState> = rideRequestRepository
        .observePickupNavigation(rideId)
        .map { resource ->
            when (resource) {
                is Resource.Success -> PickupNavigationUiState(navigationState = resource.data, isLoading = false)
                is Resource.Error -> PickupNavigationUiState(isLoading = false, errorMessage = resource.message)
                is Resource.Loading -> PickupNavigationUiState(isLoading = true)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PickupNavigationUiState())

    private val _events = MutableSharedFlow<PickupNavigationEvent>()
    val events: SharedFlow<PickupNavigationEvent> = _events

    fun onArrivedAtPickupClick() {
        viewModelScope.launch {
            when (val result = rideRequestRepository.markArrivedAtPickup(rideId)) {
                is Resource.Success -> _events.emit(PickupNavigationEvent.NavigateToDropoffFlow)
                is Resource.Error -> _events.emit(PickupNavigationEvent.ShowError(result.message))
                else -> {}
            }
        }
    }
}
