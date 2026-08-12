package com.newuperapp.Uper.ui.screens.home.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.domain.home.NavigationStep
import com.newuperapp.Uper.domain.home.PickupNavigationState
import com.newuperapp.Uper.domain.home.TurnManeuver
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonVariant
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupNavigationRoute(
    viewModel: PickupNavigationViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToDropoffFlow: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                PickupNavigationEvent.NavigateToDropoffFlow -> onNavigateToDropoffFlow()
            }
        }
    }

    PickupNavigationScreen(
        state = uiState,
        onBackClick = onBackClick,
        onArrivedClick = viewModel::onArrivedAtPickupClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupNavigationScreen(
    state: PickupNavigationState?,
    onBackClick: () -> Unit,
    onArrivedClick: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        state?.let {
            position = CameraPosition.fromLatLngZoom(LatLng(it.driverLocation.latitude, it.driverLocation.longitude), 16f)
        }
    }
    val sheetState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 150.dp,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetContainerColor = AberColor.White,
        sheetDragHandle = { SheetDragHandle() },
        topBar = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().background(AberColor.White).padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AberColor.Yellow, modifier = Modifier.size(26.dp))
                    }
                    Text(
                        "Pick up",
                        style = AberTypography.CardTitle.copy(fontSize = 20.sp),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.width(48.dp))
                }
                state?.let { TurnBanner(it.currentBanner) }
            }
        },
        sheetContent = {
            state?.let {
                PickupSheetContent(state = it, onArrivedClick = onArrivedClick)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = false),
                uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
            ) {
                state?.let {
                    Polyline(
                        points = it.routePolyline.map { p -> LatLng(p.latitude, p.longitude) },
                        color = AberColor.RouteBlue,
                        width = 10f
                    )
                }
            }

            state?.let {
                DriverLocationMarker(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun TurnBanner(step: NavigationStep) {
    Row(
        modifier = Modifier.fillMaxWidth().background(AberColor.Orange).padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(step.maneuver.toIcon(), contentDescription = null, tint = AberColor.Ink)
        Text(step.distanceText, style = AberTypography.semibody17(AberColor.Ink).copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
        Text(step.instruction, style = AberTypography.semibody17(AberColor.Ink), maxLines = 1)
    }
}

@Composable
private fun DriverLocationMarker(modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(140.dp), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.size(140.dp).background(AberColor.Yellow.copy(alpha = 0.35f), CircleShape))
        Box(
            modifier = Modifier.size(52.dp).background(AberColor.White, CircleShape).border(1.dp, AberColor.BorderGray.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.size(42.dp).background(AberColor.Yellow, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Navigation, contentDescription = null, tint = AberColor.Ink)
            }
        }
    }
}

@Composable
private fun SheetDragHandle() {
    Box(modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 4.dp), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.width(40.dp).height(4.dp).background(AberColor.BorderGray, RoundedCornerShape(2.dp)))
    }
}

@Composable
private fun PickupSheetContent(state: PickupNavigationState, onArrivedClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(AberColor.Orange),
                contentAlignment = Alignment.Center
            ) { Text("A", style = AberTypography.semibody17(AberColor.White).copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) }
            Spacer(Modifier.width(14.dp))
            Column {
                Text("Pick up at", style = AberTypography.Caption)
                Text(state.pickupAddress, style = AberTypography.CardTitle)
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricColumn(label = "EST", value = "${state.etaMinutes} min")
            MetricColumn(label = "Distance", value = "${state.distanceKm} km")
            MetricColumn(label = "Fare", value = "$${"%.2f".format(state.fare)}")
        }

        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
            AberButton(text = "Drop off", onClick = onArrivedClick, variant = AberButtonVariant.Primary)
        }

        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.3f))

        state.steps.forEach { step -> DirectionRow(step) }
    }
}

@Composable
private fun MetricColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = AberTypography.Caption)
        Spacer(Modifier.height(4.dp))
        Text(value, style = AberTypography.CardTitle.copy(fontSize = 19.sp))
    }
}

@Composable
private fun DirectionRow(step: NavigationStep) {
    val contentColor = if (step.isActive) AberColor.Orange else AberColor.Ink
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp)) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Icon(step.maneuver.toIcon(), contentDescription = null, tint = contentColor, modifier = Modifier.size(22.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    step.instruction,
                    style = AberTypography.semibody17(contentColor).copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                )
                step.subtext?.let {
                    Spacer(Modifier.height(2.dp))
                    Text(it, style = AberTypography.Caption)
                }
            }
        }
        if (step.distanceText.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.3f))
            Spacer(Modifier.height(4.dp))
            Text(step.distanceText, style = AberTypography.Caption.copy(color = if (step.isActive) AberColor.Orange else AberColor.BorderGray))
        }
    }
}

private fun TurnManeuver.toIcon(): ImageVector = when (this) {
    TurnManeuver.STRAIGHT -> Icons.Default.ArrowUpward
    TurnManeuver.TURN_LEFT -> Icons.Default.TurnLeft
    TurnManeuver.TURN_RIGHT -> Icons.Default.TurnRight
    TurnManeuver.SLIGHT_LEFT -> Icons.Default.TurnLeft
    TurnManeuver.SLIGHT_RIGHT -> Icons.AutoMirrored.Filled.ArrowForward
    TurnManeuver.ARRIVED -> Icons.Default.Navigation
}

@Preview(showBackground = true)
@Composable
private fun PickupNavigationScreenPreview() {
    PickupNavigationScreen(
        state = PickupNavigationState(
            rideId = "ride_123",
            riderName = "Esther Berry",
            pickupAddress = "7958 Swift Village",
            distanceToPickup = "1.2 km",
            timeToPickup = "4 min",
            driverLocation = com.newuperapp.Uper.domain.home.LatLng(60.1699, 24.9384),
            riderLocation = com.newuperapp.Uper.domain.home.LatLng(60.1710, 24.9400),
            routePolyline = listOf(
                com.newuperapp.Uper.domain.home.LatLng(60.1699, 24.9384),
                com.newuperapp.Uper.domain.home.LatLng(60.1710, 24.9400)
            ),
            currentBanner = NavigationStep(
                instruction = "Turn left onto Main St",
                distanceText = "200 m",
                maneuver = TurnManeuver.TURN_LEFT,
                isActive = true
            ),
            steps = listOf(
                NavigationStep(
                    instruction = "Turn left onto Main St",
                    distanceText = "200 m",
                    maneuver = TurnManeuver.TURN_LEFT,
                    isActive = true
                ),
                NavigationStep(
                    instruction = "Go straight for 1 km",
                    distanceText = "1 km",
                    maneuver = TurnManeuver.STRAIGHT
                )
            ),
            etaMinutes = 4,
            distanceKm = 1.2,
            fare = 25.0
        ),
        onBackClick = {},
        onArrivedClick = {}
    )
}
