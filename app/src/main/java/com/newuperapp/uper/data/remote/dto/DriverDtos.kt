package com.newuperapp.uper.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DriverProfileDto(
    val id: String,
    val name: String,
    val level: String,
    val avatarUrl: String? = null,
    val totalEarned: Double,
    val hoursOnline: Double,
    val totalDistanceKm: Double,
    val totalJobs: Int,
    val currentLat: Double,
    val currentLng: Double,
    val currencySymbol: String = "$"
)

@Serializable
data class HistoryItemDto(
    val id: String,
    val riderName: String,
    val riderAvatarUrl: String? = null,
    val price: Double,
    val distanceKm: Double,
    val pickupAddress: String,
    val dropoffAddress: String,
    val date: String,
    val paymentTags: List<String> = emptyList()
)

@Serializable
data class WalletDto(
    val totalEarned: Double,
    val balance: Double,
    val currency: String = "$",
    val transactions: List<TransactionDto> = emptyList()
)

@Serializable
data class TransactionDto(
    val id: String,
    val name: String,
    val transactionNumber: String,
    val amount: Double,
    val avatarUrl: String? = null
)

@Serializable
data class NotificationDto(
    val id: String,
    val type: String, // "SYSTEM", "PROMOTION", etc.
    val title: String,
    val message: String,
    val timestamp: String
)

@Serializable
data class VehicleDto(
    val id: String,
    val brand: String,
    val model: String,
    val year: String,
    val plateNumber: String,
    val color: String
)
