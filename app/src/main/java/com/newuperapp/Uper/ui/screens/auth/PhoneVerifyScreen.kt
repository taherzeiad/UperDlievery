package com.newuperapp.Uper.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonVariant
import com.newuperapp.Uper.ui.components.AberNumericKeypad
import com.newuperapp.Uper.ui.components.AberOtpInput
import com.newuperapp.Uper.ui.theme.AberColor

/**
 * نقطة الدخول الحقيقية (متوصولة بالـ ViewModel والـ Navigation).
 */
@Composable
fun PhoneVerifyRoute(
    onVerified: () -> Unit,
    onBack: () -> Unit,
    viewModel: PhoneVerifyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                PhoneVerifyEvent.NavigateToHome -> onVerified()
                PhoneVerifyEvent.NavigateBack -> onBack()
            }
        }
    }

    PhoneVerifyScreen(
        uiState = uiState,
        onBackClick = viewModel::onBackClick,
        onDigitClick = viewModel::onDigitEntered,
        onBackspaceClick = viewModel::onBackspace,
        onVerifyClick = viewModel::onVerifyClick
    )
}

/**
 * الشاشة نفسها بدون أي منطق — قابلة للمعاينة (Preview) بشكل مباشر.
 */
@Composable
fun PhoneVerifyScreen(
    uiState: PhoneVerifyUiState,
    onBackClick: () -> Unit,
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onVerifyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AberColor.SurfaceGrayAlt)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            IconButton(onClick = onBackClick, modifier = Modifier.padding(8.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AberColor.Yellow
                )
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Phone Verification",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = AberColor.Ink
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Enter your OTP code here",
                    fontSize = 17.sp,
                    color = AberColor.Ink
                )

                Spacer(modifier = Modifier.height(48.dp))

                AberOtpInput(code = uiState.code)

                uiState.errorMessage?.let { message ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = message, fontSize = 13.sp, color = AberColor.Orange)
                }

                Spacer(modifier = Modifier.height(40.dp))

                AberButton(
                    text = "VERIFY NOW",
                    onClick = onVerifyClick,
                    variant = AberButtonVariant.Primary,
                    enabled = uiState.isComplete,
                    isLoading = uiState.isSubmitting
                )
            }
        }

        AberNumericKeypad(
            onDigitClick = onDigitClick,
            onBackspaceClick = onBackspaceClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhoneVerifyScreenPreview() {
    PhoneVerifyScreen(
        uiState = PhoneVerifyUiState(code = "45"),
        onBackClick = {},
        onDigitClick = {},
        onBackspaceClick = {},
        onVerifyClick = {}
    )
}