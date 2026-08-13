package com.newuperapp.Uper.ui.screens.home.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newuperapp.Uper.domain.model.Notification
import com.newuperapp.Uper.domain.repository.DriverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val driverRepository: DriverRepository
) : ViewModel() {

    val uiState: StateFlow<NotificationsUiState> = driverRepository.observeNotifications()
        .map { NotificationsUiState(notifications = it, isLoading = false) }
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
