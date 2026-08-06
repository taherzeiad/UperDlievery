package com.newuperapp.Uper.domain.home

data class LatLng(val latitude: Double, val longitude: Double)

data class DriverProfile(
    val name: String,
    val level: String,
    val avatarUrl: String?,
    val totalEarned: Double,
    val hoursOnline: Double,
    val totalDistanceKm: Double,
    val totalJobs: Int
)

data class RideRequest(
    val passengerName: String,
    val passengerAvatarUrl: String?,
    val fare: Double,
    val distanceKm: Double,
    val tags: List<String>,
    val pickupAddress: String,
    val dropoffAddress: String,
    val driverLocation: LatLng,
    val pickupLocation: LatLng,
    val dropoffLocation: LatLng
)