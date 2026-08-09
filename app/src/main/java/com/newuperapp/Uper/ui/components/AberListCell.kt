package com.newuperapp.Uper.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.ui.theme.AberColor

/**
 * خلية جدول عامة (Title / Subtitle + سهم اختياري) — تُستخدم بالإعدادات، السجل، إلخ.
 * showChevron = false بتعطي شكل "Cell_Edited" (بدون سهم، مثلاً بوضع التحرير).
 */
@Composable
fun AberListCell(
    title: String,
    subtitle: String? = null,
    showChevron: Boolean = true,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 17.sp, color = AberColor.Ink, fontWeight = FontWeight.Normal)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 17.sp,
                    color = AberColor.BorderGray,
                    modifier = Modifier.padding(end = if (showChevron) 4.dp else 0.dp)
                )
            }
            if (showChevron) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AberColor.BorderGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AberListCellPreview() {
    Column {
        AberListCell(title = "Title", subtitle = "Subtitle", onClick = {})
        AberDivider()
        AberListCell(title = "Title", subtitle = "Subtitle", showChevron = false)
    }
}