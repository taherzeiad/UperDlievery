package com.newuperapp.uper.ui.screens.home.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Straight
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.newuperapp.uper.R
import com.newuperapp.uper.domain.model.NavigationStep
import com.newuperapp.uper.domain.model.PickupNavigationState
import com.newuperapp.uper.domain.model.TurnManeuver
import com.newuperapp.uper.ui.components.AberButton
import com.newuperapp.uper.ui.components.AberButtonStyle
import com.newuperapp.uper.ui.screens.home.SheetDragHandle
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

/**
 * Live navigation screen for the "Go to pickup" flow.
 * Integrates Google Maps with real-time route visualization and metric updates.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupNavigationRoute(
    viewModel: PickupNavigationViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToDropoffFlow: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                PickupNavigationEvent.NavigateToDropoffFlow -> onNavigateToDropoffFlow()
                is PickupNavigationEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    PickupNavigationScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onArrivedClick = viewModel::onArrivedAtPickupClick,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupNavigationScreen(
    uiState: PickupNavigationUiState,
    onBackClick: () -> Unit,
    onArrivedClick: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val state = uiState.navigationState
    val cameraPositionState = rememberCameraPositionState {
        state?.let {
            position = CameraPosition.fromLatLngZoom(
                LatLng(it.driverLocation.lat, it.driverLocation.lng), 16f
            )
        }
    }
    val sheetState = rememberBottomSheetScaffoldState(snackbarHostState = snackbarHostState)

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 150.dp,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetContainerColor = AberColor.White,
        sheetDragHandle = { SheetDragHandle() },
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AberColor.White)
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back_content_desc),
                            tint = AberColor.Yellow,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.nav_pickup_title),
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
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (uiState.errorMessage != null) {
                        Text(uiState.errorMessage, color = AberColor.Orange)
                    } else {
                        CircularProgressIndicator(color = AberColor.Yellow)
                    }
                }
            } else {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = false),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = false
                    )
                ) {
                    val driverPos = remember(state.driverLocation) {
                        LatLng(state.driverLocation.lat, state.driverLocation.lng)
                    }
                    val pickupPos = remember(state.routePolyline) {
                        LatLng(state.routePolyline.last().lat, state.routePolyline.last().lng)
                    }

                    Marker(
                        state = rememberMarkerState(position = driverPos),
                        title = "You"
                    )
                    Marker(
                        state = rememberMarkerState(position = pickupPos),
                        title = "Pickup"
                    )
                    Polyline(
                        points = state.routePolyline.map { LatLng(it.lat, it.lng) },
                        color = AberColor.Yellow,
                        width = 12f
                    )
                }
            }
        }
    }
}

@Composable
fun TurnBanner(step: NavigationStep) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AberColor.Yellow)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (step.maneuver) {
                TurnManeuver.TURN_LEFT -> Icons.Default.TurnLeft
                TurnManeuver.TURN_RIGHT -> Icons.Default.TurnRight
                else -> Icons.Default.Straight
            },
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = AberColor.Ink
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(step.instruction, style = AberTypography.CardTitle)
            Text(step.distanceText, style = AberTypography.Caption)
        }
    }
}

@Composable
fun PickupSheetContent(state: PickupNavigationState, onArrivedClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(state.pickupAddress, style = AberTypography.CardTitle)
                Text("${state.distanceKm} km • ${state.etaMinutes} min", style = AberTypography.Caption)
            }
            Text("$${state.fare}", style = AberTypography.PriceTag)
        }
        Spacer(Modifier.height(20.dp))
        AberButton(
            text = "ARRIVED",
            onClick = onArrivedClick,
            style = AberButtonStyle.Primary
        )
    }
}
