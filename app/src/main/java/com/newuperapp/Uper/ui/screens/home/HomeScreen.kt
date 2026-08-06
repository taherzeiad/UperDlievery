package com.newuperapp.Uper.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng as GoogleLatLng
import com.google.maps.android.compose.*
import com.newuperapp.Uper.domain.home.DriverProfile
import com.newuperapp.Uper.domain.home.LatLng
import com.newuperapp.Uper.domain.home.RideRequest
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonVariant
import com.newuperapp.Uper.ui.theme.AberColor

private fun LatLng.toGoogle() = GoogleLatLng(latitude, longitude)

@Composable
fun HomeRoute(
    onOpenMenu: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreen(
        uiState = uiState,
        onToggleOnline = viewModel::onToggleOnline,
        onAcceptRequest = viewModel::onAcceptRequest,
        onIgnoreRequest = viewModel::onIgnoreRequest,
        onMenuClick = onOpenMenu
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onToggleOnline: () -> Unit,
    onAcceptRequest: () -> Unit,
    onIgnoreRequest: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        HomeMap(
            currentLocation = uiState.currentLocation,
            activeRequest = uiState.activeRequest,
            modifier = Modifier.fillMaxSize()
        )

        HomeTopBar(
            isOnline = uiState.isOnline,
            onMenuClick = onMenuClick,
            onToggleOnline = onToggleOnline,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
        )

        if (!uiState.isOnline) {
            OfflineBanner(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp)
            )
        }

        uiState.driverProfile?.let { profile ->
            DriverStatsCard(
                profile = profile,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .padding(bottom = 32.dp)
            )
        }

        uiState.activeRequest?.let { request ->
            RideRequestCard(
                request = request,
                onAccept = onAcceptRequest,
                onIgnore = onIgnoreRequest,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun HomeTopBar(
    isOnline: Boolean,
    onMenuClick: () -> Unit,
    onToggleOnline: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .background(Color.White, CircleShape)
                .size(48.dp)
        ) {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
        }

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isOnline) "ONLINE" else "OFFLINE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isOnline,
                    onCheckedChange = { onToggleOnline() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AberColor.Yellow
                    )
                )
            }
        }
    }
}

@Composable
fun OfflineBanner(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color.Black.copy(alpha = 0.8f),
        contentColor = Color.White
    ) {
        Text(
            text = "You're offline. Go online to start receiving requests.",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeMap(
    currentLocation: LatLng,
    activeRequest: RideRequest?,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation.toGoogle(), 15f)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = currentLocation.toGoogle()),
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
        )

        activeRequest?.let { request ->
            Marker(
                state = MarkerState(position = request.pickupLocation.toGoogle()),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )
            Marker(
                state = MarkerState(position = request.dropoffLocation.toGoogle()),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
            Polyline(
                points = listOf(
                    request.pickupLocation.toGoogle(),
                    request.dropoffLocation.toGoogle()
                ),
                color = AberColor.Yellow,
                width = 5f
            )
        }
    }
}

@Composable
fun DriverStatsCard(
    profile: DriverProfile,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem(Icons.Default.Speed, "Jobs", "${profile.totalJobs}")
            StatItem(Icons.Default.AccessTime, "Hours", "${profile.hoursOnline}h")
            StatItem(Icons.Default.NightsStay, "Level", profile.level)
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = AberColor.Ink.copy(alpha = 0.6f))
        Text(text = label, fontSize = 12.sp, color = AberColor.Ink.copy(alpha = 0.6f))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RideRequestCard(
    request: RideRequest,
    onAccept: () -> Unit,
    onIgnore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 12.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "New Ride Request", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "$${request.fare}", color = AberColor.Orange, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            AddressRow("Pickup", request.pickupAddress)
            Spacer(modifier = Modifier.height(8.dp))
            AddressRow("Drop-off", request.dropoffAddress)
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AberButton(
                    text = "Ignore",
                    onClick = onIgnore,
                    variant = AberButtonVariant.Outline,
                    modifier = Modifier.weight(1f)
                )
                AberButton(
                    text = "Accept",
                    onClick = onAccept,
                    variant = AberButtonVariant.Primary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AddressRow(label: String, address: String) {
    Row {
        Text(text = "$label: ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Text(text = address, fontSize = 14.sp, color = AberColor.Ink.copy(alpha = 0.7f))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenOfflinePreview() {
    HomeScreen(
        uiState = HomeUiState(isOnline = false),
        onToggleOnline = {},
        onAcceptRequest = {},
        onIgnoreRequest = {},
        onMenuClick = {}
    )
}
