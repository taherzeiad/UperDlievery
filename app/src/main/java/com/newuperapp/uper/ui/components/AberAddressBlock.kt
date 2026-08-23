package com.newuperapp.uper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

/**
 * Reusable Address block with label and address text.
 */
@Composable
fun AberAddressBlock(
    label: String,
    address: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(
            text = label.uppercase(),
            style = AberTypography.SectionLabel.copy(
                fontSize = 11.sp,
                color = AberColor.BorderGray
            )
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = address,
            style = AberTypography.semibody17(AberColor.Ink)
        )
    }
}
