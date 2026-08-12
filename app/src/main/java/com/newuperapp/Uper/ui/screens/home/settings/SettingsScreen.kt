package com.newuperapp.Uper.ui.screens.home.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.model.DriverProfile
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    profile: DriverProfile?,
    onBackClick: () -> Unit,
    onVehicleManagementClick: () -> Unit,
    onDocumentManagementClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Back", tint = AberColor.Yellow)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AberColor.White)
            )
        },
        containerColor = AberColor.White
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                ProfileHeader(profile, onClick = onProfileClick)
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = AberColor.SurfaceGray, thickness = 8.dp)
            }

            item {
                SettingsItem(Icons.Default.DirectionsCar, "Vehicle Management", Color(0xFFFF8900), onClick = onVehicleManagementClick)
                HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(start = 60.dp))
                SettingsItem(Icons.Default.AssignmentInd, "Document Management", Color(0xFF2ECC71), onClick = onDocumentManagementClick)
                HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(start = 60.dp))
                SettingsItem(Icons.Default.Star, "Reviews", Color(0xFFFFD428), onClick = {})
                HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(start = 60.dp))
                SettingsItem(Icons.Default.Language, "Language", Color(0xFF3858F6), onClick = {})
                
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = AberColor.SurfaceGray, thickness = 8.dp)
            }

            item {
                SettingsItem(Icons.Default.Notifications, "Notifications", Color(0xFF3858F6), onClick = {})
                HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(start = 60.dp))
                SettingsItem(Icons.Default.Policy, "Terms & Privacy Policy", Color(0xFF9AA0AC), onClick = {})
                HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(start = 60.dp))
                SettingsItem(Icons.Default.Help, "Contact us", Color(0xFFE22D2D), onClick = {})
            }
        }
    }
}

@Composable
private fun ProfileHeader(profile: DriverProfile?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(70.dp).clip(CircleShape).background(AberColor.SurfaceGray))
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(profile?.name ?: "Martha Banks", style = AberTypography.CardTitle.copy(fontSize = 20.sp))
            Text(profile?.level ?: "Gold Member", style = AberTypography.Caption)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AberColor.BorderGray)
    }
}

@Composable
private fun SettingsItem(icon: ImageVector, label: String, iconBgColor: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = AberColor.White, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(16.dp))
        Text(label, style = AberTypography.Subtitle, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AberColor.BorderGray)
    }
}
