package com.newuperapp.Uper.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.newuperapp.Uper.ui.theme.AberColor

@Composable
fun AberDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = AberColor.BorderGray.copy(alpha = 0.4f)
    )
}
