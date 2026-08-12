package com.newuperapp.Uper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(28.dp)) {
        repeat(length) { index ->
            val filled = index < code.length
            Column(modifier = Modifier.width(44.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.height(40.dp), contentAlignment = Alignment.Center) {
                    if (filled) {
                        Text(code[index].toString(), style = AberTypography.OtpDigit)
                    } else {
                        Box(modifier = Modifier.size(9.dp).background(AberColor.Ink, CircleShape))
                    }
                }
                Spacer(Modifier.height(6.dp))
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(AberColor.BorderGray))
            }
        }
    }
}
