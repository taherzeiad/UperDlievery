package com.newuperapp.Uper.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.R
import com.newuperapp.Uper.domain.auth.CountryCode
import com.newuperapp.Uper.domain.auth.defaultCountryCodes
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberButtonVariant
import com.newuperapp.Uper.ui.components.AberPhoneField
import com.newuperapp.Uper.ui.components.AberTextField
import com.newuperapp.Uper.ui.theme.AberColor

/**
 * نقطة دخول شاشة التسجيل (تربط بين الـ ViewModel والـ Navigation).
 */
@Composable
fun SignUpRoute(
    onNavigateToVerification: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SignUpEvent.NavigateToVerification -> onNavigateToVerification()
                SignUpEvent.NavigateToSignIn -> onNavigateToSignIn()
            }
        }
    }

    SignUpScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onCountrySelected = viewModel::onCountrySelected,
        onSignUpClick = viewModel::onSignUpClick,
        onSignInClick = viewModel::onSignInClick
    )
}

/**
 * الشاشة الفعلية: بطاقة عائمة بزوايا دائرية على خلفية رمادية فاتحة،
 * تطابق تصميم الصورة المرجعية بدقة (الهيدر الأصفر + الحقول + الزر داخل البطاقة،
 * ورابط "Sign In" خارجها على الخلفية).
 */
@Composable
fun SignUpScreen(
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onCountrySelected: (CountryCode) -> Unit,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AberColor.SurfaceGrayAlt)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // البطاقة العائمة: هيدر أصفر + حقول + زر، بزوايا دائرية وظل خفيف
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = AberColor.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            // الهيدر الأصفر مع رسمة أفق المدينة (3 طبقات حقيقية بنفس ترتيب Figma)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(AberColor.Yellow)
            ) {
                // الطبقة الخلفية: أفق كامل العرض بلون ذهبي متوسط (Fill_1)
                Image(
                    painter = painterResource(id = R.drawable.fill_1),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                )

                // الطبقتان الأماميتان: تتجاوران تمامًا بنفس نسبة أبعادهما الأصلية (192:183)
                // بدون أي فراغ أو تراكب بينهما، مطابقةً للتصميم الأصلي
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fill_1_copy_2),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.weight(192f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.fill_1_copy),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.weight(183f)
                    )
                }

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Sign up ") }
                        append("with\nemail and phone\nnumber")
                    },
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    color = AberColor.Ink,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 24.dp, top = 32.dp, end = 16.dp)
                )
            }

            // الحقول والزر
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AberTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    placeholder = "name@example.com",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = uiState.errorMessage != null,
                    modifier = Modifier.fillMaxWidth()
                )

                AberPhoneField(
                    number = uiState.phoneNumber,
                    onNumberChange = onPhoneNumberChange,
                    selectedCountry = uiState.country,
                    onCountrySelected = onCountrySelected,
                    countries = defaultCountryCodes,
                    modifier = Modifier.fillMaxWidth()
                )

                uiState.errorMessage?.let { message ->
                    Text(text = message, fontSize = 13.sp, color = AberColor.Orange)
                }

                Spacer(modifier = Modifier.height(4.dp))

                AberButton(
                    text = "SIGN UP",
                    onClick = onSignUpClick,
                    variant = AberButtonVariant.Dark,
                    isLoading = uiState.isSubmitting,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // رابط "لديك حساب بالفعل؟ سجّل الدخول" خارج البطاقة، على الخلفية الرمادية
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(modifier = Modifier.padding(8.dp)) {
                Text(text = "Already have an account? ", fontSize = 15.sp, color = AberColor.Ink)
                Text(
                    text = "Sign In",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AberColor.Ink,
                    modifier = Modifier.clickable(onClick = onSignInClick)
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
        onPhoneNumberChange = {},
        onCountrySelected = {},
        onSignUpClick = {},
        onSignInClick = {},
    )
}
