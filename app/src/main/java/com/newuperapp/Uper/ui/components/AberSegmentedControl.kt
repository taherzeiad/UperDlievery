package com.newuperapp.Uper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.ui.theme.AberColor

/**
 * Segmented Control بنفس منطق كِت "As Navigation Bar"، لكن بألوان أبير (Yellow/Ink)
 * بدل الأزرق الافتراضي.
 */
@Composable
fun AberSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(shape)
            .border(1.dp, AberColor.BorderGray, shape)
            .background(AberColor.White)
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (selected) AberColor.Yellow else AberColor.White)
                    .clickable { onSelectedChange(index) }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selected) AberColor.Ink else AberColor.BorderGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AberSegmentedControlPreview() {
    var selected by remember { mutableStateOf(0) }
    AberSegmentedControl(
        options = listOf("Label", "Label"),
        selectedIndex = selected,
        onSelectedChange = { selected = it },
        modifier = Modifier.padding(16.dp)
    )
}