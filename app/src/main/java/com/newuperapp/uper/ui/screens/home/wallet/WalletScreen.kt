package com.newuperapp.uper.ui.screens.home.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.uper.R
import com.newuperapp.uper.domain.model.WalletTransaction
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

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
        onTabClick = { selectedTab = it })
}

/**
 * Styled tab button for Cash / Discount toggle.
 */
@Composable
private fun TabButton(
    text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(if (isSelected) AberColor.Ink else Color.Transparent)
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, style = AberTypography.Subtitle.copy(
                color = if (isSelected) AberColor.Yellow else AberColor.Ink,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        )
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
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder or user image
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(AberColor.SurfaceGray)
        ) {
            // If transaction avatar/image is present, load it here
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.name, style = AberTypography.CardTitle.copy(
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AberColor.Ink
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = transaction.transactionNumber, style = AberTypography.Caption.copy(
                    fontSize = 14.sp, color = Color.LightGray
                )
            )
        }

        Text(
            text = "${transaction.currencySymbol}${"%.2f".format(transaction.amount)}",
            style = AberTypography.PriceTag.copy(
                fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AberColor.Ink
            )
        )
    }
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
                        style = AberTypography.ScreenTitle.copy(
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AberColor.Ink
                        )
                    )
                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AberColor.Yellow
                )
            )
        }, containerColor = AberColor.SurfaceGrayAlt
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Yellow Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AberColor.Yellow)
                    .padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Cash / Discount Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(1.dp, AberColor.Ink, RoundedCornerShape(6.dp))
                        .clip(RoundedCornerShape(6.dp))
                ) {
                    TabButton(
                        text = stringResource(R.string.wallet_cash),
                        isSelected = selectedTab == 0,
                        modifier = Modifier.weight(1f),
                        onClick = { onTabClick(0) })
                    TabButton(
                        text = stringResource(R.string.wallet_discount),
                        isSelected = selectedTab == 1,
                        modifier = Modifier.weight(1f),
                        onClick = { onTabClick(1) })
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Earnings Amount Display
                Text(
                    text = if (uiState.isLoading) "..." else "$${"%.2f".format(uiState.balance)}",
                    style = AberTypography.ScreenTitle.copy(
                        fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = AberColor.Ink
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.wallet_total_earn).uppercase(),
                    style = AberTypography.SectionLabel.copy(
                        fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AberColor.Ink
                    )
                )

                Spacer(modifier = Modifier.height(90.dp)) // Extra padding for overlay card
            }

            // Main Content Area with Overlay Card
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-40).dp)
                    .padding(horizontal = 16.dp)
            ) {
                // Payment Method Quick Access Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onPaymentMethodClick),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(AberColor.Yellow), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Text(
                            text = stringResource(R.string.wallet_payment_method),
                            style = AberTypography.CardTitle.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AberColor.Ink
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Payment History Title
                Text(
                    text = stringResource(R.string.wallet_payment_history).uppercase(),
                    style = AberTypography.SectionLabel.copy(
                        color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                // Transactions List Container
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AberColor.Yellow)
                        }
                    } else if (uiState.errorMessage != null) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Text(text = uiState.errorMessage, color = AberColor.Orange)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            itemsIndexed(uiState.transactions) { index, transaction ->
                                TransactionItem(transaction)
                                if (index < uiState.transactions.lastIndex) {
                                    HorizontalDivider(
                                        color = AberColor.SurfaceGray,
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WalletScreenPreview() {
    WalletScreen(
        uiState = WalletUiState(
        balance = 325.0, transactions = listOf(
            WalletTransaction("1", "Elva Barnett", "#740136", 25.0),
            WalletTransaction("2", "Isaiah Francis", "#539642", 12.0),
            WalletTransaction("3", "Lula Briggs", "#123146", 34.0),
            WalletTransaction("4", "Ray Young", "#521936", 33.0),
            WalletTransaction("5", "Betty Palmer", "#129936", 15.0)
        ), isLoading = false
    ), onBackClick = {}, onPaymentMethodClick = {}, selectedTab = 0, onTabClick = {})
}