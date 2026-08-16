package com.newuperapp.uper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

enum class AberButtonStyle { Primary, Secondary, Dark, Outline }

@Composable
fun AberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AberButtonStyle = AberButtonStyle.Dark,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val height = 56.dp
    val shape = RoundedCornerShape(14.dp)

    if (style == AberButtonStyle.Outline) {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled && !isLoading,
            shape = shape,
            modifier = modifier.fillMaxWidth().height(height),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AberColor.Ink)
        ) {
            ButtonContent(text, isLoading, AberColor.Ink)
        }
        return
    }

    val (container, content) = when (style) {
        AberButtonStyle.Primary -> AberColor.Yellow to AberColor.Ink
        AberButtonStyle.Secondary -> AberColor.Orange to AberColor.White
        AberButtonStyle.Dark -> AberColor.Ink to AberColor.White
        AberButtonStyle.Outline -> AberColor.White to AberColor.Ink // unreachable
    }

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = shape,
        modifier = modifier.fillMaxWidth().height(height),
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = container.copy(alpha = 0.4f)
        )
    ) {
        ButtonContent(text, isLoading, content)
    }
}

@Composable
private fun ButtonContent(text: String, isLoading: Boolean, contentColor: androidx.compose.ui.graphics.Color) {
    Box(contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = contentColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Text(text.uppercase(), style = AberTypography.semibody17(contentColor))
        }
    }
}
