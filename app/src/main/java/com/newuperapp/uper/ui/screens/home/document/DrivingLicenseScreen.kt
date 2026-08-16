package com.newuperapp.uper.ui.screens.home.document

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.newuperapp.uper.R
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

/**
 * Screen for updating or viewing the driver's license details.
 *
 * @param onBackClick Navigation callback.
 * @param onCompleteClick Submission callback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrivingLicenseScreen(
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    var showPhotoOptions by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.document_driving_license_title),
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
        bottomBar = {
            Button(
                onClick = onCompleteClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AberColor.Yellow,
                    contentColor = AberColor.Ink
                )
            ) {
                Text(
                    text = stringResource(R.string.vehicle_complete_cta),
                    style = AberTypography.semibody17()
                )
            }
        },
        containerColor = AberColor.SurfaceGrayAlt
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(120.dp, 160.dp)
                                        .background(AberColor.Orange, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(80.dp))
                                }
                                Spacer(Modifier.width(20.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Box(modifier = Modifier.fillMaxWidth().height(20.dp).background(AberColor.SurfaceGray, RoundedCornerShape(4.dp)))
                                    Spacer(Modifier.height(8.dp))
                                    Box(modifier = Modifier.fillMaxWidth(0.7f).height(14.dp).background(AberColor.SurfaceGray, RoundedCornerShape(4.dp)))
                                    Spacer(Modifier.height(8.dp))
                                    Box(modifier = Modifier.fillMaxWidth(0.5f).height(14.dp).background(AberColor.SurfaceGray, RoundedCornerShape(4.dp)))
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.document_update_photo),
                                color = AberColor.Yellow,
                                style = AberTypography.CardTitle.copy(fontSize = 22.sp),
                                modifier = Modifier.clickable { showPhotoOptions = true }
                            )
                        }
                    }
                }

                item {
                    DocumentField(stringResource(R.string.document_card_number_label), "1234 567 890")
                }

                item {
                    DocumentField(stringResource(R.string.document_expiration_date_label), "MM/DD/YYYY", isDate = true)
                }
            }

            if (showPhotoOptions) {
                PhotoOptionsBottomSheet(onDismiss = { showPhotoOptions = false })
            }
        }
    }
}

/**
 * Reusable input field for document numbers or dates.
 */
@Composable
private fun DocumentField(label: String, value: String, isDate: Boolean = false) {
    Column {
        Text(label, style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = if (isDate) {
                { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = AberColor.BorderGray, modifier = Modifier.size(20.dp).rotate(180f)) }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

/**
 * Bottom sheet style overlay for choosing photo source.
 */
@Composable
private fun PhotoOptionsBottomSheet(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .padding(20.dp)
        ) {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.document_take_picture),
                    style = AberTypography.Subtitle.copy(fontSize = 20.sp, color = Color(0xFF3858F6))
                )
            }
            HorizontalDivider(color = AberColor.SurfaceGray)
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.document_choose_picture),
                    style = AberTypography.Subtitle.copy(fontSize = 20.sp, color = Color(0xFF3858F6))
                )
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AberColor.SurfaceGray)
            ) {
                Text(
                    text = stringResource(R.string.document_cancel),
                    style = AberTypography.semibody17(color = Color(0xFF3858F6))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrivingLicenseScreenPreview() {
    DrivingLicenseScreen(onBackClick = {}, onCompleteClick = {})
}
