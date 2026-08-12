package com.newuperapp.Uper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.ui.theme.AberColor

private data class DialKey(val digit: String, val letters: String)

private val rows = listOf(
    listOf(DialKey("1", ""), DialKey("2", "ABC"), DialKey("3", "DEF")),
    listOf(DialKey("4", "GHI"), DialKey("5", "JKL"), DialKey("6", "MNO")),
    listOf(DialKey("7", "PQRS"), DialKey("8", "TUV"), DialKey("9", "WXYZ"))
)

/** Custom Dial-pad used by Sign In (phone) and Phone Verify (OTP) screens. */
@Composable
fun AberNumericKeypad(
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit,
    onMicClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AberColor.SurfaceGray)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { key ->
                    DialButton(key.digit, key.letters, onClick = { onDigit(key.digit) }, modifier = Modifier.weight(1f))
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))
            DialButton("0", "", onClick = { onDigit("0") }, modifier = Modifier.weight(1f))
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = AberColor.IconMuted,
                    modifier = Modifier.size(26.dp).clickable(onClick = onBackspace)
                )
            }
        }
        if (onMicClick != null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice input",
                    tint = AberColor.IconMuted,
                    modifier = Modifier.padding(end = 8.dp).size(22.dp).clickable(onClick = onMicClick)
                )
            }
        }
    }
}

@Composable
private fun DialButton(digit: String, letters: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(62.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AberColor.White)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(digit, fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = AberColor.Ink)
        if (letters.isNotEmpty()) {
            Text(letters, fontSize = 9.sp, color = AberColor.IconMuted, letterSpacing = 1.sp)
        }
    }
}
