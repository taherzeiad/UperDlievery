package com.newuperapp.Uper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.newuperapp.Uper.ui.theme.AberColor

/**
 * فاصل خطي رفيع، مع دعم إزاحة (inset) من الجانبين — نفس فكرة خيار "0pt 0pt" بالكِت.
 */
@Composable
fun AberDivider(
    modifier: Modifier = Modifier,
    startInset: Dp = 0.dp,
    endInset: Dp = 0.dp,
    thickness: Dp = 1.dp,
    color: Color = AberColor.BorderGray
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = startInset, end = endInset)
            .height(thickness)
            .background(color)
    )
}

@Preview(showBackground = true)
@Composable
private fun AberDividerPreview() {
    AberDivider(startInset = 16.dp)
}