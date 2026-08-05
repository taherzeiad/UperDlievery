package com.newuperapp.Uper.ui.screens.location

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.newuperapp.Uper.R
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberTextLink
import com.newuperapp.Uper.ui.theme.AberColor

@Composable
fun EnableLocationRoute(
    onLocationResolved: () -> Unit,
    viewModel: EnableLocationViewModel = hiltViewModel()
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
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
        },
        onSkipClick = viewModel::onSkip
    )
}

@Composable
fun EnableLocationScreen(
    onUseMyLocationClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(96.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_enable_location),
            contentDescription = null,
            modifier = Modifier.size(240.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(id = R.string.location_title),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = AberColor.Ink,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.location_description),
            fontSize = 16.sp,
            color = AberColor.BorderGray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        AberButton(
            text = stringResource(id = R.string.location_use_my_location),
            onClick = onUseMyLocationClick
        )
        Spacer(modifier = Modifier.height(20.dp))
        AberTextLink(
            text = stringResource(id = R.string.location_skip),
            onClick = onSkipClick
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun EnableLocationScreenPreview() {
    EnableLocationScreen(onUseMyLocationClick = {}, onSkipClick = {})
}
