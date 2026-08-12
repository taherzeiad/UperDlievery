package com.newuperapp.Uper.ui.screens.home.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.model.BookingDetails
import com.newuperapp.Uper.domain.model.FareLine
import com.newuperapp.Uper.domain.model.LatLngPoint
import com.newuperapp.Uper.domain.model.RidePaymentTag
import com.newuperapp.Uper.domain.model.RideRequest
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonStyle
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@Composable
fun BookingDetailsRoute(
    viewModel: BookingDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToPickup: (rideId: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is BookingDetailsEvent.NavigateToPickupNavigation -> onNavigateToPickup(event.rideId)
                BookingDetailsEvent.NavigateBackAfterCancel -> onBackClick()
                is BookingDetailsEvent.LaunchDialer -> {
                    // Logic for dialer
                }

                is BookingDetailsEvent.LaunchMessenger -> {
                    // Logic for messenger
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
        onGoToPickupClick = viewModel::onGoToPickupClick
    )
}

@Composable
fun BookingDetailsScreen(
    uiState: BookingDetailsUiState,
    onBackClick: () -> Unit,
    onCallClick: () -> Unit,
    onMessageClick: () -> Unit,
    onCancelClick: () -> Unit,
    onGoToPickupClick: () -> Unit
) {
    val details = uiState.details

    Scaffold(containerColor = AberColor.SurfaceGrayAlt) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ---- Top bar ----
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
                        modifier = Modifier.size(26.dp)
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
                    CircularProgressIndicator(color = AberColor.Yellow)
                }
                return@Column
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                // ---- Rider summary ----
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AberColor.SurfaceGray)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
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
                        Text(details.request.riderName, style = AberTypography.CardTitle)
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            details.request.tags.forEach { tag -> TagPill(tag) }
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "${details.request.currencySymbol}${"%.2f".format(details.request.price)}",
                            style = AberTypography.PriceTag
                        )
                        Text("${details.request.distanceKm} km", style = AberTypography.Caption)
                    }
                }

                AddressBlock("Pick up", details.request.pickupAddress)
                HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.3f))
                AddressBlock("Drop off", details.request.dropoffAddress)
                HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.3f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text("NOTED", style = AberTypography.SectionLabel)
                    Spacer(Modifier.height(8.dp))
                    Text(details.note, style = AberTypography.semibody17())
                }

                HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.4f), thickness = 1.dp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text("TRIP FARE", style = AberTypography.SectionLabel)
                    Spacer(Modifier.height(10.dp))
                    details.fareBreakdown.forEach { line ->
                        FareRow(line.label, line.amount)
                        Spacer(Modifier.height(10.dp))
                    }
                    FareRow("Paid amount", details.paidAmount, emphasize = true)
                }

                HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.4f), thickness = 1.dp)

                // ---- Call / Message / Cancel ----
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionTile(
                        label = "Call",
                        icon = Icons.Default.Call,
                        background = Color(0xFF3DD9A6),
                        onClick = onCallClick,
                        modifier = Modifier.weight(1f)
                    )
                    ActionTile(
                        label = "Message",
                        icon = Icons.AutoMirrored.Filled.Message,
                        background = Color(0xFF4C5FF0),
                        onClick = onMessageClick,
                        modifier = Modifier.weight(1f)
                    )
                    ActionTile(
                        label = "Cancel",
                        icon = Icons.Default.DeleteOutline,
                        background = AberColor.BorderGray,
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f),
                        isLoading = uiState.isCancelling
                    )
                }
            }

            // ---- Go to pick up ----
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AberColor.Yellow)
                    .padding(20.dp)
            ) {
                AberButton(
                    text = "Go to pick up",
                    onClick = onGoToPickupClick,
                    style = AberButtonStyle.Primary
                )
            }
        }
    }
}

@Composable
private fun TagPill(tag: RidePaymentTag) {
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
            .background(AberColor.White)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(label.uppercase(), style = AberTypography.SectionLabel)
        Spacer(Modifier.height(6.dp))
        Text(address, style = AberTypography.semibody17())
    }
}

@Composable
private fun FareRow(label: String, amount: Double, emphasize: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = AberTypography.semibody17())
        Text(
            "$${"%.2f".format(amount)}",
            style = if (emphasize) AberTypography.PriceTag else AberTypography.semibody17()
        )
    }
}

@Composable
private fun ActionTile(
    label: String,
    icon: ImageVector,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .clickable(enabled = !isLoading, onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp
            )
        } else {
            Icon(icon, contentDescription = label, tint = Color.White)
        }
        Spacer(Modifier.height(6.dp))
        Text(label, style = AberTypography.semibody17(Color.White))
    }
}

@Preview(showBackground = true)
@Composable
private fun BookingDetailsScreenPreview() {
    BookingDetailsScreen(
        uiState = BookingDetailsUiState(
            details = BookingDetails(
                bookingId = "6857",
                request = RideRequest(
                    id = "ride_1",
                    riderName = "Esther Berry",
                    riderAvatarUrl = null,
                    price = 25.0,
                    distanceKm = 2.2,
                    tags = listOf(RidePaymentTag.APPLE_PAY, RidePaymentTag.DISCOUNT),
                    pickupAddress = "7958 Swift Village",
                    pickupLocation = LatLngPoint(60.1719, 24.9350),
                    dropoffAddress = "105 William St, Chicago, US",
                    dropoffLocation = LatLngPoint(60.1750, 24.9410),
                ),
                riderPhone = "+1 234 567 890",
                note = "Please wait for 5 minutes, I'm coming down.",
                fareBreakdown = listOf(
                    FareLine("Trip fare", 20.0),
                    FareLine("Service fee", 3.0),
                    FareLine("Tax", 2.0),
                ),
                paidAmount = 25.0,
            ),
            isLoading = false,
        ),
        onBackClick = {},
        onCallClick = {},
        onMessageClick = {},
        onCancelClick = {},
        onGoToPickupClick = {},
    )
}
