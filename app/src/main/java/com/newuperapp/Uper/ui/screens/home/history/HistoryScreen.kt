package com.newuperapp.Uper.ui.screens.home.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.model.HistoryItem
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("History", style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Back", tint = AberColor.Yellow)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AberColor.White
                )
            )
        },
        containerColor = AberColor.SurfaceGrayAlt
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            CalendarHeader()
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.DirectionsCar,
                    label = "Total Jobs",
                    value = "10",
                    containerColor = AberColor.Yellow
                )
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Stars,
                    label = "Earned",
                    value = "$325.00",
                    containerColor = AberColor.Orange
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // Placeholder items
                items(3) {
                    HistoryItemCard()
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun CalendarHeader() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(AberColor.White)
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val days = listOf("Mon\n11", "Tue\n12", "Wed\n13", "Thu\n14", "Fri\n15", "Sat\n16")
        items(days) { day ->
            val isSelected = day.contains("16")
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) AberColor.White else AberColor.SurfaceGray)
                    .then(if (isSelected) Modifier.background(AberColor.White, RoundedCornerShape(12.dp)).padding(2.dp).background(AberColor.White, RoundedCornerShape(12.dp)).padding(1.dp) else Modifier)
                    .then(if (isSelected) Modifier.background(AberColor.White).padding(1.dp).background(AberColor.White).padding(1.dp) else Modifier),
                contentAlignment = Alignment.Center
            ) {
                // Simplified border for preview
                Text(
                    text = day,
                    textAlign = TextAlign.Center,
                    style = AberTypography.Caption.copy(
                        color = if (isSelected) AberColor.Orange else AberColor.BorderGray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        lineHeight = 20.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    containerColor: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = modifier
            .height(90.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(40.dp), tint = AberColor.Ink)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(label, style = AberTypography.Caption.copy(color = AberColor.Ink.copy(alpha = 0.6f), fontSize = 12.sp))
                Text(value, style = AberTypography.CardTitle.copy(fontSize = 20.sp))
            }
        }
    }
}

@Composable
private fun HistoryItemCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AberColor.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)).background(AberColor.SurfaceGray))
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Steve Bowen", style = AberTypography.CardTitle.copy(fontSize = 16.sp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Tag("ApplePay")
                        Tag("Discount")
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("$25.00", style = AberTypography.PriceTag.copy(fontSize = 16.sp))
                    Text("2.2 km", style = AberTypography.Caption)
                }
            }
            
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = AberColor.SurfaceGray)
            Spacer(Modifier.height(16.dp))
            
            AddressBlock(label = "PICK UP", address = "7958 Swift Village")
            Spacer(Modifier.height(16.dp))
            AddressBlock(label = "DROP OFF", address = "105 William St, Chicago, US")
        }
    }
}

@Composable
private fun Tag(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AberColor.Yellow)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, style = AberTypography.Caption.copy(color = AberColor.Ink, fontWeight = FontWeight.Bold, fontSize = 10.sp))
    }
}

@Composable
private fun AddressBlock(label: String, address: String) {
    Column {
        Text(label, style = AberTypography.SectionLabel.copy(fontSize = 10.sp))
        Text(address, style = AberTypography.Subtitle.copy(fontSize = 15.sp))
    }
}
