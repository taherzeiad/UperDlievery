package com.newuperapp.uper.domain.model

data class LatLngPoint(val lat: Double, val lng: Double)

enum class RidePaymentTag {
    APPLE_PAY, DISCOUNT, CASH, CARD;

    companion object {
        fun fromString(value: String): RidePaymentTag? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }

    val label: String
        get() = when (this) {
            APPLE_PAY -> "ApplePay"
            DISCOUNT -> "Discount"
            CASH -> "Cash"
            CARD -> "Card"
        }
}

data class RideRequest(
    val id: String,
    val riderName: String,
    val riderAvatarUrl: String?,
    val price: Double,
    val currencySymbol: String = "$",
    val distanceKm: Double,
    val tags: List<RidePaymentTag>,
    val pickupAddress: String,
    val pickupLocation: LatLngPoint,
    val dropoffAddress: String,
    val dropoffLocation: LatLngPoint,
    /** Seconds the driver has left to respond before the request auto-expires. */
    val expiresInSeconds: Int = 15
)
