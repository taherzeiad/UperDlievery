package com.newuperapp.uper.ui.screens.home.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.uper.domain.model.Notification
import com.newuperapp.uper.domain.repository.DriverRepository
import com.newuperapp.uper.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val driverRepository: DriverRepository
) : ViewModel() {

    val uiState: StateFlow<NotificationsUiState> = driverRepository.observeNotifications()
        .map { resource ->
            when (resource) {
                is Resource.Success -> NotificationsUiState(notifications = resource.data, isLoading = false)
                is Resource.Error -> NotificationsUiState(isLoading = false, errorMessage = resource.message)
                is Resource.Loading -> NotificationsUiState(isLoading = true)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationsUiState()
        )

    fun markAsRead(id: String) {
        viewModelScope.launch {
            driverRepository.markNotificationAsRead(id)
        }
    }
}
