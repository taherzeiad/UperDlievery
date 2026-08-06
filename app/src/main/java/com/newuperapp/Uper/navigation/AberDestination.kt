package com.newuperapp.Uper.navigation

sealed class AberDestination(val route: String) {
    data object Splash : AberDestination("splash")
    data object Onboarding : AberDestination("onboarding")
    data object SignUp : AberDestination("sign_up")
    data object SignIn : AberDestination("sign_in")
    data object PhoneVerify : AberDestination("phone_verify")
    data object EnableLocation : AberDestination("enable_location")
    data object Home : AberDestination("home")
}
