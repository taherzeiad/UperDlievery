package com.newuperapp.Uper.ui.screens.home

import android.annotation.SuppressLint
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.newuperapp.Uper.ui.theme.AberColor

private fun LatLng.toGoogle() = GoogleLatLng(latitude, longitude)

@Composable
fun HomeRoute(
    onOpenMenu: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
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
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AberColor.SurfaceGray)
    ) {
        // 1. الخريطة في الخلفية
        HomeMap(
            isOnline = uiState.isOnline,
            currentLocation = uiState.currentLocation,
            activeRequest = uiState.activeRequest,
            modifier = Modifier.fillMaxSize()
        )

        // 2. الشريط العلوي البارز (Top Bar & Offline Banner)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            HomeTopBar(
                isOnline = uiState.isOnline,
                onMenuClick = onMenuClick,
                onToggleOnline = onToggleOnline
            )

            if (!uiState.isOnline) {
                OfflineBanner()
            }
        }

        // 3. زر تحديد الموقع الحالي (Location FAB)
        FloatingActionButton(
            onClick = { /* Center location */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = if ((uiState.activeRequest != null) || (!uiState.isOnline)) 300.dp else 240.dp,
                    end = 16.dp
                )
                .size(48.dp),
            shape = CircleShape,
            containerColor = AberColor.White,
            contentColor = AberColor.Ink,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "My Location",
                modifier = Modifier.size(20.dp)
            )
        }

        // 4. البطاقات السفلية حسب الحالة (Driver Stats or Ride Request)
        if (!uiState.isOnline) {
            DriverStatsCard(
                profile = uiState.driverProfile ?: DriverProfile(
                    name = "Jeremiah Curtis",
                    level = "Basic level",
                    avatarUrl = "", // تم إضافة المتغير المفقود
                    totalEarned = 325.00, // تم التعديل للاسم الصحيح
                    hoursOnline = 10.2,
                    totalDistanceKm = 30.0,
                    totalJobs = 20
                ), modifier = Modifier.align(Alignment.BottomCenter)
            )
        } else {
            uiState.activeRequest?.let { request ->
                RideRequestCard(
                    request = request,
                    onAccept = onAcceptRequest,
                    onIgnore = onIgnoreRequest,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onMenuClick, modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = AberColor.Ink,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = if (isOnline) "Online" else "Offline",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = AberColor.Ink
        )

        Switch(
            checked = isOnline,
            onCheckedChange = { onToggleOnline() },
            modifier = Modifier.align(Alignment.CenterEnd),
            colors = SwitchDefaults.colors(
                checkedThumbColor = AberColor.White,
                checkedTrackColor = AberColor.Orange,
                uncheckedThumbColor = AberColor.White,
                uncheckedTrackColor = Color(0xFFCCCCCC),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun OfflineBanner(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(), color = AberColor.Orange, contentColor = AberColor.Ink
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.15f), CircleShape)
                    .border(1.dp, Color.Black.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NightsStay,
                    contentDescription = null,
                    tint = AberColor.Ink,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "You are offline !",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AberColor.Ink
                )
                Text(
                    text = "Go online to start accepting jobs.",
                    fontSize = 13.sp,
                    color = AberColor.Ink.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeMap(
    isOnline: Boolean,
    currentLocation: LatLng,
    activeRequest: RideRequest?,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation.toGoogle(), 15f)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
    ) {
        if (!isOnline) {
            Circle(
                center = currentLocation.toGoogle(),
                radius = 300.0,
                fillColor = AberColor.Yellow.copy(alpha = 0.25f),
                strokeColor = Color.Transparent
            )
        }

        Marker(
            state = MarkerState(position = currentLocation.toGoogle()),
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
        )

        activeRequest?.let { request ->
            Marker(
                state = MarkerState(position = request.pickupLocation.toGoogle()),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            )
            Marker(
                state = MarkerState(position = request.dropoffLocation.toGoogle()),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)
            )
            Polyline(
                points = listOf(
                    request.pickupLocation.toGoogle(), request.dropoffLocation.toGoogle()
                ), color = Color(0xFF3858F6), width = 12f
            )
        }
    }
}

@Composable
fun DriverStatsCard(
    profile: DriverProfile, modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = AberColor.White,
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .background(AberColor.BorderGray.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(AberColor.SurfaceGray), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.name.take(1),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = AberColor.Ink
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = profile.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = AberColor.Ink
                        )
                        Text(
                            text = profile.level, fontSize = 13.sp, color = AberColor.BorderGray
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    // تم الاعتماد على totalEarned هنا
                    Text(
                        text = "$${String.format(Locale.US, "%.2f", profile.totalEarned)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = AberColor.Ink
                    )
                    Text(
                        text = "Earned", fontSize = 13.sp, color = AberColor.BorderGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = AberColor.Yellow
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    YellowStatItem(
                        icon = Icons.Default.AccessTime,
                        value = profile.hoursOnline.toString(),
                        label = "HOURS ONLINE"
                    )
                    YellowStatItem(
                        icon = Icons.Default.Speed,
                        value = "${profile.totalDistanceKm.toInt()} KM",
                        label = "TOTAL DISTANCE"
                    )
                    YellowStatItem(
                        icon = null,
                        value = profile.totalJobs.toString(),
                        label = "TOTAL JOBS"
                    )
                }
            }
        }
    }
}

@Composable
fun YellowStatItem(
    icon: ImageVector?, value: String, label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AberColor.Ink.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        } else {
            Spacer(modifier = Modifier.height(28.dp))
        }
        Text(
            text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AberColor.Ink
        )
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = AberColor.Ink.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun RideRequestCard(
    request: RideRequest, onAccept: () -> Unit, onIgnore: () -> Unit, modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = AberColor.White,
        shadowElevation = 16.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(AberColor.SurfaceGray), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = request.passengerName.take(1),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = AberColor.Ink
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = request.passengerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = AberColor.Ink
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            BadgeChip(text = "ApplePay")
                            BadgeChip(text = "Discount")
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${String.format(Locale.US, "%.2f", request.fare)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = AberColor.Ink
                    )
                    Text(
                        text = request.distanceText, fontSize = 13.sp, color = AberColor.BorderGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = AberColor.SurfaceGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "PICK UP",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AberColor.BorderGray,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = request.pickupAddress,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AberColor.Ink
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = AberColor.SurfaceGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "DROP OFF",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AberColor.BorderGray,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = request.dropoffAddress,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AberColor.Ink
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onIgnore, modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Ignore",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AberColor.BorderGray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onAccept,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AberColor.Yellow, contentColor = AberColor.Ink
                    )
                ) {
                    Text(
                        text = "Accept", fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun BadgeChip(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp), color = AberColor.Yellow
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = AberColor.Ink,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

// تم حذف الامتداد totalEarnings لأنه أصبح موجوداً بشكل رسمي تحت اسم totalEarned
val RideRequest.distanceText: String get() = "2.2 km"

@Preview(showBackground = true)
@Composable
fun HomeScreenOfflinePreview() {
    HomeScreen(
        uiState = HomeUiState(isOnline = false),
        onToggleOnline = {},
        onAcceptRequest = {},
        onIgnoreRequest = {},
        onMenuClick = {},
    )
}
