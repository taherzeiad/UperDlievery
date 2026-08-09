package com.newuperapp.Uper.ui.screens.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.auth.CountryCode
import com.newuperapp.Uper.domain.auth.defaultCountryCodes
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonVariant
import com.newuperapp.Uper.ui.components.AberPhoneField
import com.newuperapp.Uper.ui.components.AberTextField
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToOtp: (fullPhoneNumber: String) -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SignUpEvent.NavigateToOtp -> onNavigateToOtp(event.fullPhoneNumber)
            }
        }
    }

    SignUpScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onSignUpClick = viewModel::onSignUpClick,
        onSignInClick = onNavigateToSignIn
    )
}

@Composable
fun SignUpScreen(
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    Scaffold(containerColor = AberColor.SurfaceGrayAlt) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ---- Hero header ----
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(AberColor.Yellow)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Sign up") }
                        append(" with email and phone number")
                    },
                    style = AberTypography.HeroTitle.copy(fontSize = 28.sp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 80.dp, start = 28.dp, end = 40.dp)
                )
            }

            // ---- Form ----
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AberTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    placeholder = "name@example.com",
                    label = "Email"
                )
                AberPhoneField(
                    number = uiState.phoneNumber,
                    onNumberChange = onPhoneChange,
                    selectedCountry = CountryCode("VN", "Vietnam", "🇻🇳", "+84"),
                    onCountrySelected = {},
                    countries = defaultCountryCodes
                )

                uiState.errorMessage?.let {
                    Text(it, style = AberTypography.Caption.copy(color = AberColor.Orange))
                }

                Spacer(Modifier.height(8.dp))
                AberButton(
                    text = "Sign up",
                    onClick = onSignUpClick,
                    variant = AberButtonVariant.Dark,
                    enabled = uiState.isSubmitEnabled,
                    isLoading = uiState.isSubmitting
                )
            }

            Spacer(Modifier.height(60.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Already have an account? ", style = AberTypography.Caption)
                Text(
                    text = "Sign In", style = AberTypography.Caption.copy(
                        fontWeight = FontWeight.Bold, color = AberColor.Ink
                    ), modifier = Modifier.clickable(onClick = onSignInClick)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen(
        uiState = SignUpUiState(),
        onEmailChange = {},
        onPhoneChange = {},
        onSignUpClick = {},
        onSignInClick = {})
}
