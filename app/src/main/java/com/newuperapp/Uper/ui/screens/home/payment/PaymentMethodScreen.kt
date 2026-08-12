package com.newuperapp.Uper.ui.screens.home.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.R
import com.newuperapp.Uper.domain.model.PaymentMethod
import com.newuperapp.Uper.domain.model.PaymentMethodType
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/**
 * Screen for managing driver payment methods and adding new cards.
 *
 * @param onBackClick Callback for the back navigation action.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.payment_method_title),
                        style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AberColor.Yellow)
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
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    onClick = { /* Implement add card action */ }
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
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color.White)
                        }
                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = stringResource(R.string.payment_add_new_card),
                            style = AberTypography.CardTitle.copy(fontSize = 18.sp),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AberColor.BorderGray)
                    }
                }
            }

            item {
                Text(
                    text = stringResource(R.string.payment_credit_cards_label),
                    style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray, fontSize = 14.sp),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            val cards = listOf(
                PaymentMethod("1", PaymentMethodType.VISA, "**** **** **** 3765"),
                PaymentMethod("2", PaymentMethodType.PAYPAL, "pfeffer_ellen@balistreri.net"),
                PaymentMethod("3", PaymentMethodType.MASTERCARD, "**** **** **** 8562")
            )

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        cards.forEachIndexed { index, card ->
                            PaymentMethodItem(card)
                            if (index < cards.size - 1) {
                                HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(start = 80.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * List item component for displaying an existing payment method.
 */
@Composable
private fun PaymentMethodItem(card: PaymentMethod) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mock brand icon placeholder
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(AberColor.SurfaceGray),
            contentAlignment = Alignment.Center
        ) {
            val typeLabel = when (card.type) {
                PaymentMethodType.VISA -> "VISA"
                PaymentMethodType.PAYPAL -> "PAYPAL"
                PaymentMethodType.MASTERCARD -> "MC"
            }
            Text(
                text = typeLabel,
                style = AberTypography.Caption.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    color = AberColor.Ink
                )
            )
        }
        
        Spacer(Modifier.width(16.dp))
        
        Column {
            Text(card.details, style = AberTypography.CardTitle.copy(fontSize = 16.sp))
            Text(
                text = when (card.type) {
                    PaymentMethodType.VISA -> "VISA"
                    PaymentMethodType.PAYPAL -> "Paypal"
                    PaymentMethodType.MASTERCARD -> "Master Card"
                },
                style = AberTypography.Caption.copy(color = AberColor.BorderGray)
            )
        }
    }
}
