package com.newuperapp.Uper.ui.screens.home.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.model.Notification
import com.newuperapp.Uper.domain.model.NotificationType
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Notifications", style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)) },
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
        containerColor = AberColor.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val items = listOf(
                Notification("1", NotificationType.SYSTEM, "System", "Booking #1234 has been success...", "Today"),
                Notification("2", NotificationType.PROMOTION, "Promotion", "Invite friends - Get 3 coupons each!", "Today"),
                Notification("3", NotificationType.PROMOTION, "Promotion", "Invite friends - Get 3 coupons each!", "Today"),
                Notification("4", NotificationType.CANCELLED, "System", "Booking #1205 has been cancelled", "Today"),
                Notification("5", NotificationType.WALLET, "System", "Thank you! Your transaction is com...", "Today"),
                Notification("6", NotificationType.PROMOTION, "Promotion", "Invite friends - Get 3 coupons each!", "Today")
            )

            items(items) { item ->
                NotificationItem(item)
                HorizontalDivider(color = AberColor.SurfaceGray, thickness = 1.dp)
            }
        }
    }
}

@Composable
private fun NotificationItem(notification: Notification) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val (icon, color) = when (notification.type) {
            NotificationType.SYSTEM -> Icons.Default.Check to androidx.compose.ui.graphics.Color(0xFF3858F6)
            NotificationType.PROMOTION -> Icons.Default.Percent to AberColor.Yellow
            NotificationType.WALLET -> Icons.Default.Wallet to androidx.compose.ui.graphics.Color(0xFF2ECC71)
            NotificationType.CANCELLED -> Icons.Default.Close to AberColor.Danger
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = AberColor.White, modifier = Modifier.size(24.dp))
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text(notification.title, style = AberTypography.CardTitle.copy(fontSize = 16.sp))
            Text(
                notification.message,
                style = AberTypography.Subtitle.copy(color = AberColor.Ink.copy(alpha = 0.7f), fontSize = 14.sp),
                maxLines = 1
            )
        }
    }
}
