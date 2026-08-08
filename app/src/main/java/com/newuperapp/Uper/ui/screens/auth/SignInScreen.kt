package com.newuperapp.Uper.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.Uper.R
import com.newuperapp.Uper.domain.auth.CountryCode
import com.newuperapp.Uper.domain.auth.defaultCountryCodes
import com.newuperapp.Uper.ui.components.AberNumericKeypad
import com.newuperapp.Uper.ui.components.AberPhoneDisplayField
import com.newuperapp.Uper.ui.theme.AberColor

/**
 * نقطة دخول الشاشة (تربط الـ ViewModel مع الـ Navigation).
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
 * شاشة تسجيل الدخول برقم الهاتف.
 *
 * القياسات أدناه مأخوذة بالقياس المباشر من ملف التصميم (7_Sign_In.jpg، 1125×2436px @3x):
 * - الجزء الأصفر المرئي فوق البطاقة البيضاء ≈ 170dp (Box بارتفاع 220dp مع إزاحة سالبة 50dp)
 * - قسم لوحة الأرقام (الرمادي الفاتح) يبدأ عند ≈ 470dp من أعلى الشاشة
 * - هامش المحتوى الأفقي داخل البطاقة البيضاء ≈ 32dp من كل جهة
 * - عنوان "Login with your phone number" بحجم 32sp / ارتفاع سطر 40sp
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
            // الخلفية الصفراء أعلى الشاشة
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(AberColor.Yellow)
            ) {
                // خط أفق المدينة أسفل الجزء الأصفر (~101dp من ارتفاع الصورة التوضيحية)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(101.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fill_1),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxSize()
                    )

                    // الطبقتان الأفتح فالأغمق فوق بعضهما لإعطاء عمق للمدينة
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.fill_1_copy_2),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .weight(192f)
                                .fillMaxHeight()
                        )
                        Image(
                            painter = painterResource(id = R.drawable.fill_1_copy),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .weight(183f)
                                .fillMaxHeight()
                        )
                    }
                }
            }

            // البطاقة البيضاء السفلية (تتراكب فوق الأصفر بمقدار 50dp لتطابق القياس الفعلي)
            Column(
                modifier = Modifier
                    .offset(y = (-50).dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(AberColor.White)
                    .padding(horizontal = 32.dp, vertical = 28.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Login ") }
                        append("with your phone number")
                    },
                    fontSize = 32.sp,
                    lineHeight = 40.sp,
                    color = AberColor.Ink
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ملاحظة: عناصر هذا الحقل (العلم، السهم، الخط الفاصل، مؤشر الكتابة الأصفر،
                // وزر المسح الدائري X) موجودة داخل AberPhoneDisplayField نفسه ولم تُشارَك
                // معي، لذا لم أستطع تعديلها هنا — راجع القائمة في رسالتي لضبطها بدقة.
                AberPhoneDisplayField(
                    number = uiState.phoneNumber,
                    selectedCountry = uiState.country,
                    onCountrySelected = onCountrySelected,
                    countries = defaultCountryCodes,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                SignInNextButton(
                    text = "NEXT",
                    onClick = onNextClick,
                    enabled = uiState.isFormValid,
                    isLoading = uiState.isSubmitting
                )
            }
        }

        AberNumericKeypad(
            onDigitClick = onDigitClick,
            onBackspaceClick = onBackspaceClick,
            onMicClick = { /* TODO: تفعيل الإدخال الصوتي */ }
        )
    }
}

/**
 * زر "NEXT" بنفس تصميم الصورة تمامًا: خلفية كحلية داكنة (AberColor.Ink) مع
 * "شارة" أغمق وأكثر زرقة خلف النص مباشرة فقط — هذا تفصيل حقيقي في التصميم
 * (تحققت منه بتكبير الصورة، وليس ضوضاء ضغط JPEG) وليس موجودًا حاليًا في
 * AberButton، لذلك بُني هنا محليًا بدل استدعاء AberButton مباشرة.
 * اقتراح: أضيفي variant جديد لـ AberButton (مثلاً AberButtonVariant.DarkBadged)
 * ليصبح هذا الشكل قابلاً لإعادة الاستخدام في شاشات أخرى.
 */
@Composable
private fun SignInNextButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val badgeNavy = Color(0xFF0B1F44) // لون الشارة الأغمق خلف النص، مقاس من الصورة مباشرة

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (enabled) AberColor.Ink else AberColor.Ink.copy(alpha = 0.4f))
            .clickable(enabled = enabled && !isLoading, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = AberColor.White,
                strokeWidth = 2.dp,
                modifier = Modifier.height(20.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(badgeNavy)
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text(
                    text = text,
                    color = AberColor.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
            }
        }
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