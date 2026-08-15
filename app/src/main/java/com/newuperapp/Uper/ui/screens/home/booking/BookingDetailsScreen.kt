package com.newuperapp.Uper.ui.screens.home.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.R
import com.newuperapp.Uper.domain.model.BookingDetails
import com.newuperapp.Uper.domain.model.FareLine
import com.newuperapp.Uper.domain.model.LatLngPoint
import com.newuperapp.Uper.domain.model.RidePaymentTag
import com.newuperapp.Uper.domain.model.RideRequest
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonStyle
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/**
 * Detailed view of a confirmed booking, providing rider contact and navigation actions.
 */
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
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
                    CircularProgressIndicator(color = AberColor.Yellow)
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
                    RiderAvatar(
                        avatarUrl = details.request.riderAvatarUrl,
                        riderName = details.request.riderName
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
                        Text(
                            "${details.request.distanceKm} km",
                            style = AberTypography.Caption.copy(color = AberColor.Ink.copy(alpha = 0.7f))
                        )
                    }
                }

                AddressBlock(
                    stringResource(R.string.booking_pick_up_label), details.request.pickupAddress
                )
                HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.3f))
                AddressBlock(
                    stringResource(R.string.booking_drop_off_label), details.request.dropoffAddress
                )
                HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.3f))

                // Passenger Note
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text(
                        stringResource(R.string.booking_noted_label),
                        style = AberTypography.SectionLabel.copy(color = AberColor.Ink)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        details.note,
                        style = AberTypography.semibody17(),
                        modifier = Modifier.widthIn(max = 600.dp)
                    )
                }

                HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.4f), thickness = 1.dp)

                // Fare Breakdown
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text(
                        stringResource(R.string.booking_trip_fare_label),
                        style = AberTypography.SectionLabel.copy(color = AberColor.Ink)
                    )
                    Spacer(Modifier.height(10.dp))
                    details.fareBreakdown.forEach { line ->
                        FareRow(line.label, line.amount)
                        Spacer(Modifier.height(10.dp))
                    }
                    FareRow(
                        stringResource(R.string.booking_paid_amount),
                        details.paidAmount,
                        emphasize = true
                    )
                }

                HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.4f), thickness = 1.dp)

                // Contact Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionTile(
                        label = stringResource(R.string.booking_call),
                        icon = Icons.Default.Call,
                        background = Color(0xFF3DD9A6),
                        contentColor = AberColor.Ink,
                        onClick = onCallClick,
                        modifier = Modifier.weight(1f)
                    )
                    ActionTile(
                        label = stringResource(R.string.booking_message),
                        icon = Icons.AutoMirrored.Filled.Message,
                        background = Color(0xFF4C5FF0),
                        contentColor = Color.White,
                        onClick = onMessageClick,
                        modifier = Modifier.weight(1f)
                    )
                    ActionTile(
                        label = stringResource(R.string.booking_cancel),
                        icon = Icons.Default.DeleteOutline,
                        background = AberColor.BorderGray,
                        contentColor = AberColor.Ink,
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f),
                        isLoading = uiState.isCancelling
                    )
                }
            }

            // Primary Navigation CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AberColor.Yellow)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                AberButton(
                    text = stringResource(R.string.booking_go_to_pick_up),
                    onClick = onGoToPickupClick,
                    style = AberButtonStyle.Primary,
                    modifier = Modifier.widthIn(max = 320.dp)
                )
            }
        }
    }
}

/**
 * Rider avatar circle. Loads the real photo when a URL is available (per design),
 * falling back to a neutral placeholder circle otherwise.
 */
@Composable
private fun RiderAvatar(avatarUrl: String?, riderName: String) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(AberColor.BorderGray)
            .border(1.dp, AberColor.Ink.copy(alpha = 0.1f), CircleShape)
    ) {
        if (!avatarUrl.isNullOrBlank()) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = riderName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
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
    // Design shows a solid yellow pill with dark ink text — previously the Box used
    // TagBackground while the Text style separately (and redundantly/incorrectly)
    // set its own background, which fought with the Box color instead of matching it.
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(AberColor.Yellow)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            label, style = AberTypography.Caption.copy(
                color = AberColor.Ink, fontWeight = FontWeight.SemiBold
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
        Text(
            label.uppercase(),
            style = AberTypography.SectionLabel.copy(color = AberColor.Ink)
        )
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
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier
            .widthIn(max = 120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .semantics { role = Role.Button }
            .clickable(enabled = !isLoading, onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = contentColor, modifier = Modifier.size(22.dp), strokeWidth = 2.dp
            )
        } else {
            Icon(icon, contentDescription = label, tint = contentColor)
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = label, style = AberTypography.semibody17(), color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookingDetailsScreenPreview() {
    BookingDetailsScreen(
        uiState = BookingDetailsUiState(
            details = BookingDetails(
                bookingId = "123456",
                request = RideRequest(
                    id = "ride_1",
                    riderName = "Esther Berry",
                    riderAvatarUrl = "https://i.pravatar.cc/150?img=47",
                    price = 25.0,
                    distanceKm = 2.2,
                    tags = listOf(RidePaymentTag.APPLE_PAY, RidePaymentTag.DISCOUNT),
                    pickupAddress = "7958 Swift Village",
                    pickupLocation = LatLngPoint(60.1719, 24.9350),
                    dropoffAddress = "105 William St, Chicago, US",
                    dropoffLocation = LatLngPoint(60.1750, 24.9410),
                ),
                riderPhone = "+1 234 567 890",
                note = "Lorem ipsum dolor sit amet, consectetur adipisc elit. Nullam ac vestibulum erat. Cras vulputate auctor lectus at consequat.",
                fareBreakdown = listOf(
                    FareLine("Apple Pay", 15.0),
                    FareLine("Discount", 10.0),
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