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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonStyle
import com.newuperapp.Uper.ui.components.AberPhoneField
import com.newuperapp.Uper.ui.components.AberTextField
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/** Stateful entry point — wired to Hilt + navigation. */
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

/** Stateless — pure function of [uiState], safe for @Preview. */
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
                    .height(300.dp)
                    .background(AberColor.Yellow)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(AberTypography.HeroTitleBold.toSpanStyle()) { append("Sign up") }
                        withStyle(AberTypography.HeroTitle.toSpanStyle()) { append(" with email and phone number") }
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 90.dp, start = 28.dp, end = 40.dp)
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
                    keyboardType = KeyboardType.Email
                )
                AberPhoneField(
                    countryFlagEmoji = "🇻🇳",
                    dialCode = uiState.dialCode,
                    value = uiState.phoneNumber,
                    onValueChange = onPhoneChange
                )

                uiState.errorMessage?.let {
                    Text(it, style = AberTypography.Caption.copy(color = AberColor.Orange))
                }

                Spacer(Modifier.height(8.dp))
                AberButton(
                    text = "Sign up",
                    onClick = onSignUpClick,
                    style = AberButtonStyle.Dark,
                    enabled = uiState.isSubmitEnabled,
                    isLoading = uiState.isSubmitting
                )
            }

            Spacer(Modifier.height(60.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(AberTypography.Subtitle.toSpanStyle()) { append("Already have an account? ") }
                    withStyle(
                        AberTypography.semibody17().copy(fontWeight = FontWeight.Bold).toSpanStyle()
                    ) { append("Sign In") }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onSignInClick)
                    .padding(vertical = 16.dp)
            )
        }
    }
}

private fun TextStyle.toSpanStyle() = SpanStyle(
    color = color, fontSize = fontSize, fontWeight = fontWeight, fontFamily = fontFamily
)
