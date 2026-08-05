package com.newuperapp.Uper.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.ui.theme.AberColor

/**
 * رابط نصي بسيط قابل للنقر (Skip / Skip for now / ...) بلون موحّد بكل الشاشات،
 * بدون ripple عشان يضل خفيف بصرياً متل النص العادي.
 */
@Composable
fun AberTextLink(
    text: String, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = AberColor.BorderGray,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun AberTextLinkPreview() {
    AberTextLink(text = "Skip", onClick = {})
}