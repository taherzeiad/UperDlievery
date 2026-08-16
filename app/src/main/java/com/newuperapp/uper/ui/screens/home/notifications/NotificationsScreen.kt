package com.newuperapp.uper.ui.screens.home.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.uper.R
import com.newuperapp.uper.domain.model.Notification
import com.newuperapp.uper.domain.model.NotificationType
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberColor.White
import com.newuperapp.uper.ui.theme.AberTypography

/**
 * Screen displaying a list of push notifications and system messages for the driver.
 *
 * @param onBackClick Navigation callback (hamburger menu / drawer toggle).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit, viewModel: NotificationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NotificationsScreen(
        uiState = uiState, onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState, onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.notifications_title),
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
        },
        // Light gray shows through above the first row and below the last one,
        // matching the subtle band visible in the design.
        containerColor = AberColor.SurfaceGrayAlt
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item { Spacer(Modifier.height(16.dp)) }

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
            } else {
                items(uiState.notifications) { notification ->
                    NotificationItem(notification)
                    HorizontalDivider(color = AberColor.SurfaceGray)
                }
            }
        }
    }
}

/**
 * Visual style (icon + background color) for a notification, derived
 * from its [NotificationType]. Adjust the mapping/colors if your
 * design system defines different ones per type.
 */
private data class NotificationStyle(val icon: ImageVector, val backgroundColor: Color)

private fun styleFor(type: NotificationType): NotificationStyle = when (type) {
    NotificationType.BOOKING_SUCCESS -> NotificationStyle(Icons.Default.Check, Color(0xFF3858F6))
    NotificationType.BOOKING_CANCELLED -> NotificationStyle(Icons.Default.Close, Color(0xFFEF3355))
    NotificationType.PROMOTION -> NotificationStyle(
        Icons.Default.ConfirmationNumber, AberColor.Yellow
    )

    NotificationType.PAYMENT -> NotificationStyle(
        Icons.Default.AccountBalanceWallet, Color(0xFF2ED9A6)
    )

    NotificationType.SYSTEM -> NotificationStyle(Icons.Default.Notifications, AberColor.BorderGray)
}

/**
 * Single notification entry row: a colored icon circle (varies by
 * notification type), a bold title, and a single-line message with
 * any "#number" token (e.g. "#1234") rendered in bold, ellipsized if
 * it overflows.
 */
@Composable
private fun NotificationItem(notification: Notification) {
    val style = styleFor(notification.type)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AberColor.White)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(style.backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = style.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title, style = AberTypography.CardTitle.copy(
                    fontSize = 18.sp, fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = highlightNumberTokens(notification.message),
                style = AberTypography.Subtitle.copy(
                    fontSize = 16.sp, color = AberColor.Ink.copy(alpha = 0.8f)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Bolds any "#123" style token inside [message] (e.g. booking numbers),
 * matching the inline emphasis seen in the design.
 */
private fun highlightNumberTokens(message: String) = buildAnnotatedString {
    val regex = Regex("#\\d+")
    var lastIndex = 0
    for (match in regex.findAll(message)) {
        append(message.substring(lastIndex, match.range.first))
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(match.value)
        }
        lastIndex = match.range.last + 1
    }
    append(message.substring(lastIndex))
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    NotificationsScreen(
        uiState = NotificationsUiState(
            notifications = listOf(
                Notification(
                    "1",
                    NotificationType.BOOKING_SUCCESS,
                    "System",
                    "Booking #1234 has been successful",
                    "2 hours ago"
                ), Notification(
                    "2",
                    NotificationType.PROMOTION,
                    "Promotion",
                    "Invite friends - Get 3 coupons each!",
                    "3 hours ago"
                ), Notification(
                    "3",
                    NotificationType.PROMOTION,
                    "Promotion",
                    "Invite friends - Get 3 coupons each!",
                    "5 hours ago"
                ), Notification(
                    "4",
                    NotificationType.BOOKING_CANCELLED,
                    "System",
                    "Booking #1205 has been cancelled",
                    "1 day ago"
                ), Notification(
                    "5",
                    NotificationType.PAYMENT,
                    "System",
                    "Thank you! Your transaction is complete",
                    "1 day ago"
                ), Notification(
                    "6",
                    NotificationType.PROMOTION,
                    "Promotion",
                    "Invite friends - Get 3 coupons each!",
                    "2 days ago"
                )
            ), isLoading = false
        ), onBackClick = {})
}