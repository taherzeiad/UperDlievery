package com.newuperapp.uper.ui.screens.home.vehicle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
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
import com.newuperapp.uper.domain.model.Vehicle
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

/**
 * Screen displaying the driver's registered vehicles and allows selecting the active one.
 *
 * @param onBackClick Navigation callback.
 * @param onAddVehicleClick Navigation to add a new vehicle.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleManagementScreen(
    onBackClick: () -> Unit,
    onAddVehicleClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.vehicle_management_title),
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddVehicleClick,
                containerColor = AberColor.Yellow,
                contentColor = AberColor.Ink,
                shape = CircleShape,
                modifier = Modifier.size(70.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(40.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = AberColor.SurfaceGrayAlt
    ) { padding ->
        val vehicles = listOf(
            Vehicle("1", "Madza", "43A 235.70", "", "", "", "", true),
            Vehicle("2", "Mitsubishi Outlander", "43A 125.84", "", "", "", "", false)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vehicles) { vehicle ->
                VehicleItem(vehicle)
            }
        }
    }
}

/**
 * Card component for a single vehicle in the list.
 */
@Composable
private fun VehicleItem(vehicle: Vehicle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(AberColor.Yellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Assignment, contentDescription = null, tint = Color.White)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(vehicle.brand, style = AberTypography.CardTitle.copy(fontSize = 18.sp))
                Text(vehicle.model, style = AberTypography.Caption.copy(fontSize = 14.sp))
            }
            if (vehicle.isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AberColor.Orange, modifier = Modifier.size(28.dp))
            } else {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .border(1.dp, AberColor.Orange, CircleShape)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VehicleManagementScreenPreview() {
    VehicleManagementScreen(onBackClick = {}, onAddVehicleClick = {})
}
