package com.newuperapp.Uper.ui.screens.home.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.model.WalletTransaction
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    onBackClick: () -> Unit,
    onPaymentMethodClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Wallet", style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Back", tint = AberColor.Ink)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AberColor.Yellow)
            )
        },
        containerColor = AberColor.SurfaceGrayAlt
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AberColor.Yellow)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                TabButton("Cash", selectedTab == 0, modifier = Modifier.weight(1f)) { selectedTab = 0 }
                TabButton("Discount", selectedTab == 1, modifier = Modifier.weight(1f)) { selectedTab = 1 }
            }

            // Total Earned Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AberColor.Yellow)
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$325.00", style = AberTypography.ScreenTitle.copy(fontSize = 48.sp))
                Text("TOTAL EARN", style = AberTypography.SectionLabel.copy(fontSize = 14.sp))
            }

            // Payment Method Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                onClick = onPaymentMethodClick
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(AberColor.Yellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color.White)
                    }
                    Spacer(Modifier.width(16.dp))
                    Text("Payment method", style = AberTypography.CardTitle.copy(fontSize = 20.sp), modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AberColor.BorderGray)
                }
            }

            // Payment History
            Text(
                "PAYMENT HISTORY",
                style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray, fontSize = 14.sp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            val transactions = listOf(
                WalletTransaction("1", "Elva Barnett", "#740136", 25.0),
                WalletTransaction("2", "Isaiah Francis", "#539642", 12.0),
                WalletTransaction("3", "Lula Briggs", "#123146", 34.0),
                WalletTransaction("4", "Ray Young", "#521936", 33.0),
                WalletTransaction("5", "Betty Palmer", "#129936", 15.0)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color.White)
            ) {
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(horizontal = 20.dp))
                }
            }
        }
    }
}

@Composable
private fun TabButton(text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        color = if (isSelected) AberColor.Ink else Color.Transparent,
        shape = if (text == "Cash") RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp) else RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, AberColor.Ink) else null
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = AberTypography.Subtitle.copy(
                    color = if (isSelected) AberColor.Yellow else AberColor.Ink,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun TransactionItem(transaction: WalletTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(AberColor.SurfaceGray))
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.name, style = AberTypography.CardTitle.copy(fontSize = 17.sp))
            Text(transaction.transactionNumber, style = AberTypography.Caption)
        }
        Text("${transaction.currencySymbol}${"%.2f".format(transaction.amount)}", style = AberTypography.PriceTag.copy(fontSize = 17.sp))
    }
}
