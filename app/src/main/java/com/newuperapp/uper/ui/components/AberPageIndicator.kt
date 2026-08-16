package com.newuperapp.uper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.newuperapp.uper.ui.theme.AberColor

/**
 * مؤشر صفحات دائري (Dots) — يُستخدم بأي Pager بالتطبيق (الـ Onboarding وغيره)
 * بدل ما نعيد كتابة نفس المنطق بكل شاشة.
 */
@Composable
fun AberPageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    dotSize: Dp = 12.dp,
    spacing: Dp = 18.6.dp
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        repeat(pageCount) { index ->
            val selected = index == currentPage
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(if (selected) AberColor.Yellow else AberColor.SurfaceGray)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AberPageIndicatorPreview() {
    AberPageIndicator(pageCount = 3, currentPage = 1)
}