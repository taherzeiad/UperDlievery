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
    data object History : AberDestination("history")
    data object Notifications : AberDestination("notifications")
    data object InviteFriends : AberDestination("invite_friends")
    data object Settings : AberDestination("settings")
    data object Wallet : AberDestination("wallet")
    data object Chat : AberDestination("chat/{name}") {
        fun createRoute(name: String) = "chat/$name"
    }
    data object Profile : AberDestination("profile")
    data object EditProfile : AberDestination("edit_profile")
    data object VehicleManagement : AberDestination("vehicle_management")
    data object AddVehicle : AberDestination("add_vehicle")
    data object DocumentManagement : AberDestination("document_management")
    data object DrivingLicense : AberDestination("driving_license")
    data object PaymentMethod : AberDestination("payment_method")

    companion object {
        const val ARG_PHONE = "phone"
        const val ARG_RIDE_ID = "rideId"
    }
}
