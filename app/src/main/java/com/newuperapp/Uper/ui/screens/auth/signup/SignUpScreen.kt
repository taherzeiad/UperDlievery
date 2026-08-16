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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.R
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonStyle
import com.newuperapp.Uper.ui.components.AberPhoneField
import com.newuperapp.Uper.ui.components.AberTextField
import com.newuperapp.Uper.ui.utils.CountryUtils
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography
import com.joelkanyi.jcomposecountrycodepicker.component.CountrySelectionDialog
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState

/**
 * Sign Up screen allowing new drivers to register using email and phone number.
 */
@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToOtp: (fullPhoneNumber: String) -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val ccpState = rememberKomposeCountryCodePickerState()

    var showCountryPicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SignUpEvent.NavigateToOtp -> onNavigateToOtp(event.fullPhoneNumber)
            }
        }
    }

    if (showCountryPicker) {
        CountrySelectionDialog(
            countryList = ccpState.countryList,
            onDismissRequest = { showCountryPicker = false },
            onSelect = { country ->
                viewModel.onCountrySelected(country.phoneNoCode, CountryUtils.getFlagEmoji(country.code))
                showCountryPicker = false
            },
            containerColor = AberColor.White,
            contentColor = AberColor.Ink
        )
    }

    SignUpScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onSignUpClick = viewModel::onSignUpClick,
        onSignInClick = onNavigateToSignIn,
        onCountryClick = { showCountryPicker = true }
    )
}

@Composable
fun SignUpScreen(
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onCountryClick: () -> Unit
) {
    Scaffold(containerColor = AberColor.SurfaceGrayAlt) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(AberColor.Yellow)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(AberTypography.HeroTitleBold.toSpanStyle()) { 
                            append(stringResource(R.string.auth_sign_up_title)) 
                        }
                        withStyle(AberTypography.HeroTitle.toSpanStyle()) { 
                            append(stringResource(R.string.auth_sign_up_subtitle)) 
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 90.dp, start = 28.dp, end = 40.dp)
                )
            }

            // Form Fields
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
                    placeholder = stringResource(R.string.auth_email_placeholder),
                    keyboardType = KeyboardType.Email
                )
                AberPhoneField(
                    countryFlagEmoji = uiState.countryFlag,
                    dialCode = uiState.dialCode,
                    value = uiState.phoneNumber,
                    onValueChange = onPhoneChange,
                    onCountryClick = onCountryClick
                )

                uiState.errorMessage?.let {
                    Text(it, style = AberTypography.Caption.copy(color = AberColor.Orange))
                }

                Spacer(Modifier.height(8.dp))
                AberButton(
                    text = stringResource(R.string.auth_sign_up_cta),
                    onClick = onSignUpClick,
                    style = AberButtonStyle.Dark,
                    enabled = uiState.isSubmitEnabled,
                    isLoading = uiState.isSubmitting
                )
            }

            Spacer(Modifier.height(60.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(AberTypography.Subtitle.toSpanStyle()) { 
                        append(stringResource(R.string.auth_already_account)) 
                    }
                    withStyle(
                        AberTypography.semibody17().copy(fontWeight = FontWeight.Bold).toSpanStyle()
                    ) { 
                        append(stringResource(R.string.auth_sign_in_cta)) 
                    }
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

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen(
        uiState = SignUpUiState(email = "test@example.com", phoneNumber = "905070017"),
        onEmailChange = {},
        onPhoneChange = {},
        onSignUpClick = {},
        onSignInClick = {},
        onCountryClick = {}
    )
}

