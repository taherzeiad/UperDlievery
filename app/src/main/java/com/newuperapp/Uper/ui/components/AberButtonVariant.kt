package com.newuperapp.Uper.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.ui.theme.AberColor

enum class AberButtonVariant { Primary, Secondary, Outline, Dark }

/**
 * زر مشترك (Sign Up / Continue / ...) بنفس مواصفات الكِت: زوايا دائرية، ارتفاع ثابت،
 * نص Semibold. استخدميه بكل الشاشات بدل تصميم زر جديد كل مرة.
 */
@Composable
fun AberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AberButtonVariant = AberButtonVariant.Primary,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val shape = RoundedCornerShape(10.dp)
    val height = 45.dp
    val width = 220.dp

    val content: @Composable () -> Unit = {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = if (variant == AberButtonVariant.Primary) AberColor.Ink else AberColor.White,
                strokeWidth = 2.dp
            )
        } else {
            AberButtonLabel(text)
        }
    }

    when (variant) {
        AberButtonVariant.Primary -> Button(
            onClick = onClick,
            enabled = enabled && !isLoading,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = AberColor.Yellow,
                contentColor = AberColor.Ink,
                disabledContainerColor = AberColor.Yellow.copy(alpha = 0.4f),
                disabledContentColor = AberColor.Ink.copy(alpha = 0.4f)
            ),
            modifier = modifier
                .width(width)
                .height(height)
        ) { content() }

        AberButtonVariant.Secondary -> Button(
            onClick = onClick,
            enabled = enabled && !isLoading,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = AberColor.Orange,
                contentColor = AberColor.White,
                disabledContainerColor = AberColor.Orange.copy(alpha = 0.4f),
                disabledContentColor = AberColor.White.copy(alpha = 0.4f)
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(height)
        ) { content() }

        AberButtonVariant.Outline -> OutlinedButton(
            onClick = onClick,
            enabled = enabled && !isLoading,
            shape = shape,
            border = BorderStroke(1.dp, AberColor.BorderGray),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AberColor.Ink, disabledContentColor = AberColor.BorderGray
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(height)
        ) { content() }

        AberButtonVariant.Dark -> Button(
            onClick = onClick,
            enabled = enabled && !isLoading,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = AberColor.Ink,
                contentColor = AberColor.White,
                disabledContainerColor = AberColor.Ink.copy(alpha = 0.4f),
                disabledContentColor = AberColor.White.copy(alpha = 0.4f)
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(height)
        ) { content() }
    }
}

@Composable
private fun AberButtonLabel(text: String) {
    Text(text = text, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
}

@Preview(showBackground = true)
@Composable
private fun AberButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AberButton(text = "SIGN UP", onClick = {}, variant = AberButtonVariant.Primary)
        AberButton(text = "CONTINUE", onClick = {}, variant = AberButtonVariant.Secondary)
        AberButton(text = "CANCEL", onClick = {}, variant = AberButtonVariant.Outline)
        AberButton(text = "DARK", onClick = {}, variant = AberButtonVariant.Dark)
        AberButton(text = "LOADING", onClick = {}, isLoading = true)
        AberButton(text = "DISABLED", onClick = {}, enabled = false)
    }
}
