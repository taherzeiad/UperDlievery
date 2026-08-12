package com.newuperapp.Uper.domain.model

enum class TurnManeuver { STRAIGHT, TURN_LEFT, TURN_RIGHT, SLIGHT_LEFT, SLIGHT_RIGHT }

data class NavigationStep(
    val maneuver: TurnManeuver,
    val instruction: String,
    val distanceText: String,
    val subtext: String? = null,
    val isActive: Boolean = false
)

data class PickupNavigationState(
    val rideId: String,
    val pickupAddress: String,
    val etaMinutes: Int,
    val distanceKm: Double,
    val fare: Double,
    val currentBanner: NavigationStep,
    val steps: List<NavigationStep>,
    val routePolyline: List<LatLngPoint>,
    val driverLocation: LatLngPoint
)
