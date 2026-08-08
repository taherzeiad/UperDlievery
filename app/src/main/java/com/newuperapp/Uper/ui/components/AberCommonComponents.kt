package com.newuperapp.Uper.ui.components

import android.system.Os.close
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.auth.CountryCode
import com.newuperapp.Uper.ui.theme.AberColor
import kotlin.io.path.Path

@Composable
fun AberOtpInput(
    code: String, modifier: Modifier = Modifier, length: Int = 4
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(length) { index ->
            val char = code.getOrNull(index)?.toString() ?: ""
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .border(1.dp, AberColor.BorderGray, RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = char,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AberColor.Ink
                )
            }
        }
    }
}

/**
 * لوحة الأرقام السفلية في شاشة تسجيل الدخول.
 *
 * قياسات مأخوذة مباشرة من الصورة (7_Sign_In.jpg):
 * - خلفية القسم: رمادي فاتح جدًا #F7F8FA
 * - كل مفتاح رقم: بطاقة بيضاء بزوايا دائرية، الرقم أسود/كحلي غامق، والحروف
 *   (ABC, DEF...) رمادية صغيرة بمسافة بين الأحرف — المفتاحان 1 و0 بدون حروف
 * - الصف الأخير: الخانة اليسرى فارغة تمامًا (لا توجد بطاقة إطلاقًا)، ثم "0"،
 *   ثم أيقونة Backspace (شكل مخصص بحدود فقط بدون تعبئة، بدون خلفية بطاقة)
 * - أيقونة الميكروفون منفصلة تمامًا عن الشبكة، أسفل يمين اللوحة
 *
 * ألوان النص الرمادي (letters/icons) هنا قيم تقريبية (#9AA0AC) لأنها غير
 * موجودة حاليًا ضمن AberColor في الكود المُشارَك معي — عدّليها لتطابق نظام
 * الألوان الفعلي لديكِ إن كان هناك توكن مخصص لها.
 */

private val KeypadBackground = Color(0xFFF7F8FA)
private val KeypadLetterGray = Color(0xFF9AA0AC)

private data class KeypadKey(val digit: String, val letters: String)

private val numberRows = listOf(
    listOf(KeypadKey("1", ""), KeypadKey("2", "ABC"), KeypadKey("3", "DEF")),
    listOf(KeypadKey("4", "GHI"), KeypadKey("5", "JKL"), KeypadKey("6", "MNO")),
    listOf(KeypadKey("7", "PQRS"), KeypadKey("8", "TUV"), KeypadKey("9", "WXYZ"))
)

@Composable
fun AberNumericKeypad(
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onMicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(KeypadBackground)
            .padding(horizontal = 16.dp, top = 16.dp, bottom = 12.dp)
    ) {
        numberRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { key ->
                    NumberKey(
                        digit = key.digit,
                        letters = key.letters,
                        onClick = { onDigitClick(key.digit) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // الصف الأخير: فراغ - 0 - Backspace
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            NumberKey(
                digit = "0",
                letters = "",
                onClick = { onDigitClick("0") },
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp), contentAlignment = Alignment.Center
            ) {
                BackspaceIcon(onClick = onBackspaceClick)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // أيقونة الميكروفون بمفردها أسفل يمين اللوحة (خارج شبكة الأزرار)
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Outlined.Mic,
                contentDescription = "Voice input",
                tint = KeypadLetterGray,
                modifier = Modifier
                    .size(28.dp)
                    .clickable(onClick = onMicClick)
            )
        }
    }
}

@Composable
private fun NumberKey(
    digit: String, letters: String, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(AberColor.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = digit,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                color = AberColor.Ink
            )
            if (letters.isNotEmpty()) {
                Text(
                    text = letters,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = KeypadLetterGray
                )
            }
        }
    }
}

/**
 * شكل الـ Backspace كما يظهر بالصورة تمامًا: خط حدود فقط بدون تعبئة، بدون
 * خلفية بطاقة بيضاء (على عكس بقية المفاتيح) — مستطيل بزوايا يمنى دائرية
 * ورأس مدبب من الجهة اليسرى.
 */
@Composable
private fun BackspaceIcon(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(width = 46.dp, height = 32.dp)
            .clickable(onClick = onClick)
    ) {
        val w = size.width
        val h = size.height
        val corner = h * 0.3f
        val tipX = w * 0.22f

        val path = Path().apply {
            moveTo(tipX, 0f)
            lineTo(w - corner, 0f)
            quadraticBezierTo(w, 0f, w, corner)
            lineTo(w, h - corner)
            quadraticBezierTo(w, h, w - corner, h)
            lineTo(tipX, h)
            lineTo(0f, h / 2f)
            close()
        }
        drawPath(
            path = path, color = KeypadLetterGray, style = Stroke(width = 3.dp.toPx())
        )
    }
}

@Composable
fun AberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    isPassword: Boolean = false,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier) {
        label?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                color = AberColor.Ink.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            isError = isError,
            keyboardOptions = keyboardOptions,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AberColor.Yellow, unfocusedBorderColor = AberColor.BorderGray
            )
        )
    }
}

@Composable
fun AberPhoneField(
    number: String,
    onNumberChange: (String) -> Unit,
    selectedCountry: CountryCode,
    onCountrySelected: (CountryCode) -> Unit,
    countries: List<CountryCode>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Phone Number",
            fontSize = 14.sp,
            color = AberColor.Ink.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, AberColor.BorderGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            var expanded by remember { mutableStateOf(false) }
            Box {
                Row(
                    modifier = Modifier.clickable { expanded = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = selectedCountry.flag, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = selectedCountry.phoneCode,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text("${country.flag} ${country.name} (${country.phoneCode})") },
                            onClick = {
                                onCountrySelected(country)
                                expanded = false
                            })
                    }
                }
            }
            VerticalDivider(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp))
            BasicTextField(
                value = number,
                onValueChange = onNumberChange,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
            )
        }
    }
}

@Composable
fun AberPhoneDisplayField(
    number: String,
    selectedCountry: CountryCode,
    onCountrySelected: (CountryCode) -> Unit,
    countries: List<CountryCode>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(AberColor.BorderGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            Row(
                modifier = Modifier.clickable { expanded = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedCountry.flag, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = selectedCountry.phoneCode, fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text("${country.flag} ${country.name} (${country.phoneCode})") },
                        onClick = {
                            onCountrySelected(country)
                            expanded = false
                        })
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = number, fontSize = 16.sp, color = AberColor.Ink)
    }
}
