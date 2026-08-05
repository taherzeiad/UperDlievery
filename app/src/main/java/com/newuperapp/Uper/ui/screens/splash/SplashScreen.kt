package com.newuperapp.Uper.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.R
import com.newuperapp.Uper.ui.theme.AberColor

@Composable
fun SplashRoute(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.destination.collect { destination ->
            when (destination) {
                SplashDestination.Onboarding -> onNavigateToOnboarding()
                SplashDestination.Home -> onNavigateToHome()
            }
        }
    }
    SplashScreen()
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AberColor.Yellow)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_splash_skyline),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(AberColor.Ink), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_wheel),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = AberColor.Ink
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
