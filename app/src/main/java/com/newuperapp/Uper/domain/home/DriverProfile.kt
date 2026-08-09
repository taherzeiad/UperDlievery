package com.newuperapp.Uper.domain.home

data class LatLng(val latitude: Double, val longitude: Double) {
    val lat: Double get() = latitude
    val lng: Double get() = longitude
}

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
    val tags: List<RidePaymentTag>,
    val pickupAddress: String,
    val dropoffAddress: String,
    val driverLocation: LatLng,
    val pickupLocation: LatLng,
    val dropoffLocation: LatLng
)

enum class RidePaymentTag {
    APPLE_PAY, DISCOUNT, CASH, CARD
}

data class BookingDetails(
    val bookingId: String,
    val request: RideRequestShort,
    val riderPhone: String,
    val note: String,
    val fareBreakdown: List<FareLine>,
    val paidAmount: Double
)

data class RideRequestShort(
    val riderName: String,
    val riderRating: Float,
    val tags: List<RidePaymentTag>,
    val price: Double,
    val currencySymbol: String = "$",
    val distanceKm: Double,
    val pickupAddress: String,
    val dropoffAddress: String
)

data class FareLine(val label: String, val amount: Double)

data class PickupNavigationState(
    val rideId: String,
    val riderName: String,
    val pickupAddress: String,
    val distanceToPickup: String,
    val timeToPickup: String,
    val driverLocation: LatLng,
    val riderLocation: LatLng,
    val routePolyline: List<LatLng>,
    val currentBanner: NavigationStep,
    val steps: List<NavigationStep>,
    val etaMinutes: Int,
    val distanceKm: Double,
    val fare: Double
)

data class NavigationStep(
    val instruction: String,
    val distanceText: String,
    val maneuver: TurnManeuver,
    val subtext: String? = null,
    val isActive: Boolean = false
)

enum class TurnManeuver {
    STRAIGHT, TURN_LEFT, TURN_RIGHT, SLIGHT_LEFT, SLIGHT_RIGHT, ARRIVED
}
