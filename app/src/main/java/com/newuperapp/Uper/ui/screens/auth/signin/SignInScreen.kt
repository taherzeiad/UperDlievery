package com.newuperapp.Uper.ui.screens.auth.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonStyle
import com.newuperapp.Uper.ui.components.AberNumericKeypad
import com.newuperapp.Uper.ui.components.AberPhoneDisplayField
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@Composable
fun SignInRoute(
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToOtp: (fullPhoneNumber: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SignInEvent.NavigateToOtp -> onNavigateToOtp(event.fullPhoneNumber)
            }
        }
    }

    SignInScreen(
        uiState = uiState,
        onDigitPressed = viewModel::onDigitPressed,
        onBackspace = viewModel::onBackspace,
        onClearClick = viewModel::onClearClick,
        onNextClick = viewModel::onNextClick
    )
}

@Composable
fun SignInScreen(
    uiState: SignInUiState,
    onDigitPressed: (String) -> Unit,
    onBackspace: () -> Unit,
    onClearClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold(containerColor = AberColor.Yellow) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            Box(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(200.dp))

                Column(
                    modifier = Modifier
                        .padding(top = 200.dp)
                        .fillMaxWidth()
                        .background(AberColor.White, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .padding(horizontal = 28.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(AberTypography.HeroTitleBold.toSpanStyle()) { append("Login") }
                            withStyle(AberTypography.HeroTitle.toSpanStyle()) { append(" with your phone number") }
                        },
                        style = AberTypography.HeroTitle.copy(fontSize = 28.sp, lineHeight = 36.sp)
                    )

                    AberPhoneDisplayField(
                        countryFlagEmoji = "🇻🇳",
                        dialCode = uiState.dialCode,
                        value = uiState.phoneNumber,
                        onClearClick = onClearClick
                    )

                    uiState.errorMessage?.let {
                        Text(it, style = AberTypography.Caption.copy(color = AberColor.Orange))
                    }

                    AberButton(
                        text = "Next",
                        onClick = onNextClick,
                        style = AberButtonStyle.Dark,
                        enabled = uiState.isNextEnabled,
                        isLoading = uiState.isSubmitting
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            AberNumericKeypad(
                onDigit = onDigitPressed,
                onBackspace = onBackspace,
                onMicClick = { /* voice input */ }
            )
        }
    }
}

private fun TextStyle.toSpanStyle() = SpanStyle(
    color = color, fontSize = fontSize, fontWeight = fontWeight, fontFamily = fontFamily
)
