package com.newuperapp.Uper.navigation

sealed class AberDestination(val route: String) {
    data object Splash : AberDestination("splash")
    data object Onboarding : AberDestination("onboarding")
    data object SignUp : AberDestination("sign_up")
    data object SignIn : AberDestination("sign_in")
    data object PhoneVerify : AberDestination("phone_verify/{phone}") {
        fun createRoute(phone: String) = "phone_verify/$phone"
    }
    data object EnableLocation : AberDestination("enable_location")
    data object Home : AberDestination("home")
    data object BookingDetails : AberDestination("booking_details/{rideId}") {
        fun createRoute(rideId: String) = "booking_details/$rideId"
    }
    data object PickupNavigation : AberDestination("pickup_navigation/{rideId}") {
        fun createRoute(rideId: String) = "pickup_navigation/$rideId"
    }

    companion object {
        const val ARG_PHONE = "phone"
        const val ARG_RIDE_ID = "rideId"
    }
}
