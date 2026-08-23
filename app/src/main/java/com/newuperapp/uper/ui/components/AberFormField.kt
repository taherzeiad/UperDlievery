package com.newuperapp.uper.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

@Composable
fun AberFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    placeholder: String = "",
    readOnly: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = {
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = AberColor.BorderGray
        )
    }
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray)
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            placeholder = {
                Text(
                    text = placeholder,
                    style = AberTypography.Subtitle.copy(color = AberColor.BorderGray)
                )
            },
            trailingIcon = trailingIcon,
            textStyle = AberTypography.semibody17(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}
