package com.newuperapp.Uper.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.newuperapp.Uper.domain.model.DriverProfile
import com.newuperapp.Uper.domain.model.RidePaymentTag
import com.newuperapp.Uper.domain.model.RideRequest
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonStyle
import com.newuperapp.Uper.ui.components.AberDrawer
import com.newuperapp.Uper.ui.components.DrawerMenuItem
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

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
        drawerState = drawerState, drawerContent = {
            uiState.driverProfile?.let { profile ->
                AberDrawer(
                    profile = profile, onMenuItemClick = { item ->
                        scope.launch { drawerState.close() }
                        when (item) {
                            DrawerMenuItem.Home -> {}
                            DrawerMenuItem.History -> onNavigateToHistory()
                            DrawerMenuItem.Notifications -> onNavigateToNotifications()
                            DrawerMenuItem.InviteFriends -> onNavigateToInviteFriends()
                            DrawerMenuItem.Settings -> onNavigateToSettings()
                            DrawerMenuItem.Wallet -> onNavigateToWallet()
                            DrawerMenuItem.Profile -> onNavigateToProfile()
                            DrawerMenuItem.Logout -> { /* Handle logout */
                            }
                        }
                    })
            }
        }) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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
                        position = LatLng(
                            profile?.currentLat ?: 60.1699, profile?.currentLng ?: 24.9384
                        )
                    ), title = "You"
                )
                if (hasRequests) {
                    uiState.pendingRequests.forEach { request ->
                        Marker(
                            state = MarkerState(
                                position = LatLng(
                                    request.pickupLocation.lat, request.pickupLocation.lng
                                )
                            ), title = request.riderName
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
                }, modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            )
        }
    }
}

@Composable
private fun HomeTopBar(isOnline: Boolean, onToggle: (Boolean) -> Unit, onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AberColor.White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = AberColor.Ink)
        }
        Text(
            text = if (isOnline) "Online" else "Offline",
            style = AberTypography.ScreenTitle.copy(fontSize = 20.sp),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Switch(
            checked = isOnline, onCheckedChange = onToggle, colors = SwitchDefaults.colors(
                checkedThumbColor = AberColor.White,
                checkedTrackColor = AberColor.Orange,
                uncheckedThumbColor = AberColor.White,
                uncheckedTrackColor = AberColor.Ink
            )
        )
    }
}

@Composable
private fun OfflineBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AberColor.Orange)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .border(2.dp, AberColor.Ink.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.DarkMode,
                contentDescription = null,
                tint = AberColor.Ink,
                modifier = Modifier.size(20.dp)
            )
        }
        Column {
            Text("You are offline !", style = AberTypography.CardTitle.copy(fontSize = 16.sp))
            Text(
                "Go online to start accepting jobs.",
                style = AberTypography.Caption.copy(color = AberColor.Ink.copy(alpha = 0.7f))
            )
        }
    }
}

/** Orange "You have N new requests." banner shown while offers are queued (Home Online, swipe-up state). */
@Composable
private fun NewRequestsBanner(count: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AberColor.Orange)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            "You have $count new requests.",
            style = AberTypography.semibody17(AberColor.Ink).copy(fontSize = 17.sp)
        )
    }
}

@Composable
private fun RecenterFab(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(48.dp)
            .background(AberColor.White, CircleShape)
            .border(1.dp, AberColor.BorderGray.copy(alpha = 0.5f), CircleShape)
    ) {
        Icon(Icons.Default.MyLocation, contentDescription = "Recenter", tint = AberColor.Ink)
    }
}

@Composable
private fun SheetDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(AberColor.BorderGray, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
private fun DriverStatsSheetContent(profile: DriverProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(AberColor.SurfaceGray)
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(profile.name, style = AberTypography.CardTitle)
                Text(profile.level, style = AberTypography.Caption)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${profile.currencySymbol}${"%.2f".format(profile.totalEarned)}",
                    style = AberTypography.PriceTag
                )
                Text("Earned", style = AberTypography.Caption)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(AberColor.Yellow)
                .padding(vertical = 22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.AccessTime,
                    value = "${profile.hoursOnline}",
                    label = "HOURS ONLINE"
                )
                StatItem(
                    icon = Icons.Default.Speed,
                    value = "${profile.totalDistanceKm.toInt()} KM",
                    label = "TOTAL DISTANCE"
                )
                StatItem(
                    icon = Icons.Default.Route, value = "${profile.totalJobs}", label = "TOTAL JOBS"
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = AberColor.Ink)
        Spacer(Modifier.height(6.dp))
        Text(value, style = AberTypography.StatValue)
        Text(label, style = AberTypography.StatLabel)
    }
}

/**
 * Scrollable queue of pending offers (the "swipe up" state on Home Online).
 * Tapping a collapsed card expands it in place to reveal its Accept button —
 * matches the reference where only the tapped card grows an "Accept" CTA.
 */
@Composable
private fun PendingRequestsSheetContent(
    requests: List<RideRequest>,
    expandedRequestId: String?,
    onCardClick: (String) -> Unit,
    onAcceptClick: (String) -> Unit,
    onIgnoreClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 640.dp)
    ) {
        itemsIndexed(requests, key = { _, item -> item.id }) { index, request ->
            RequestQueueCard(
                request = request,
                isExpanded = request.id == expandedRequestId,
                onClick = { onCardClick(request.id) },
                onAcceptClick = { onAcceptClick(request.id) })
            if (index != requests.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(AberColor.SurfaceGrayAlt)
                )
            }
        }
    }
}

@Composable
private fun RequestQueueCard(
    request: RideRequest, isExpanded: Boolean, onClick: () -> Unit, onAcceptClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AberColor.SurfaceGray)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(AberColor.BorderGray.copy(alpha = 0.4f))
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(request.riderName, style = AberTypography.CardTitle)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    request.tags.forEach { tag -> RequestTagPill(tag) }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${request.currencySymbol}${"%.2f".format(request.price)}",
                    style = AberTypography.PriceTag
                )
                Text("${request.distanceKm} km", style = AberTypography.Caption)
            }
        }

        Column(modifier = Modifier.background(AberColor.White)) {
            AddressBlock(label = "Pick up", address = request.pickupAddress)
            HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.4f))
            AddressBlock(label = "Drop off", address = request.dropoffAddress)

            if (isExpanded) {
                HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.4f))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    AberButton(
                        text = "Accept", onClick = onAcceptClick, style = AberButtonStyle.Primary
                    )
                }
            }
        }
    }
}

@Composable
private fun RequestTagPill(tag: RidePaymentTag) {
    val label = when (tag) {
        RidePaymentTag.APPLE_PAY -> "ApplePay"
        RidePaymentTag.DISCOUNT -> "Discount"
        RidePaymentTag.CASH -> "Cash"
        RidePaymentTag.CARD -> "Card"
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(AberColor.TagBackground)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            label, style = AberTypography.Caption.copy(
                color = AberColor.Ink,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            )
        )
    }
}

@Composable
private fun AddressBlock(label: String, address: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(label.uppercase(), style = AberTypography.SectionLabel)
        Spacer(Modifier.height(6.dp))
        Text(address, style = AberTypography.semibody17())
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeUiState(
        isOnline = false, driverProfile = DriverProfile(
            "1", "Taher", "Pro", null, 100.0, "$", 5.0, 20.0, 10, 60.1699, 24.9384
        )
    ),
        onToggleOnline = {},
        onMenuClick = {},
        onRequestCardClick = {},
        onAcceptClick = {},
        onIgnoreClick = {})
}
