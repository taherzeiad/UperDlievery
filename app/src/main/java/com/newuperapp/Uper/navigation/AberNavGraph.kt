package com.newuperapp.Uper.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.newuperapp.Uper.ui.screens.auth.WelcomeScreen
import com.newuperapp.Uper.ui.screens.auth.otp.PhoneVerifyRoute
import com.newuperapp.Uper.ui.screens.auth.signin.SignInRoute
import com.newuperapp.Uper.ui.screens.auth.signup.SignUpRoute
import com.newuperapp.Uper.ui.screens.home.HomeRoute
import com.newuperapp.Uper.ui.screens.home.booking.BookingDetailsRoute
import com.newuperapp.Uper.ui.screens.home.chat.ChatScreen
import com.newuperapp.Uper.ui.screens.home.document.DocumentManagementScreen
import com.newuperapp.Uper.ui.screens.home.document.DrivingLicenseScreen
import com.newuperapp.Uper.ui.screens.home.history.HistoryScreen
import com.newuperapp.Uper.ui.screens.home.invite.InviteFriendsScreen
import com.newuperapp.Uper.ui.screens.home.navigation.PickupNavigationRoute
import com.newuperapp.Uper.ui.screens.home.notifications.NotificationsScreen
import com.newuperapp.Uper.ui.screens.home.payment.PaymentMethodScreen
import com.newuperapp.Uper.ui.screens.home.profile.EditProfileScreen
import com.newuperapp.Uper.ui.screens.home.profile.ProfileScreen
import com.newuperapp.Uper.ui.screens.home.settings.SettingsScreen
import com.newuperapp.Uper.ui.screens.home.vehicle.AddVehicleScreen
import com.newuperapp.Uper.ui.screens.home.vehicle.VehicleManagementScreen
import com.newuperapp.Uper.ui.screens.home.wallet.WalletScreen
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
                    navController.navigate(AberDestination.Welcome.route) {
                        popUpTo(AberDestination.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AberDestination.Welcome.route) {
            WelcomeScreen(
                onNavigateToSignUp = { navController.navigate(AberDestination.SignUp.route) },
                onNavigateToSignIn = { navController.navigate(AberDestination.SignIn.route) }
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
                onOpenMenu = { /* Handled internally by drawer in HomeRoute */ },
                onNavigateToBookingDetails = { rideId ->
                    navController.navigate(AberDestination.BookingDetails.createRoute(rideId))
                },
                onNavigateToHistory = { navController.navigate(AberDestination.History.route) },
                onNavigateToNotifications = { navController.navigate(AberDestination.Notifications.route) },
                onNavigateToInviteFriends = { navController.navigate(AberDestination.InviteFriends.route) },
                onNavigateToSettings = { navController.navigate(AberDestination.Settings.route) },
                onNavigateToWallet = { navController.navigate(AberDestination.Wallet.route) },
                onNavigateToProfile = { navController.navigate(AberDestination.Profile.route) }
            )
        }

        composable(AberDestination.History.route) {
            HistoryScreen(onBackClick = { navController.popBackStack() })
        }

        composable(AberDestination.Notifications.route) {
            NotificationsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(AberDestination.InviteFriends.route) {
            InviteFriendsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(AberDestination.Settings.route) {
            SettingsScreen(
                profile = null,
                onBackClick = { navController.popBackStack() },
                onVehicleManagementClick = { navController.navigate(AberDestination.VehicleManagement.route) },
                onDocumentManagementClick = { navController.navigate(AberDestination.DocumentManagement.route) },
                onProfileClick = { navController.navigate(AberDestination.Profile.route) }
            )
        }

        composable(AberDestination.Profile.route) {
            ProfileScreen(
                profile = null,
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate(AberDestination.EditProfile.route) }
            )
        }

        composable(AberDestination.EditProfile.route) {
            EditProfileScreen(
                onCancelClick = { navController.popBackStack() },
                onDoneClick = { navController.popBackStack() }
            )
        }

        composable(AberDestination.VehicleManagement.route) {
            VehicleManagementScreen(
                onBackClick = { navController.popBackStack() },
                onAddVehicleClick = { navController.navigate(AberDestination.AddVehicle.route) }
            )
        }

        composable(AberDestination.AddVehicle.route) {
            AddVehicleScreen(
                onBackClick = { navController.popBackStack() },
                onCompleteClick = { navController.popBackStack() }
            )
        }

        composable(AberDestination.DocumentManagement.route) {
            DocumentManagementScreen(
                onBackClick = { navController.popBackStack() },
                onDrivingLicenseClick = { navController.navigate(AberDestination.DrivingLicense.route) }
            )
        }

        composable(AberDestination.DrivingLicense.route) {
            DrivingLicenseScreen(
                onBackClick = { navController.popBackStack() },
                onCompleteClick = { navController.popBackStack() }
            )
        }

        composable(AberDestination.Wallet.route) {
            WalletScreen(
                onBackClick = { navController.popBackStack() },
                onPaymentMethodClick = { navController.navigate(AberDestination.PaymentMethod.route) }
            )
        }

        composable(AberDestination.PaymentMethod.route) {
            PaymentMethodScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AberDestination.Chat.route,
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            ChatScreen(name = name, onBackClick = { navController.popBackStack() })
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
            SignInRoute(
                onNavigateToOtp = { phone ->
                    navController.navigate(AberDestination.PhoneVerify.createRoute(phone))
                },
                onNavigateToSignUp = {
                    navController.navigate(AberDestination.SignUp.route)
                }
            )
        }

        composable(
            route = AberDestination.PhoneVerify.route,
            arguments = listOf(navArgument(AberDestination.ARG_PHONE) { type = NavType.StringType })
        ) {
            PhoneVerifyRoute(
                onBackClick = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(AberDestination.EnableLocation.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = AberDestination.BookingDetails.route,
            arguments = listOf(navArgument(AberDestination.ARG_RIDE_ID) { type = NavType.StringType })
        ) {
            BookingDetailsRoute(
                onBackClick = { navController.popBackStack() },
                onNavigateToPickup = { rideId ->
                    navController.navigate(AberDestination.PickupNavigation.createRoute(rideId))
                }
            )
        }

        composable(
            route = AberDestination.PickupNavigation.route,
            arguments = listOf(navArgument(AberDestination.ARG_RIDE_ID) { type = NavType.StringType })
        ) {
            PickupNavigationRoute(
                onBackClick = { navController.popBackStack() },
                onNavigateToDropoffFlow = {
                    navController.popBackStack(AberDestination.Home.route, inclusive = false)
                }
            )
        }
    }
}
