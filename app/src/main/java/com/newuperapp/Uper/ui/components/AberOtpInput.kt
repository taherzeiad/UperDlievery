package com.newuperapp.Uper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/** Filled slots show the digit, empty slots show a dot placeholder, each above an underline. */
@Composable
fun AberOtpInput(
    code: String,
    length: Int = 4,
    modifier: Modifier = Modifier
) {
    // The reference design spreads the 4 slots across the FULL row width, not a narrow
    // centered block: measuring the underline row against the screen frame showed it
    // spanning edge-to-edge with the same margins as the title/subtitle text above it.
    // Each slot now takes an equal share of the row via weight(1f) instead of a fixed
    // 44.dp width, so the group stretches to match.
    Row(modifier = modifier.fillMaxWidth()) {
        repeat(length) { index ->
            val filled = index < code.length
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.height(40.dp), contentAlignment = Alignment.Center) {
                    if (filled) {
                        Text(code[index].toString(), style = AberTypography.OtpDigit)
                    } else {
                        Box(modifier = Modifier
                            .size(9.dp)
                            .background(AberColor.Ink, CircleShape))
                    }
                }
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(AberColor.BorderGray)
                )
            }
        }
    }
}