package com.newuperapp.uper.ui.screens.home.history

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.newuperapp.uper.R
import com.newuperapp.uper.domain.model.HistoryItem
import com.newuperapp.uper.domain.model.RidePaymentTag
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

/**
 * Driver's trip history screen, displaying a day picker, cumulative stats,
 * and a list of past jobs with rider avatar, payment/discount tags,
 * price, distance and pickup/drop-off addresses.
 *
 * @param onBackClick Navigation callback (hamburger menu / drawer toggle).
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit, viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HistoryScreen(
        uiState = uiState, onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    uiState: HistoryUiState, onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                Text(
                    text = stringResource(R.string.history_title),
                    style = AberTypography.ScreenTitle.copy(
                        fontSize = 22.sp, fontWeight = FontWeight.Bold
                    )
                )
            },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = AberColor.Yellow
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AberColor.White)
            )
        }, containerColor = AberColor.SurfaceGrayAlt
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                DayStrip(
                    days = uiState.days,
                    selectedDay = uiState.selectedDay,
                    onDaySelected = uiState.onDaySelected
                )
                HorizontalDivider(color = AberColor.SurfaceGray)
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Total Jobs card comes first, solid yellow background
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.DirectionsCar,
                        value = uiState.totalJobs.toString(),
                        label = stringResource(R.string.history_total_jobs),
                        backgroundColor = AberColor.Yellow
                    )
                    // Earned card second, solid orange background
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Loyalty,
                        value = "$${"%.2f".format(uiState.totalEarned)}",
                        label = stringResource(R.string.history_total_earned),
                        backgroundColor = AberColor.Orange
                    )
                }
            }

            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AberColor.Yellow)
                    }
                }
            } else if (uiState.errorMessage != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = uiState.errorMessage, color = AberColor.Orange)
                    }
                }
            } else {
                items(uiState.trips.size) { index ->
                    HistoryItemCard(uiState.trips[index])
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * Horizontally scrollable strip of days (e.g. Mon 11 ... Sat 16).
 * The selected day is highlighted with an orange rounded border.
 */
@Composable
private fun DayStrip(
    days: List<DayItem>, selectedDay: DayItem?, onDaySelected: (DayItem) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(AberColor.White)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(days) { day ->
            val isSelected = day == selectedDay
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSelected) Color.Transparent else AberColor.SurfaceGrayAlt)
                    .then(
                        if (isSelected) Modifier.border(
                            width = 1.5.dp,
                            color = AberColor.Orange,
                            shape = RoundedCornerShape(14.dp)
                        ) else Modifier
                    )
                    .padding(vertical = 12.dp)
                    .clickable(day, onDaySelected)
            ) {
                Text(
                    text = day.dayName, style = AberTypography.Caption.copy(
                        color = if (isSelected) AberColor.Orange else AberColor.BorderGray
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = day.dayNumber, style = AberTypography.CardTitle.copy(
                        fontSize = 18.sp,
                        color = if (isSelected) AberColor.Orange else AberColor.BorderGray
                    )
                )
            }
        }
    }
}

@SuppressLint("SuspiciousModifierThen")
private fun Modifier.clickable(day: DayItem, onDaySelected: (DayItem) -> Unit): Modifier =
    this.then(
        clickable { onDaySelected(day) })

/**
 * Data representing a single day in the day strip.
 * [fullDate] (ISO format, e.g. "2026-08-16") is used to filter trips
 * and to determine which day is selected; [dayName]/[dayNumber] are
 * the short labels rendered in the strip (e.g. "Sat" / "16").
 */
data class DayItem(
    val dayName: String, val dayNumber: String, val fullDate: String
)

/**
 * Reusable stat card for the Total Jobs / Earned summary.
 * Solid colored background (yellow / orange) with a leading icon,
 * label above the value.
 */
@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    backgroundColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = AberColor.Ink)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    style = AberTypography.Caption.copy(color = AberColor.Ink.copy(alpha = 0.7f))
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = value, style = AberTypography.CardTitle.copy(
                        fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AberColor.Ink
                    )
                )
            }
        }
    }
}

/**
 * List item representing a single completed trip: rider avatar photo,
 * name, payment/discount tags, price, distance and pickup/drop-off
 * address blocks stacked vertically.
 */
@Composable
private fun HistoryItemCard(item: HistoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AberColor.SurfaceGrayAlt)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.riderAvatarUrl,
                    contentDescription = item.riderName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(AberColor.SurfaceGray)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.riderName, style = AberTypography.CardTitle.copy(
                            fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )
                    )
                    if (item.paymentTags.isNotEmpty()) {
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item.paymentTags.forEach { tag ->
                                TagChip(tag.label)
                            }
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${item.currencySymbol}${"%.2f".format(item.price)}",
                        style = AberTypography.PriceTag.copy(
                            fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${item.distanceKm} km",
                        style = AberTypography.Caption.copy(color = AberColor.BorderGray)
                    )
                }
            }

            // White inner panel with the pickup / drop-off details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AberColor.White)
                    .padding(16.dp)
            ) {
                AddressBlock(stringResource(R.string.booking_pick_up_label), item.pickupAddress)
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = AberColor.SurfaceGray)
                Spacer(Modifier.height(12.dp))
                AddressBlock(stringResource(R.string.booking_drop_off_label), item.dropoffAddress)
            }
        }
    }
}

/**
 * Small rounded pill used for payment method / discount tags
 * (e.g. "ApplePay", "Discount").
 */
@Composable
private fun TagChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(AberColor.Yellow)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text, style = AberTypography.Caption.copy(
                fontWeight = FontWeight.Bold, color = AberColor.Ink
            )
        )
    }
}

/**
 * Address block with the label (e.g. "PICK UP") stacked above the
 * address text, matching the vertical layout in the design.
 */
@Composable
private fun AddressBlock(label: String, address: String) {
    Column {
        Text(
            text = label.uppercase(), style = AberTypography.SectionLabel.copy(
                fontSize = 11.sp, color = AberColor.BorderGray
            )
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = address, style = AberTypography.CardTitle.copy(
                fontSize = 16.sp, color = AberColor.Ink
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    HistoryScreen(
        uiState = HistoryUiState(
            days = listOf(
                DayItem("Sun", "10", "2026-08-10"),
                DayItem("Mon", "11", "2026-08-11"),
                DayItem("Tue", "12", "2026-08-12"),
                DayItem("Wed", "13", "2026-08-13"),
                DayItem("Thu", "14", "2026-08-14"),
                DayItem("Fri", "15", "2026-08-15"),
                DayItem("Sat", "16", "2026-08-16")
            ), selectedDay = DayItem("Sat", "16", "2026-08-16"), trips = listOf(
                HistoryItem(
                    id = "1",
                    riderName = "Steve Bowen",
                    riderAvatarUrl = null,
                    price = 25.0,
                    currencySymbol = "$",
                    distanceKm = 2.2,
                    pickupAddress = "7958 Swift Village",
                    dropoffAddress = "105 William St, Chicago, US",
                    date = "2026-08-16",
                    paymentTags = listOf(RidePaymentTag.APPLE_PAY, RidePaymentTag.DISCOUNT)
                ), HistoryItem(
                    id = "2",
                    riderName = "Andre Clarke",
                    riderAvatarUrl = null,
                    price = 20.0,
                    currencySymbol = "$",
                    distanceKm = 1.8,
                    pickupAddress = "061 Will Terrace Apt. 812",
                    dropoffAddress = "7617 Hegmann Landing",
                    date = "2026-08-16",
                    paymentTags = listOf(RidePaymentTag.DISCOUNT)
                )
            ), totalEarned = 325.0, totalJobs = 10, isLoading = false
        ), onBackClick = {})
}