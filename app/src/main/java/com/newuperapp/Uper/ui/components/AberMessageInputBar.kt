package com.newuperapp.Uper.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.ui.theme.AberColor

/**
 * شريط إدخال رسالة (شات/دعم) — نفس شكل "Type a message" بالكِت، بألوان أبير.
 */
@Composable
fun AberMessageInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onAttachClick: (() -> Unit)? = null,
    placeholder: String = "Type a message...",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AberColor.White, shape)
            .border(1.dp, AberColor.BorderGray, shape)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(text = placeholder, fontSize = 15.sp, color = AberColor.BorderGray)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 15.sp, color = AberColor.Ink),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (onAttachClick != null) {
            IconButton(onClick = onAttachClick) {
                Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = "Attach",
                    tint = AberColor.BorderGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AberMessageInputBarPreview() {
    var text by remember { mutableStateOf("") }
    AberMessageInputBar(
        value = text,
        onValueChange = { text = it },
        onAttachClick = {},
        modifier = Modifier.padding(16.dp)
    )
}