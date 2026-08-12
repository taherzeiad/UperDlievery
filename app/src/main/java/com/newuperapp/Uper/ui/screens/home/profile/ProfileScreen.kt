package com.newuperapp.Uper.ui.screens.home.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.model.DriverProfile
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: DriverProfile?,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AberColor.Yellow)
                    }
                },
                actions = {
                    TextButton(onClick = onEditClick) {
                        Text("Edit", color = AberColor.Orange, style = AberTypography.Subtitle.copy(fontWeight = FontWeight.Bold))
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
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(AberColor.SurfaceGray)
                )
                Spacer(Modifier.height(20.dp))
                Text(profile?.name ?: "Martha Banks", style = AberTypography.CardTitle.copy(fontSize = 24.sp))
                Text(profile?.level ?: "Gold Member", style = AberTypography.Caption.copy(fontSize = 16.sp))
                Spacer(Modifier.height(40.dp))
            }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    Text("INFORMATIONS", style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray, fontSize = 14.sp))
                    Spacer(Modifier.height(16.dp))
                    
                    ProfileInfoItem("Username", profile?.name ?: "Martha Banks")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileInfoItem("Phone number", "584-490-9153")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileInfoItem("Email", "freeslab88@gmail.com")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileInfoItem("Gender", "Female")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileInfoItem("Birthday", "April 16, 1988")
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = AberTypography.Subtitle.copy(fontSize = 18.sp), modifier = Modifier.weight(1f))
        Text(value, style = AberTypography.Subtitle.copy(color = AberColor.BorderGray, fontSize = 18.sp))
        Spacer(Modifier.width(8.dp))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AberColor.BorderGray, modifier = Modifier.size(20.dp))
    }
}
