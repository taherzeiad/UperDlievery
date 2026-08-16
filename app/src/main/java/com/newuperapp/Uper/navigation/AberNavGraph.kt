package com.newuperapp.uper.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.newuperapp.uper.ui.screens.auth.WelcomeScreen
import com.newuperapp.uper.ui.screens.auth.otp.PhoneVerifyRoute
import com.newuperapp.uper.ui.screens.auth.signin.SignInRoute
import com.newuperapp.uper.ui.screens.auth.signup.SignUpRoute
import com.newuperapp.uper.ui.screens.home.HomeRoute
import com.newuperapp.uper.ui.screens.home.booking.BookingDetailsRoute
import com.newuperapp.uper.ui.screens.home.chat.ChatScreen
import com.newuperapp.uper.ui.screens.home.document.DocumentManagementScreen
import com.newuperapp.uper.ui.screens.home.document.DrivingLicenseScreen
import com.newuperapp.uper.ui.screens.home.history.HistoryScreen
import com.newuperapp.uper.ui.screens.home.invite.InviteFriendsScreen
import com.newuperapp.uper.ui.screens.home.navigation.PickupNavigationRoute
import com.newuperapp.uper.ui.screens.home.notifications.NotificationsScreen
import com.newuperapp.uper.ui.screens.home.payment.PaymentMethodScreen
import com.newuperapp.uper.ui.screens.home.profile.EditProfileScreen
import com.newuperapp.uper.ui.screens.home.profile.ProfileScreen
import com.newuperapp.uper.ui.screens.home.settings.SettingsScreen
import com.newuperapp.uper.ui.screens.home.vehicle.AddVehicleScreen
import com.newuperapp.uper.ui.screens.home.vehicle.VehicleManagementScreen
import com.newuperapp.uper.ui.screens.home.wallet.WalletScreen
import com.newuperapp.uper.ui.screens.location.EnableLocationRoute
import com.newuperapp.uper.ui.screens.onboarding.OnboardingRoute
import com.newuperapp.uper.ui.screens.splash.SplashRoute

/**
 * Central navigation graph for the entire application.
 * Defines all routes and transitions between them.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AberNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController, startDestination = AberDestination.SignIn.route
    ) {
        addAppStartFlow(navController)
        addAuthFlow(navController)
        addHomeFlow(navController)
    }
}

/**
 * Initial application flow: Splash, Onboarding, and Location Permission.
 */
private fun NavGraphBuilder.addAppStartFlow(navController: NavHostController) {
    composable(AberDestination.Splash.route) {
        SplashRoute(onNavigateToOnboarding = {
            navController.navigate(AberDestination.Onboarding.route) {
                popUpTo(AberDestination.Splash.route) { inclusive = true }
            }
        }, onNavigateToHome = {
            navController.navigate(AberDestination.Home.route) {
                popUpTo(AberDestination.Splash.route) { inclusive = true }
            }
        })
    }

    composable(AberDestination.Onboarding.route) {
        OnboardingRoute(
            onFinished = {
                navController.navigate(AberDestination.Welcome.route) {
                    popUpTo(AberDestination.Onboarding.route) { inclusive = true }
                }
            })
    }

    composable(AberDestination.EnableLocation.route) {
        EnableLocationRoute(
            onLocationResolved = {
                navController.navigate(AberDestination.Home.route) {
                    popUpTo(AberDestination.EnableLocation.route) { inclusive = true }
                }
            })
    }
}

/**
 * Authentication flow: Welcome selection, Sign Up, Sign In, and OTP Verification.
 */
private fun NavGraphBuilder.addAuthFlow(navController: NavHostController) {
    composable(AberDestination.Welcome.route) {
        WelcomeScreen(
            onNavigateToSignUp = { navController.navigate(AberDestination.SignUp.route) },
            onNavigateToSignIn = { navController.navigate(AberDestination.SignIn.route) })
    }

    composable(AberDestination.SignUp.route) {
        SignUpRoute(
            onNavigateToOtp = { phone ->
                navController.navigate(AberDestination.PhoneVerify.createRoute(phone))
            },
            onNavigateToSignIn = {
                navController.navigate(AberDestination.SignIn.route)
            },
        )
    }

    composable(AberDestination.SignIn.route) {
        SignInRoute(onNavigateToOtp = { phone ->
            navController.navigate(AberDestination.PhoneVerify.createRoute(phone))
        }, onNavigateToSignUp = {
            navController.navigate(AberDestination.SignUp.route)
        })
    }

    composable(
        route = AberDestination.PhoneVerify.route,
        arguments = listOf(navArgument(AberDestination.ARG_PHONE) { type = NavType.StringType })
    ) {
        PhoneVerifyRoute(onBackClick = { navController.popBackStack() }, onNavigateToHome = {
            navController.navigate(AberDestination.EnableLocation.route) {
                popUpTo(0) { inclusive = true }
            }
        })
    }
}

/**
 * Main application dashboard and management flows.
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.addHomeFlow(navController: NavHostController) {
    composable(AberDestination.Home.route) {
        HomeRoute(
            onOpenMenu = { /* Drawer handled in HomeRoute */ },
            onNavigateToBookingDetails = { rideId ->
                navController.navigate(AberDestination.BookingDetails.createRoute(rideId))
            },
            onNavigateToHistory = { navController.navigate(AberDestination.History.route) },
            onNavigateToNotifications = { navController.navigate(AberDestination.Notifications.route) },
            onNavigateToInviteFriends = { navController.navigate(AberDestination.InviteFriends.route) },
            onNavigateToSettings = { navController.navigate(AberDestination.Settings.route) },
            onNavigateToWallet = { navController.navigate(AberDestination.Wallet.route) },
            onNavigateToProfile = { navController.navigate(AberDestination.Profile.route) })
    }

    // Ride Details & Active Navigation
    composable(
        route = AberDestination.BookingDetails.route,
        arguments = listOf(navArgument(AberDestination.ARG_RIDE_ID) { type = NavType.StringType })
    ) {
        BookingDetailsRoute(
            onBackClick = { navController.popBackStack() },
            onNavigateToPickup = { rideId ->
                navController.navigate(AberDestination.PickupNavigation.createRoute(rideId))
            })
    }

    composable(
        route = AberDestination.PickupNavigation.route,
        arguments = listOf(navArgument(AberDestination.ARG_RIDE_ID) { type = NavType.StringType })
    ) {
        PickupNavigationRoute(
            onBackClick = { navController.popBackStack() },
            onNavigateToDropoffFlow = {
                navController.popBackStack(AberDestination.Home.route, inclusive = false)
            })
    }

    // Side Drawer Features
    composable(AberDestination.History.route) {
        HistoryScreen(onBackClick = { navController.popBackStack() })
    }

    composable(AberDestination.Notifications.route) {
        NotificationsScreen(onBackClick = { navController.popBackStack() })
    }

    composable(AberDestination.InviteFriends.route) {
        InviteFriendsScreen(onBackClick = { navController.popBackStack() })
    }

    composable(AberDestination.Wallet.route) {
        WalletScreen(
            onBackClick = { navController.popBackStack() },
            onPaymentMethodClick = { navController.navigate(AberDestination.PaymentMethod.route) })
    }

    composable(AberDestination.PaymentMethod.route) {
        PaymentMethodScreen(onBackClick = { navController.popBackStack() })
    }

    composable(
        route = AberDestination.Chat.route,
        arguments = listOf(navArgument("name") { type = NavType.StringType })
    ) { backStackEntry ->
        val name = backStackEntry.arguments?.getString("name") ?: ""
        ChatScreen(name = name, onBackClick = { navController.popBackStack() })
    }

    // Account & Settings
    composable(AberDestination.Settings.route) {
        SettingsScreen(
            onBackClick = { navController.popBackStack() },
            onVehicleManagementClick = { navController.navigate(AberDestination.VehicleManagement.route) },
            onDocumentManagementClick = { navController.navigate(AberDestination.DocumentManagement.route) },
            onProfileClick = { navController.navigate(AberDestination.Profile.route) })
    }

    composable(AberDestination.Profile.route) {
        ProfileScreen(
            onBackClick = { navController.popBackStack() },
            onEditClick = { navController.navigate(AberDestination.EditProfile.route) })
    }

    composable(AberDestination.EditProfile.route) {
        EditProfileScreen(
            onCancelClick = { navController.popBackStack() },
            onDoneClick = { navController.popBackStack() })
    }

    composable(AberDestination.VehicleManagement.route) {
        VehicleManagementScreen(
            onBackClick = { navController.popBackStack() },
            onAddVehicleClick = { navController.navigate(AberDestination.AddVehicle.route) })
    }

    composable(AberDestination.AddVehicle.route) {
        AddVehicleScreen(
            onBackClick = { navController.popBackStack() },
            onCompleteClick = { navController.popBackStack() })
    }

    composable(AberDestination.DocumentManagement.route) {
        DocumentManagementScreen(
            onBackClick = { navController.popBackStack() },
            onDrivingLicenseClick = { navController.navigate(AberDestination.DrivingLicense.route) })
    }

    composable(AberDestination.DrivingLicense.route) {
        DrivingLicenseScreen(
            onBackClick = { navController.popBackStack() },
            onCompleteClick = { navController.popBackStack() })
    }
}
