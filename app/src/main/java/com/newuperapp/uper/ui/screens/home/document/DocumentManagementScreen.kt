package com.newuperapp.uper.ui.screens.home.document

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.newuperapp.uper.R
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

/**
 * Screen for managing driver legal documents (IDs, License, etc.).
 *
 * @param onBackClick Navigation callback.
 * @param onDrivingLicenseClick Navigation to specific driving license details.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentManagementScreen(
    onBackClick: () -> Unit, onDrivingLicenseClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                Text(
                    text = stringResource(R.string.document_management_title),
                    style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)
                )
            },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AberColor.Yellow
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AberColor.White)
            )
        }, containerColor = AberColor.SurfaceGrayAlt
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DocumentItem(
                    title = stringResource(R.string.document_id_cards_title),
                    iconColor = AberColor.Yellow,
                    onClick = { })
            }
            item {
                DocumentItem(
                    title = stringResource(R.string.document_driving_license_title),
                    iconColor = AberColor.Orange,
                    onClick = onDrivingLicenseClick
                )
            }
        }
    }
}

/**
 * Visual card representing a document type with placeholder illustrations.
 */
@Composable
private fun DocumentItem(title: String, iconColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(120.dp, 160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconColor), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                }
                Spacer(Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                            .background(AberColor.SurfaceGray, RoundedCornerShape(8.dp))
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(15.dp)
                            .background(AberColor.SurfaceGray, RoundedCornerShape(8.dp))
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(15.dp)
                            .background(AberColor.SurfaceGray, RoundedCornerShape(4.dp))
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(15.dp)
                            .background(AberColor.SurfaceGray, RoundedCornerShape(8.dp))
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(title, style = AberTypography.CardTitle.copy(fontSize = 20.sp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DocumentManagementScreenPreview() {
    DocumentManagementScreen(onBackClick = {}, onDrivingLicenseClick = {})
}
