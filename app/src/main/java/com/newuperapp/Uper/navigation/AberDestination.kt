package com.newuperapp.Uper.navigation

sealed class AberDestination(val route: String) {
    data object Splash : AberDestination("splash")
    data object Onboarding : AberDestination("onboarding")
    data object EnableLocation : AberDestination("enable_location")
    data object Home : AberDestination("home")
}