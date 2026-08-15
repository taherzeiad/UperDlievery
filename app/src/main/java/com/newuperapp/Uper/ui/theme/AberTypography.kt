package com.newuperapp.Uper.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.ui.graphics.Color

/**
 * أنماط النصوص الجاهزة من الكِت (Text/Black/Semibold/17pt و Text/White/Semibold/17pt).
 * استخدميها زي ما هي بدل تكرار fontSize/fontWeight بكل شاشة.
 */
object AberTypography {
    fun semibody17(color: Color = AberColor.Ink) = TextStyle(
        color = color, fontSize = 17.sp, fontWeight = FontWeight.SemiBold
    )

    val CardTitle = TextStyle(
        color = AberColor.Ink, fontSize = 20.sp, fontWeight = FontWeight.Bold
    )

    val ScreenTitle = TextStyle(
        color = AberColor.Ink, fontSize = 32.sp, fontWeight = FontWeight.Bold
    )

    val Subtitle = TextStyle(
        color = AberColor.Ink, fontSize = 17.sp, fontWeight = FontWeight.Normal
    )

    val HeroTitle = TextStyle(
        color = AberColor.Ink, fontSize = 32.sp, fontWeight = FontWeight.Normal
    )

    val HeroTitleBold = TextStyle(
        color = AberColor.Ink, fontSize = 32.sp, fontWeight = FontWeight.Bold
    )

    val PriceTag = TextStyle(
        color = AberColor.Ink, fontSize = 20.sp, fontWeight = FontWeight.Bold
    )

    val Caption = TextStyle(
        color = AberColor.BorderGray, fontSize = 15.sp, fontWeight = FontWeight.Normal
    )

    val SectionLabel = TextStyle(
        color = AberColor.BorderGray, fontSize = 11.sp, fontWeight = FontWeight.Bold
    )

    val StatValue = TextStyle(
        color = AberColor.Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold
    )

    val StatLabel = TextStyle(
        color = AberColor.Ink.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold
    )

    val OtpDigit = TextStyle(
        color = AberColor.Ink, fontSize = 32.sp, fontWeight = FontWeight.Bold
    )

    val FieldHint = TextStyle(
        color = AberColor.BorderGray, fontSize = 17.sp, fontWeight = FontWeight.Normal
    )

    val Semibold17Black = TextStyle(
        color = AberColor.Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold
    )

    val Semibold17White = TextStyle(
        color = AberColor.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold
    )
}