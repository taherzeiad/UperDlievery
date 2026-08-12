package com.newuperapp.Uper.ui.screens.home.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.R
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/**
 * Driver's trip history screen, displaying cumulative stats and a list of past jobs.
 *
 * @param onBackClick Navigation callback to return to the previous screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.history_title), 
                        style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back", 
                            tint = AberColor.Yellow
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AberColor.White)
            )
        },
        containerColor = AberColor.SurfaceGrayAlt
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                CalendarHeader()
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.MonetizationOn,
                        value = "$250.00",
                        label = stringResource(R.string.history_total_earned),
                        color = Color(0xFF3858F6)
                    )
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.History,
                        value = "12",
                        label = stringResource(R.string.history_total_jobs),
                        color = AberColor.Orange
                    )
                }
            }

            item {
                Text(
                    text = stringResource(R.string.history_trips).uppercase(),
                    style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray, fontSize = 14.sp),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            items(5) {
                HistoryItemCard()
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

/**
 * Filter header for selecting the history date range.
 */
@Composable
private fun CalendarHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AberColor.White)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = AberColor.Yellow)
        Spacer(Modifier.width(12.dp))
        Text(text = "August 2026", style = AberTypography.Subtitle.copy(fontWeight = FontWeight.Bold))
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AberColor.BorderGray)
    }
}

/**
 * Reusable stat card for earnings or job count summary.
 */
@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(Modifier.height(12.dp))
            Text(text = value, style = AberTypography.CardTitle.copy(fontSize = 20.sp))
            Text(text = label, style = AberTypography.Caption)
        }
    }
}

/**
 * List item representing a single completed trip.
 */
@Composable
private fun HistoryItemCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(AberColor.SurfaceGray))
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Steve Bowen", style = AberTypography.CardTitle.copy(fontSize = 16.sp))
                    Text(text = "Apple Pay", style = AberTypography.Caption)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "$25.00", style = AberTypography.PriceTag.copy(fontSize = 16.sp))
                    Text(text = "2.2 km", style = AberTypography.Caption)
                }
            }
            
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = AberColor.SurfaceGray)
            Spacer(Modifier.height(16.dp))
            
            AddressBlock(stringResource(R.string.booking_pick_up_label), "7958 Swift Village")
            Spacer(Modifier.height(12.dp))
            AddressBlock(stringResource(R.string.booking_drop_off_label), "105 William St, Chicago, US")
        }
    }
}

@Composable
private fun AddressBlock(label: String, address: String) {
    Row {
        Text(
            text = label, 
            style = AberTypography.SectionLabel.copy(fontSize = 10.sp), 
            modifier = Modifier.width(60.dp)
        )
        Text(text = address, style = AberTypography.Caption.copy(color = AberColor.Ink))
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    HistoryScreen(onBackClick = {})
}
