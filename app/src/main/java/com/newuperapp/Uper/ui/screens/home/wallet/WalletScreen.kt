package com.newuperapp.Uper.ui.screens.home.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.R
import com.newuperapp.Uper.domain.model.WalletTransaction
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/**
 * Main Wallet screen showing current earnings, payment methods, and transaction history.
 *
 * @param onBackClick Callback for the menu/back icon.
 * @param onPaymentMethodClick Callback to navigate to payment method management.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    onBackClick: () -> Unit,
    onPaymentMethodClick: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    WalletScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onPaymentMethodClick = onPaymentMethodClick,
        selectedTab = selectedTab,
        onTabClick = { selectedTab = it }
    )
}


/**
 * Styled tab button for the wallet sub-navigation.
 */
@Composable
private fun TabButton(text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        color = if (isSelected) AberColor.Ink else Color.Transparent
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

/**
 * List item representing a single transaction in the wallet history.
 */
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

@Preview(showBackground = true)
@Composable
private fun WalletScreenPreview() {
    WalletScreen(
        uiState = WalletUiState(
            balance = 325.0,
            transactions = listOf(
                WalletTransaction("1", "Ali Ahmed", "#740136", 25.0),
                WalletTransaction("2", "Sarah Khaled", "#539642", 12.0)
            ),
            isLoading = false
        ),
        onBackClick = {},
        onPaymentMethodClick = {},
        selectedTab = 0,
        onTabClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    uiState: WalletUiState,
    onBackClick: () -> Unit,
    onPaymentMethodClick: () -> Unit,
    selectedTab: Int,
    onTabClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.wallet_title),
                        style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)
                    ) 
                },
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
            // Cash / Discount Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AberColor.Yellow)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .border(1.dp, AberColor.Ink, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                TabButton(stringResource(R.string.wallet_cash), selectedTab == 0, modifier = Modifier.weight(1f)) { onTabClick(0) }
                TabButton(stringResource(R.string.wallet_discount), selectedTab == 1, modifier = Modifier.weight(1f)) { onTabClick(1) }
            }

            // Earnings Summary
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AberColor.Yellow)
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (uiState.isLoading) "..." else "$${uiState.balance}",
                    style = AberTypography.ScreenTitle.copy(fontSize = 44.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(R.string.wallet_total_earn),
                    style = AberTypography.SectionLabel.copy(fontSize = 14.sp, color = AberColor.Ink.copy(alpha = 0.5f))
                )
            }

            // Quick access to Payment Methods
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
                    Text(
                        text = stringResource(R.string.wallet_payment_method),
                        style = AberTypography.CardTitle.copy(fontSize = 20.sp),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AberColor.BorderGray)
                }
            }

            // Recent Transactions
            Text(
                text = stringResource(R.string.wallet_payment_history),
                style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray, fontSize = 14.sp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color.White)
            ) {
                if (uiState.isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = AberColor.Yellow)
                        }
                    }
                } else {
                    items(uiState.transactions) { transaction ->
                        TransactionItem(transaction)
                        HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(horizontal = 20.dp))
                    }
                }
            }
        }
    }
}

