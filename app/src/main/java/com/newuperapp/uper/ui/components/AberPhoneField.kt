package com.newuperapp.uper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

/**
 * Editable phone row — flag + dial code + free-typed number. Used on Sign Up
 * where a system keyboard is fine.
 */
@Composable
fun AberPhoneField(
    countryFlagEmoji: String,
    dialCode: String,
    value: String,
    onValueChange: (String) -> Unit,
    onCountryClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    PhoneFieldShell(countryFlagEmoji, dialCode, onCountryClick, modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = AberTypography.semibody17(),
            singleLine = true,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Phone),
            decorationBox = { inner ->
                if (value.isEmpty()) Text("Mobile number", style = AberTypography.FieldHint)
                inner()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Read-only phone row driven by the custom [AberNumericKeypad] (Sign In screen),
 * with a clear ("x") action instead of a system IME.
 */
@Composable
fun AberPhoneDisplayField(
    countryFlagEmoji: String,
    dialCode: String,
    value: String,
    onClearClick: () -> Unit,
    onCountryClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    PhoneFieldShell(countryFlagEmoji, dialCode, onCountryClick, modifier, trailing = {
        if (value.isNotEmpty()) {
            IconButton(onClick = onClearClick) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(AberColor.BorderGray.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("✕", color = AberColor.IconMuted, fontSize = 12.sp) }
            }
        }
    }) {
        Text(
            text = value.ifEmpty { "" },
            style = AberTypography.semibody17(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PhoneFieldShell(
    countryFlagEmoji: String,
    dialCode: String,
    onCountryClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, AberColor.BorderGray, RoundedCornerShape(14.dp))
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onCountryClick)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(countryFlagEmoji, fontSize = 18.sp)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = AberColor.Ink)
        }
        Box(modifier = Modifier.width(1.dp).height(24.dp).background(AberColor.BorderGray))
        Text(
            dialCode,
            style = AberTypography.semibody17().copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
            modifier = Modifier.padding(start = 12.dp, end = 8.dp)
        )
        Box(modifier = Modifier.weight(1f)) { content() }
        trailing?.invoke() ?: Spacer(Modifier.width(12.dp))
    }
}
