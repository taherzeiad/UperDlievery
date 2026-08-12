package com.newuperapp.Uper.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.model.DriverProfile
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@Composable
fun AberDrawer(
    profile: DriverProfile,
    onMenuItemClick: (DrawerMenuItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(AberColor.White)
    ) {
        // Header (Yellow Section)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AberColor.Yellow)
                .clickable { onMenuItemClick(DrawerMenuItem.Profile) }
                .padding(top = 60.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(AberColor.SurfaceGray)
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = profile.name,
                        style = AberTypography.CardTitle.copy(fontSize = 20.sp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AberColor.White.copy(alpha = 0.9f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = AberColor.Yellow,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = profile.level,
                                style = AberTypography.Caption.copy(
                                    color = AberColor.Yellow,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.AccessTime,
                    value = profile.hoursOnline.toString(),
                    label = "Hours online"
                )
                StatItem(
                    icon = Icons.Default.Speed,
                    value = "${profile.totalDistanceKm.toInt()} KM",
                    label = "Total Distance"
                )
                StatItem(
                    icon = Icons.Default.Route,
                    value = profile.totalJobs.toString(),
                    label = "Total Jobs"
                )
            }
        }

        // Menu Items
        Column(modifier = Modifier.padding(vertical = 20.dp)) {
            DrawerItem(
                icon = Icons.Default.Home,
                label = "Home",
                onClick = { onMenuItemClick(DrawerMenuItem.Home) }
            )
            DrawerItem(
                icon = Icons.Outlined.AccountBalanceWallet,
                label = "My Wallet",
                onClick = { onMenuItemClick(DrawerMenuItem.Wallet) }
            )
            DrawerItem(
                icon = Icons.Default.History,
                label = "History",
                onClick = { onMenuItemClick(DrawerMenuItem.History) }
            )
            DrawerItem(
                icon = Icons.Outlined.Notifications,
                label = "Notifications",
                onClick = { onMenuItemClick(DrawerMenuItem.Notifications) }
            )
            DrawerItem(
                icon = Icons.Default.CardGiftcard,
                label = "Invite Friends",
                onClick = { onMenuItemClick(DrawerMenuItem.InviteFriends) }
            )
            DrawerItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = { onMenuItemClick(DrawerMenuItem.Settings) }
            )
            DrawerItem(
                icon = Icons.Default.Logout,
                label = "Logout",
                onClick = { onMenuItemClick(DrawerMenuItem.Logout) }
            )
        }
    }
}

@Composable
private fun StatItem(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = AberColor.Ink, modifier = Modifier.size(28.dp))
        Spacer(Modifier.height(8.dp))
        Text(value, style = AberTypography.StatValue.copy(fontSize = 18.sp))
        Text(label, style = AberTypography.StatLabel.copy(color = AberColor.Ink.copy(alpha = 0.5f)))
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = AberColor.BorderGray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(20.dp))
        Text(
            text = label,
            style = AberTypography.Subtitle.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        )
    }
}

enum class DrawerMenuItem {
    Home, Wallet, History, Notifications, InviteFriends, Settings, Logout, Profile
}
