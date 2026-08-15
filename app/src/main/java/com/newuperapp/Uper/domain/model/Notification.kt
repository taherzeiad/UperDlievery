package com.newuperapp.Uper.domain.model

enum class NotificationType {
    BOOKING_SUCCESS,
    BOOKING_CANCELLED,
    PROMOTION,
    PAYMENT,
    SYSTEM
}

data class Notification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timestamp: String
)
