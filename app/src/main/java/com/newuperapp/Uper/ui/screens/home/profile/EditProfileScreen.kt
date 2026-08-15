package com.newuperapp.Uper.ui.screens.home.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
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
import androidx.compose.ui.tooling.preview.Preview
import com.newuperapp.Uper.R
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/**
 * Screen for editing driver profile information.
 *
 * @param onCancelClick Callback to discard changes and return.
 * @param onDoneClick Callback to save changes and return.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onCancelClick: () -> Unit,
    onDoneClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    TextButton(onClick = onCancelClick) {
                        Text(
                            text = stringResource(R.string.profile_cancel),
                            color = AberColor.Orange,
                            style = AberTypography.Subtitle.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onDoneClick) {
                        Text(
                            text = stringResource(R.string.profile_done),
                            color = AberColor.BorderGray,
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
                .padding(padding)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.Top // محاذاة من الأعلى ليكون شكل الصورة والـ Fields متناسقاً
                ) {
                    // قسم الصورة والزر أسفلها مباشرة
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(AberColor.SurfaceGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = AberColor.Yellow,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        // نص التعديل أسفل الصورة مباشرة
                        Text(
                            text = stringResource(R.string.profile_edit_photo),
                            color = AberColor.Orange,
                            style = AberTypography.Subtitle,
                            modifier = Modifier.clickable { /* تنفيذ إجراء التعديل */ }
                        )
                    }

                    Spacer(Modifier.width(20.dp))

                    // قسم حقول الأسماء
                    Column(modifier = Modifier.weight(1f)) {
                        TextField(
                            value = "Martha",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = AberTypography.Subtitle.copy(fontSize = 18.sp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = AberColor.SurfaceGray,
                                focusedIndicatorColor = AberColor.Orange
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        TextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.profile_last_name_placeholder),
                                    color = AberColor.BorderGray,
                                    style = AberTypography.Subtitle.copy(fontSize = 18.sp)
                                )
                            },
                            textStyle = AberTypography.Subtitle.copy(fontSize = 18.sp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = AberColor.SurfaceGray,
                                focusedIndicatorColor = AberColor.Orange
                            )
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = AberColor.SurfaceGray, thickness = 1.dp)
            }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    EditInfoItem(stringResource(R.string.profile_phone_number), "584-490-9153")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    EditInfoItem(stringResource(R.string.profile_email), "freeslab88@gmail.com")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    EditInfoItem(stringResource(R.string.profile_gender), "Female")
                    HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))
                    EditInfoItem(stringResource(R.string.profile_birthday), "April 16, 1988")
                }
            }
        }
    }
}

/**
 * Row item for editing profile fields.
 */
@Composable
private fun EditInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = AberTypography.Subtitle.copy(fontSize = 18.sp), modifier = Modifier.weight(1f))
        Text(value, style = AberTypography.Subtitle.copy(color = AberColor.BorderGray, fontSize = 18.sp))
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(onCancelClick = {}, onDoneClick = {})
}