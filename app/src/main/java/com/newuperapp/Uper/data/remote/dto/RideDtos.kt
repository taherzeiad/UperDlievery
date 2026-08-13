package com.newuperapp.Uper.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RideRequestDto(
    val id: String,
    val riderName: String,
    val riderAvatarUrl: String? = null,
    val price: Double,
    val distanceKm: Double,
    val pickupAddress: String,
    val pickupLat: Double,
    val pickupLng: Double,
    val dropoffAddress: String,
    val dropoffLat: Double,
    val dropoffLng: Double,
    val tags: List<String> = emptyList()
)

@Serializable
data class BookingDetailsDto(
    val bookingId: String,
    val ride: RideRequestDto,
    val riderPhone: String,
    val note: String? = null,
    val fareBreakdown: List<FareLineDto> = emptyList(),
    val paidAmount: Double
)

@Serializable
data class FareLineDto(
    val label: String,
    val amount: Double
)

@Serializable
data class NavigationStateDto(
    val rideId: String,
    val pickupAddress: String,
    val etaMinutes: Int,
    val distanceKm: Double,
    val fare: Double,
    val currentStep: NavigationStepDto,
    val allSteps: List<NavigationStepDto> = emptyList(),
    val polylinePoints: List<LatLngDto> = emptyList(),
    val driverLat: Double,
    val driverLng: Double
)

@Serializable
data class NavigationStepDto(
    val maneuver: String,
    val instruction: String,
    val distanceText: String,
    val subtext: String? = null,
    val isActive: Boolean = false
)

@Serializable
data class LatLngDto(
    val lat: Double,
    val lng: Double
)
