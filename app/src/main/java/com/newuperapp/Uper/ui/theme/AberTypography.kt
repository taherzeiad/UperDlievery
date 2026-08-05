package com.newuperapp.Uper.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * أنماط النصوص الجاهزة من الكِت (Text/Black/Semibold/17pt و Text/White/Semibold/17pt).
 * استخدميها زي ما هي بدل تكرار fontSize/fontWeight بكل شاشة.
 */
object AberTypography {
    val Semibold17Black = TextStyle(
        color = AberColor.Ink,
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold
    )

    val Semibold17White = TextStyle(
        color = AberColor.White,
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold
    )
}