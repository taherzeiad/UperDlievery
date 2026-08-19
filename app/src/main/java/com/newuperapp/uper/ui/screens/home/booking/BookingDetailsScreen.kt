package com.newuperapp.uper.ui.screens.home.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.newuperapp.uper.domain.model.*
import com.newuperapp.uper.ui.components.AberButton
import com.newuperapp.uper.ui.components.AberButtonStyle
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage

@Composable
fun BookingDetailsRoute(
    onBackClick: () -> Unit,
    onNavigateToPickup: (rideId: String) -> Unit,
    viewModel: BookingDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.events, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is BookingDetailsEvent.NavigateToPickupNavigation -> onNavigateToPickup(event.rideId)
                    BookingDetailsEvent.NavigateBackAfterCancel -> onBackClick()
                    is BookingDetailsEvent.LaunchDialer -> { /* تنفيذ Intent الاتصال الهاتفي */
                    }
                    is BookingDetailsEvent.LaunchMessenger -> { /* تنفيذ Intent تطبيق الرسائل */
                    }
                    is BookingDetailsEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    BookingDetailsScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onCallClick = viewModel::onCallClick,
        onMessageClick = viewModel::onMessageClick,
        onCancelClick = viewModel::onCancelClick,
        onGoToPickupClick = viewModel::onGoToPickupClick,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun BookingDetailsScreen(
    uiState: BookingDetailsUiState,
    onBackClick: () -> Unit,
    onCallClick: () -> Unit,
    onMessageClick: () -> Unit,
    onCancelClick: () -> Unit,
    onGoToPickupClick: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val details = uiState.details

    Scaffold(
        containerColor = AberColor.SurfaceGrayAlt,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Top Bar with Booking ID
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AberColor.White)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AberColor.Yellow,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = if (details != null) "#${details.bookingId}" else "",
                    style = AberTypography.CardTitle.copy(fontSize = 20.sp),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(48.dp))
            }
            HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.3f))

            if (details == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (uiState.errorMessage != null) {
                        Text(uiState.errorMessage, color = AberColor.Orange)
                    } else {
                        CircularProgressIndicator(color = AberColor.Yellow)
                    }
                }
                return@Column
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Rider Summary Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AberColor.SurfaceGray)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = details.request.riderAvatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(AberColor.SurfaceGrayAlt)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = details.request.riderName,
                            style = AberTypography.CardTitle.copy(fontSize = 18.sp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "4.8 Rating", // Placeholder
                                style = AberTypography.Caption
                            )
                        }
                    }

                    // Call & Message Buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = onMessageClick,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(AberColor.White)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = "Chat", tint = Color.Black)
                        }
                        IconButton(
                            onClick = onCallClick,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(AberColor.Yellow)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = Color.Black)
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Addresses
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AberColor.White)
                        .padding(20.dp)
                ) {
                    AddressItem(
                        dotColor = AberColor.Yellow,
                        address = details.request.pickupAddress,
                        label = "PICKUP"
                    )
                    Spacer(Modifier.height(16.dp))
                    AddressItem(
                        dotColor = AberColor.Orange,
                        address = details.request.dropoffAddress,
                        label = "DROP OFF"
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Note
                if (details.note.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AberColor.White)
                            .padding(20.dp)
                    ) {
                        Text(text = "NOTE", style = AberTypography.Caption, color = Color.Gray)
                        Spacer(Modifier.height(4.dp))
                        Text(text = details.note, style = AberTypography.Subtitle)
                    }
                    Spacer(Modifier.height(8.dp))
                }

                // Fare Breakdown
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AberColor.White)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "FARE BREAKDOWN",
                        style = AberTypography.Caption,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(12.dp))
                    details.fareBreakdown.forEach { line ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = line.label,
                                style = AberTypography.Subtitle,
                                color = Color.Gray,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${details.request.currencySymbol}${line.amount}",
                                style = AberTypography.Subtitle,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = AberColor.BorderGray.copy(alpha = 0.3f)
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "PAID AMOUNT",
                            style = AberTypography.Subtitle,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${details.request.currencySymbol}${details.paidAmount}",
                            style = AberTypography.HeroTitle.copy(
                                fontSize = 18.sp,
                                color = AberColor.Orange
                            ),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Bottom Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AberColor.White)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                AberButton(
                    text = "GO TO PICKUP",
                    onClick = onGoToPickupClick,
                    style = AberButtonStyle.Dark
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "CANCEL BOOKING",
                    style = AberTypography.Subtitle.copy(
                        color = AberColor.Orange,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onCancelClick)
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        if (uiState.isCancelling) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AberColor.Yellow)
            }
        }
    }
}

@Composable
private fun AddressItem(dotColor: Color, address: String, label: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(text = label, style = AberTypography.Caption, color = Color.Gray)
            Text(text = address, style = AberTypography.Subtitle, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview
@Composable
private fun BookingDetailsPreview() {
    BookingDetailsScreen(
        uiState = BookingDetailsUiState(
            details = BookingDetails(
                bookingId = "BK-9922",
                request = RideRequest(
                    id = "1",
                    riderName = "Emma Watson",
                    riderAvatarUrl = null,
                    price = 45.0,
                    currencySymbol = "$",
                    distanceKm = 3.5,
                    tags = listOf(RidePaymentTag.CASH),
                    pickupAddress = "Green Garden Apt 4",
                    pickupLocation = LatLngPoint(0.0, 0.0),
                    dropoffAddress = "West Park Mall",
                    dropoffLocation = LatLngPoint(0.0, 0.0)
                ),
                riderPhone = "+1 555 123 4567",
                note = "Please call when you arrive.",
                fareBreakdown = listOf(FareLine("Base Fare", 40.0), FareLine("Discount", -5.0)),
                paidAmount = 45.0
            ),
            isLoading = false
        ),
        onBackClick = {},
        onCallClick = {},
        onMessageClick = {},
        onCancelClick = {},
        onGoToPickupClick = {},
        snackbarHostState = remember { SnackbarHostState() }
    )
}
