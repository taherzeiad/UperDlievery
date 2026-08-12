package com.newuperapp.Uper.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/** Generic single-line outlined field styled to the Aber kit (rounded, gray border, yellow focus). */
@Composable
fun AberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, style = AberTypography.FieldHint) },
        singleLine = true,
        isError = isError,
        supportingText = supportingText?.let { { Text(it, style = AberTypography.Caption.copy(color = AberColor.Orange)) } },
        textStyle = AberTypography.semibody17(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AberColor.Yellow,
            unfocusedBorderColor = AberColor.BorderGray,
            errorBorderColor = AberColor.Orange,
            focusedContainerColor = AberColor.White,
            unfocusedContainerColor = AberColor.White
        ),
        modifier = modifier.fillMaxWidth().height(56.dp)
    )
}
