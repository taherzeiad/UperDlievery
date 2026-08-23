package com.newuperapp.uper.ui.screens.home.vehicle

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
import androidx.compose.ui.tooling.preview.Preview
import com.newuperapp.uper.R
import com.newuperapp.uper.ui.components.AberFormField
import com.newuperapp.uper.ui.components.AberTopBar
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

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
            AberTopBar(
                title = stringResource(R.string.vehicle_add_new_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Button(
                onClick = onCompleteClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
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
            item { AberFormField(stringResource(R.string.vehicle_brand_label), "Toyota") }
            item { AberFormField(stringResource(R.string.vehicle_model_label), "Camry") }
            item { AberFormField(stringResource(R.string.vehicle_year_label), "2018") }
            item {
                AberFormField(
                    stringResource(R.string.vehicle_license_plate_label),
                    "43A 364.82"
                )
            }
            item { AberFormField(stringResource(R.string.vehicle_color_label), "Black") }
            item {
                AberFormField(
                    stringResource(R.string.vehicle_booking_type_label),
                    "Taxi 7 Seat"
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AddVehicleScreenPreview() {
    AddVehicleScreen(onBackClick = {}, onCompleteClick = {})
}

