package com.newuperapp.Uper.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Aber Driver – لوحة الألوان الأساسية.
 * كل الكمبوننتس المشتركة (Button, Cell, Divider, ...) بتسحب ألوانها من هنا فقط،
 * فإذا تغيّرت الهوية البصرية مستقبلاً بتعدّل بمكان واحد بس.
 */
object AberColor {
    val Yellow        = Color(0xFFFFD428) // أساسي (Primary) – أزرار وتمييز
    val Orange        = Color(0xFFFF8900) // ثانوي (Secondary) – تنبيه/تمييز إضافي
    val White         = Color(0xFFFFFFFF) // خلفيات بطاقات، نص على خلفية غامقة
    val Ink           = Color(0xFF242A37) // نص أساسي غامق / خلفية غامقة (Dark surface)
    val SurfaceGray   = Color(0xFFF1F2F6) // خلفية ثانوية للشاشات والكروت
    val SurfaceGrayAlt = Color(0xFFF7F8FA) // خلفية فاتحة جداً لحقول الإدخال والأقسام
    val BorderGray    = Color(0xFFBEC2CE) // حدود، فواصل، نص ثانوي (subtitle) ومعطّل (disabled)
    val TagBackground = Color(0xFFEBEFF2) // خلفية الشارات (Tags)
    val RouteBlue     = Color(0xFF3858F6) // لون مسار الخريطة
    val IconMuted     = Color(0xFF9AA0AC) // لون الأيقونات غير النشطة
    val Danger        = Color(0xFFE22D2D) // لون التنبيه/الخطأ
}

private val AberShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

private val AberColorScheme = lightColorScheme(
    primary = AberColor.Yellow,
    onPrimary = AberColor.Ink,
    secondary = AberColor.Orange,
    onSecondary = AberColor.White,
    background = AberColor.SurfaceGrayAlt,
    onBackground = AberColor.Ink,
    surface = AberColor.White,
    onSurface = AberColor.Ink,
    error = AberColor.Danger
)

@Composable
fun AberTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AberColorScheme,
        shapes = AberShapes,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    AberTheme(content = content)
}
