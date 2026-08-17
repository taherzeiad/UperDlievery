package com.newuperapp.uper.ui.screens.location

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.uper.R
import com.newuperapp.uper.ui.components.AberButton
import com.newuperapp.uper.ui.components.AberTextLink
import com.newuperapp.uper.ui.theme.AberColor

/**
 * Screen requesting location permissions from the driver to enable trip matching and tracking.
 */
@Composable
fun EnableLocationRoute(
    onLocationResolved: () -> Unit,
    viewModel: EnableLocationViewModel = hiltViewModel(),
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        viewModel.onPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                EnableLocationEvent.Continue -> onLocationResolved()
            }
        }
    }

    EnableLocationScreen(
        onUseMyLocationClick = {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }, onSkipClick = viewModel::onSkip
    )
}

@Composable
fun EnableLocationScreen(
    onUseMyLocationClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(96.dp))
        Image(
            painter = painterResource(id = R.drawable.located),
            contentDescription = null,
            modifier = Modifier.width(263.dp).height(230.dp)
        )
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(id = R.string.location_title),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = AberColor.Ink,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(id = R.string.location_description),
            fontSize = 17.sp,
            color = AberColor.Ink,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(70.dp))
        AberButton(
            text = stringResource(id = R.string.location_use_my_location),
            onClick = onUseMyLocationClick
        )
        Spacer(modifier = Modifier.height(45.dp))
        AberTextLink(
            text = stringResource(id = R.string.location_skip), onClick = onSkipClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnableLocationScreenPreview() {
    EnableLocationScreen(
        onUseMyLocationClick = {},
        onSkipClick = {},
    )
}
