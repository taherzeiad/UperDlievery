package com.newuperapp.Uper.domain.model

data class HistoryItem(
    val id: String,
    val riderName: String,
    val riderAvatarUrl: String?,
    val price: Double,
    val currencySymbol: String = "$",
    val distanceKm: Double,
    val pickupAddress: String,
    val dropoffAddress: String,
    val date: String,
    val paymentTags: List<RidePaymentTag> = emptyList()
)
