package com.newuperapp.Uper.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.newuperapp.Uper.domain.model.DriverProfile
import com.newuperapp.Uper.ui.components.AberDrawer
import com.newuperapp.Uper.ui.components.DrawerMenuItem
import com.newuperapp.Uper.ui.theme.AberColor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * The primary entry point for the Home screen, managing the navigation drawer
 * and interaction with the [HomeViewModel].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenMenu: () -> Unit,
    onNavigateToBookingDetails: (rideId: String) -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToInviteFriends: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToBookingDetails -> onNavigateToBookingDetails(event.rideId)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState, 
        drawerContent = {
            uiState.driverProfile?.let { profile ->
                AberDrawer(
                    profile = profile, 
                    onMenuItemClick = { item ->
                        scope.launch { drawerState.close() }
                        when (item) {
                            DrawerMenuItem.Home -> {}
                            DrawerMenuItem.History -> onNavigateToHistory()
                            DrawerMenuItem.Notifications -> onNavigateToNotifications()
                            DrawerMenuItem.InviteFriends -> onNavigateToInviteFriends()
                            DrawerMenuItem.Settings -> onNavigateToSettings()
                            DrawerMenuItem.Wallet -> onNavigateToWallet()
                            DrawerMenuItem.Profile -> onNavigateToProfile()
                            DrawerMenuItem.Logout -> { /* Handle logout */ }
                        }
                    }
                )
            }
        }
    ) {
        HomeScreen(
            uiState = uiState,
            onToggleOnline = viewModel::onToggleOnline,
            onMenuClick = { scope.launch { drawerState.open() } },
            onRequestCardClick = viewModel::onRequestCardClick,
            onAcceptClick = viewModel::onAcceptRide,
            onIgnoreClick = viewModel::onIgnoreRide
        )
    }
}

/**
 * Main dashboard UI presenting the map, online/offline status, and ride requests.
 */
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onToggleOnline: (Boolean) -> Unit,
    onMenuClick: () -> Unit,
    onRequestCardClick: (String) -> Unit,
    onAcceptClick: (String) -> Unit,
    onIgnoreClick: (String) -> Unit,
) {
    val profile = uiState.driverProfile
    val hasRequests = uiState.isOnline && uiState.pendingRequests.isNotEmpty()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(profile?.currentLat ?: 60.1699, profile?.currentLng ?: 24.9384), 15f
        )
    }

    val sheetState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = if (hasRequests) 420.dp else 260.dp,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetContainerColor = AberColor.White,
        sheetDragHandle = { SheetDragHandle() },
        topBar = {
            HomeTopBar(
                isOnline = uiState.isOnline, onToggle = onToggleOnline, onMenuClick = onMenuClick
            )
        },
        sheetContent = {
            if (hasRequests) {
                PendingRequestsSheetContent(
                    requests = uiState.pendingRequests,
                    expandedRequestId = uiState.expandedRequestId,
                    onCardClick = onRequestCardClick,
                    onAcceptClick = onAcceptClick,
                    onIgnoreClick = onIgnoreClick
                )
            } else if (profile != null) {
                DriverStatsSheetContent(profile = profile)
            }
        }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = false),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false, myLocationButtonEnabled = false
                )
            ) {
                Marker(
                    state = MarkerState(
                        position = LatLng(profile?.currentLat ?: 60.1699, profile?.currentLng ?: 24.9384)
                    ), 
                    title = "You"
                )
                if (hasRequests) {
                    uiState.pendingRequests.forEach { request ->
                        Marker(
                            state = MarkerState(
                                position = LatLng(request.pickupLocation.lat, request.pickupLocation.lng)
                            ), 
                            title = request.riderName
                        )
                    }
                }
            }

            if (!uiState.isOnline) {
                OfflineBanner(modifier = Modifier.align(Alignment.TopCenter))
            } else if (hasRequests) {
                NewRequestsBanner(
                    count = uiState.pendingRequests.size,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

            RecenterFab(
                onClick = {
                    profile?.let {
                        cameraPositionState.position =
                            CameraPosition.fromLatLngZoom(LatLng(it.currentLat, it.currentLng), 15f)
                    }
                }, 
                modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeUiState(
            isOnline = false, 
            driverProfile = DriverProfile(
                "1", "Taher", "Pro", null, 100.0, "$", 5.0, 20.0, 10, 60.1699, 24.9384
            )
        ),
        onToggleOnline = {},
        onMenuClick = {},
        onRequestCardClick = {},
        onAcceptClick = {},
        onIgnoreClick = {}
    )
}
