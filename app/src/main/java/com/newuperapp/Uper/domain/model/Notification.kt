package com.newuperapp.Uper.domain.model

enum class NotificationType {
    SYSTEM, PROMOTION, WALLET, CANCELLED
}

data class Notification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timestamp: String
)
