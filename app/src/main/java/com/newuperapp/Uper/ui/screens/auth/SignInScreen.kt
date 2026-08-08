package com.newuperapp.Uper.ui.screens.auth

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.domain.auth.CountryCode
import com.newuperapp.Uper.domain.auth.defaultCountryCodes
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonVariant
import com.newuperapp.Uper.ui.components.AberNumericKeypad
import com.newuperapp.Uper.ui.components.AberPhoneDisplayField
import com.newuperapp.Uper.ui.theme.AberColor

/**
 * نقطة الدخول الحقيقية (متوصولة بالـ ViewModel والـ Navigation).
 */
@Composable
fun SignInRoute(
    onNavigateToVerification: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SignInEvent.NavigateToVerification -> onNavigateToVerification()
            }
        }
    }

    SignInScreen(
        uiState = uiState,
        onCountrySelected = viewModel::onCountrySelected,
        onDigitClick = viewModel::onDigitEntered,
        onBackspaceClick = viewModel::onBackspace,
        onNextClick = viewModel::onNextClick
    )
}

/**
 * الشاشة نفسها بدون أي منطق — قابلة للمعاينة (Preview) بشكل مباشر.
 */
@Composable
fun SignInScreen(
    uiState: SignInUiState,
    onCountrySelected: (CountryCode) -> Unit,
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            // خلفية صفراء بخط أفق المدينة
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(AberColor.Yellow)
            ) {
                Image(
                    painter = painterResource(id = com.newuperapp.Uper.R.drawable.img_splash_skyline),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }

            // البطاقة البيضاء العلوية (تتراكب فوق الخلفية الصفراء)
            Column(
                modifier = Modifier
                    .offset(y = (-56).dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(AberColor.White)
                    .padding(24.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Login ") }
                        append("with your phone number")
                    },
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    color = AberColor.Ink
                )

                Spacer(modifier = Modifier.height(24.dp))

                AberPhoneDisplayField(
                    number = uiState.phoneNumber,
                    selectedCountry = uiState.country,
                    onCountrySelected = onCountrySelected,
                    countries = defaultCountryCodes,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                AberButton(
                    text = "NEXT",
                    onClick = onNextClick,
                    variant = AberButtonVariant.Dark,
                    enabled = uiState.isFormValid,
                    isLoading = uiState.isSubmitting
                )
            }
        }

        AberNumericKeypad(
            onDigitClick = onDigitClick,
            onBackspaceClick = onBackspaceClick,
            onMicClick = { /* TODO: إدخال صوتي لاحقاً */ }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    SignInScreen(
        uiState = SignInUiState(phoneNumber = "905070017"),
        onCountrySelected = {},
        onDigitClick = {},
        onBackspaceClick = {},
        onNextClick = {}
    )
}