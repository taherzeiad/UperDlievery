package com.newuperapp.Uper.ui.screens.home.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.R
import com.newuperapp.Uper.domain.model.DriverProfile
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography
import com.newuperapp.Uper.ui.screens.home.settings.SettingsViewModel

/**
 * Screen displaying the driver's profile details.
 *
 * @param onBackClick Callback for the back navigation action.
 * @param onEditClick Callback to navigate to the profile editing screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsState()

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
                        Text(
                            text = stringResource(R.string.profile_edit),
                            color = AberColor.Orange,
                            style = AberTypography.Subtitle.copy(fontWeight = FontWeight.Bold)
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
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(20.dp))
                // Profile Photo Placeholder
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
                    Text(
                        text = stringResource(R.string.profile_informations_label),
                        style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray, fontSize = 14.sp)
                    )
                    Spacer(Modifier.height(16.dp))
                    
                    ProfileInfoItem(stringResource(R.string.profile_username), profile?.name ?: "Martha Banks")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileInfoItem(stringResource(R.string.profile_phone_number), "584-490-9153")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileInfoItem(stringResource(R.string.profile_email), "freeslab88@gmail.com")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileInfoItem(stringResource(R.string.profile_gender), "Female")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileInfoItem(stringResource(R.string.profile_birthday), "April 16, 1988")
                }
            }
        }
    }
}

/**
 * Reusable row item for displaying a label and its corresponding profile value.
 */
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
