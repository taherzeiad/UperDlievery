package com.newuperapp.Uper.ui.components

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.auth.CountryCode
import com.newuperapp.Uper.ui.theme.AberColor

@Composable
fun AberOtpInput(
    code: String,
    modifier: Modifier = Modifier,
    length: Int = 4
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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

@Composable
fun AberNumericKeypad(
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onMicClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("mic", "0", "delete")
        )

        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .clickable(enabled = key.isNotEmpty()) {
                                when (key) {
                                    "delete" -> onBackspaceClick()
                                    "mic" -> onMicClick()
                                    else -> onDigitClick(key)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        when (key) {
                            "delete" -> Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = "Delete", tint = AberColor.Ink)
                            "mic" -> Icon(Icons.Default.Mic, contentDescription = "Mic", tint = AberColor.Yellow)
                            else -> Text(
                                text = key,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                                color = AberColor.Ink
                            )
                        }
                    }
                }
            }
        }
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
                focusedBorderColor = AberColor.Yellow,
                unfocusedBorderColor = AberColor.BorderGray
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
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var expanded by remember { mutableStateOf(false) }
            Box {
                Row(
                    modifier = Modifier.clickable { expanded = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = selectedCountry.flag, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = selectedCountry.phoneCode, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text("${country.flag} ${country.name} (${country.phoneCode})") },
                            onClick = {
                                onCountrySelected(country)
                                expanded = false
                            }
                        )
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
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            Row(
                modifier = Modifier.clickable { expanded = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedCountry.flag, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = selectedCountry.phoneCode, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text("${country.flag} ${country.name} (${country.phoneCode})") },
                        onClick = {
                            onCountrySelected(country)
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = number, fontSize = 16.sp, color = AberColor.Ink)
    }
}
