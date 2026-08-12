package com.newuperapp.Uper.domain.model

data class DriverProfile(
    val id: String,
    val name: String,
    val level: String,
    val avatarUrl: String?,
    val totalEarned: Double,
    val currencySymbol: String = "$",
    val hoursOnline: Double,
    val totalDistanceKm: Double,
    val totalJobs: Int,
    val currentLat: Double,
    val currentLng: Double
)
