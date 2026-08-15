package com.newuperapp.Uper.ui.screens.auth.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.R
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonStyle
import com.newuperapp.Uper.ui.components.AberNumericKeypad
import com.newuperapp.Uper.ui.components.AberPhoneDisplayField
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/**
 * Sign In screen handling phone number input using a custom numeric keypad.
 */
@Composable
fun SignInRoute(
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToOtp: (fullPhoneNumber: String) -> Unit,
    onNavigateToSignUp: () -> Unit
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
        onNextClick = viewModel::onNextClick,
        onSignUpClick = onNavigateToSignUp
    )
}

@Composable
fun SignInScreen(
    uiState: SignInUiState,
    onDigitPressed: (String) -> Unit,
    onBackspace: () -> Unit,
    onClearClick: () -> Unit,
    onNextClick: () -> Unit,
    onSignUpClick: () -> Unit
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
                            withStyle(AberTypography.HeroTitleBold.toSpanStyle()) { 
                                append(stringResource(R.string.auth_login_title)) 
                            }
                            withStyle(AberTypography.HeroTitle.toSpanStyle()) { 
                                append(stringResource(R.string.auth_login_subtitle)) 
                            }
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
                        text = stringResource(R.string.auth_next_cta),
                        onClick = onNextClick,
                        style = AberButtonStyle.Dark,
                        enabled = uiState.isNextEnabled,
                        isLoading = uiState.isSubmitting
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(AberTypography.Subtitle.toSpanStyle()) { 
                                append(stringResource(R.string.auth_no_account)) 
                            }
                            withStyle(
                                SpanStyle(fontWeight = FontWeight.Bold, color = AberColor.Orange)
                            ) { 
                                append(stringResource(R.string.auth_sign_up_cta)) 
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onSignUpClick)
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
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
private fun SignInScreenPreview() {
    SignInScreen(
        uiState = SignInUiState(phoneNumber = "905070017"),
        onDigitPressed = {},
        onBackspace = {},
        onClearClick = {},
        onNextClick = {},
        onSignUpClick = {}
    )
}

private fun TextStyle.toSpanStyle() = SpanStyle(
    color = color, fontSize = fontSize, fontWeight = fontWeight, fontFamily = fontFamily
)
