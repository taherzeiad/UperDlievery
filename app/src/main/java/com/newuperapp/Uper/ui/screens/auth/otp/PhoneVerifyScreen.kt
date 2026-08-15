package com.newuperapp.Uper.ui.screens.auth.otp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.newuperapp.Uper.R
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonStyle
import com.newuperapp.Uper.ui.components.AberNumericKeypad
import com.newuperapp.Uper.ui.components.AberOtpInput
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/**
 * OTP Verification screen where drivers enter the code sent to their phone.
 */
@Composable
fun PhoneVerifyRoute(
    viewModel: PhoneVerifyViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                PhoneVerifyEvent.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    PhoneVerifyScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onDigitPressed = viewModel::onDigitPressed,
        onBackspace = viewModel::onBackspace,
        onVerifyClick = viewModel::onVerifyClick,
        onResendClick = viewModel::onResendClick
    )
}

@Composable
fun PhoneVerifyScreen(
    uiState: PhoneVerifyUiState,
    onBackClick: () -> Unit,
    onDigitPressed: (String) -> Unit,
    onBackspace: () -> Unit,
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit
) {
    Scaffold(containerColor = AberColor.SurfaceGrayAlt) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 12.dp, top = 8.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AberColor.Yellow,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp).padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.auth_phone_verification_title),
                    style = AberTypography.ScreenTitle
                )
                Text(
                    text = stringResource(R.string.auth_otp_description),
                    style = AberTypography.Subtitle
                )

                Spacer(Modifier.height(36.dp))

                AberOtpInput(code = uiState.code, length = 4)

                uiState.errorMessage?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(it, style = AberTypography.Caption.copy(color = AberColor.Orange))
                }

                Spacer(Modifier.height(24.dp))

                AberButton(
                    text = stringResource(R.string.auth_verify_now_cta),
                    onClick = onVerifyClick,
                    style = AberButtonStyle.Primary,
                    enabled = uiState.isVerifyEnabled,
                    isLoading = uiState.isVerifying
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = if (uiState.isResending) {
                        stringResource(R.string.auth_resending)
                    } else {
                        stringResource(R.string.auth_resend_code_cta)
                    },
                    style = AberTypography.semibody17(AberColor.Orange).copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable(enabled = !uiState.isResending, onClick = onResendClick)
                )
            }

            Spacer(Modifier.weight(1f))

            AberNumericKeypad(
                onDigit = onDigitPressed,
                onBackspace = onBackspace,
                onMicClick = { /* voice input placeholder */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhoneVerifyScreenPreview() {
    PhoneVerifyScreen(
        uiState = PhoneVerifyUiState(code = "123", isVerifying = false),
        onBackClick = {},
        onDigitPressed = {},
        onBackspace = {},
        onVerifyClick = {},
        onResendClick = {}
    )
}

