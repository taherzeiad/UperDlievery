package com.newuperapp.Uper.ui.screens.auth.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.R
import com.newuperapp.Uper.domain.auth.CountryCode
import com.newuperapp.Uper.domain.auth.defaultCountryCodes
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonVariant
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
        onCountrySelected = { /* TODO if needed */ },
        onDigitPressed = viewModel::onDigitPressed,
        onBackspace = viewModel::onBackspace,
        onNextClick = viewModel::onNextClick
    )
}

@Composable
fun SignInScreen(
    uiState: SignInUiState,
    onCountrySelected: (CountryCode) -> Unit,
    onDigitPressed: (String) -> Unit,
    onBackspace: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(AberColor.Yellow)) {
        Box(modifier = Modifier.weight(1f)) {
            // Header Image
            Image(
                painter = painterResource(id = R.drawable.group_2),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            // White Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(AberColor.White)
                    .padding(horizontal = 32.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Login") }
                        append(" with your phone number")
                    },
                    style = AberTypography.HeroTitle.copy(fontSize = 28.sp, lineHeight = 36.sp)
                )

                AberPhoneDisplayField(
                    number = uiState.phoneNumber,
                    selectedCountry = CountryCode("VN", "Vietnam", "🇻🇳", "+84"),
                    onCountrySelected = onCountrySelected,
                    countries = defaultCountryCodes,
                    modifier = Modifier.fillMaxWidth()
                )

                uiState.errorMessage?.let {
                    Text(it, style = AberTypography.Caption.copy(color = AberColor.Orange))
                }

                AberButton(
                    text = "Next",
                    onClick = onNextClick,
                    variant = AberButtonVariant.Dark,
                    enabled = uiState.isNextEnabled,
                    isLoading = uiState.isSubmitting
                )
            }
        }

        AberNumericKeypad(
            onDigitClick = onDigitPressed,
            onBackspaceClick = onBackspace,
            onMicClick = { /* voice input */ }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    SignInScreen(
        uiState = SignInUiState(),
        onCountrySelected = {},
        onDigitPressed = {},
        onBackspace = {},
        onNextClick = {}
    )
}
