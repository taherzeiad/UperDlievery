package com.newuperapp.Uper.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.newuperapp.Uper.ui.screens.auth.otp.PhoneVerifyRoute
import com.newuperapp.Uper.ui.screens.auth.signin.SignInRoute
import com.newuperapp.Uper.ui.screens.auth.signup.SignUpRoute
import com.newuperapp.Uper.ui.screens.home.HomeRoute
import com.newuperapp.Uper.ui.screens.home.HomeScreen
import com.newuperapp.Uper.ui.screens.location.EnableLocationRoute
import com.newuperapp.Uper.ui.screens.onboarding.OnboardingRoute
import com.newuperapp.Uper.ui.screens.splash.SplashRoute

@Composable
fun AberNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = AberDestination.Splash.route
    ) {
        composable(AberDestination.Splash.route) {
            SplashRoute(
                onNavigateToOnboarding = {
                    navController.navigate(AberDestination.Onboarding.route) {
                        popUpTo(AberDestination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AberDestination.Home.route) {
                        popUpTo(AberDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AberDestination.Onboarding.route) {
            OnboardingRoute(
                onFinished = {
                    navController.navigate(AberDestination.EnableLocation.route) {
                        popUpTo(AberDestination.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AberDestination.EnableLocation.route) {
            EnableLocationRoute(
                onLocationResolved = {
                    navController.navigate(AberDestination.Home.route) {
                        popUpTo(AberDestination.EnableLocation.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AberDestination.Home.route) {
            HomeRoute(
                onOpenMenu = { /* TODO */ }
            )
        }

        composable(AberDestination.SignUp.route) {
            SignUpRoute(
                onNavigateToOtp = { phone ->
                    navController.navigate(AberDestination.PhoneVerify.createRoute(phone))
                },
                onNavigateToSignIn = {
                    navController.navigate(AberDestination.SignIn.route)
                }
            )
        }

        composable(AberDestination.SignIn.route) {
            SignInRoute(
                onNavigateToOtp = { phone ->
                    navController.navigate(AberDestination.PhoneVerify.createRoute(phone))
                }
            )
        }

        composable(AberDestination.PhoneVerify.route) {
            PhoneVerifyRoute(
                onBackClick = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(AberDestination.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
