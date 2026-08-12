package com.newuperapp.Uper.ui.screens.home.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.R
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

data class Notification(val id: String, val title: String, val description: String, val time: String)

/**
 * Screen displaying a list of push notifications and system messages for the driver.
 *
 * @param onBackClick Navigation callback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit
) {
    val notifications = listOf(
        Notification("1", "System Update", "Your app has been updated to the latest version.", "2 hours ago"),
        Notification("2", "Promotion", "Earn double points this weekend!", "5 hours ago"),
        Notification("3", "Ride Feedback", "A passenger gave you a 5-star rating!", "1 day ago")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.notifications_title), 
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
        containerColor = AberColor.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(notifications) { notification ->
                NotificationItem(notification)
                HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
    }
}

/**
 * Single notification entry row.
 */
@Composable
private fun NotificationItem(notification: Notification) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(AberColor.SurfaceGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = AberColor.Yellow, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = notification.title, style = AberTypography.CardTitle.copy(fontSize = 16.sp))
            Spacer(Modifier.height(4.dp))
            Text(text = notification.description, style = AberTypography.Subtitle.copy(fontSize = 14.sp, color = AberColor.Ink.copy(alpha = 0.7f)))
            Spacer(Modifier.height(8.dp))
            Text(text = notification.time, style = AberTypography.Caption)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    NotificationsScreen(onBackClick = {})
}
