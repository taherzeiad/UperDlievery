package com.newuperapp.Uper.ui.screens.home.vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.R
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/**
 * Form for adding a new vehicle to the driver's account.
 *
 * @param onBackClick Navigation callback.
 * @param onCompleteClick Submission callback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleScreen(
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.vehicle_add_new_title),
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { VehicleField(stringResource(R.string.vehicle_brand_label), "Toyota") }
            item { VehicleField(stringResource(R.string.vehicle_model_label), "Camry") }
            item { VehicleField(stringResource(R.string.vehicle_year_label), "2018") }
            item { VehicleField(stringResource(R.string.vehicle_license_plate_label), "43A 364.82") }
            item { VehicleField(stringResource(R.string.vehicle_color_label), "Black") }
            item { VehicleField(stringResource(R.string.vehicle_booking_type_label), "Taxi 7 Seat") }
        }
    }
}

/**
 * Single input field with a label and chevron for selection-based forms.
 */
@Composable
private fun VehicleField(label: String, value: String) {
    Column {
        Text(label, style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AberColor.BorderGray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        )
    }
}
