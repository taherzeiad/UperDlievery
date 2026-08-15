package com.newuperapp.Uper.ui.screens.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.R
import com.newuperapp.Uper.ui.components.AberMessageInputBar
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

/**
 * Screen providing a messaging interface between the driver and a passenger or support.
 *
 * @param name The name of the recipient to display in the header.
 * @param onBackClick Navigation callback.
 */
private data class ChatMessage(
    val text: String,
    val isFromMe: Boolean,
    // When set, a centered timestamp divider is rendered above this message —
    // mirrors how the design only shows a divider when a time gap starts.
    val timeLabel: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    name: String,
    onBackClick: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Hello, are you nearby?", isFromMe = false, timeLabel = "Today at 5:03 PM"),
            ChatMessage("I'll be there in a few mins", isFromMe = true),
            ChatMessage("OK, I am waiting at Vinmark Store", isFromMe = false),
            ChatMessage("Sorry, I'm stuck in traffic. Please give me a moment.", isFromMe = true, timeLabel = "5:33 PM"),
        )
    }
    val onSendClick: () -> Unit = {
        val trimmed = messageText.trim()
        if (trimmed.isNotEmpty()) {
            messages.add(ChatMessage(trimmed, isFromMe = true))
            messageText = ""
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = name,
                        style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AberColor.Yellow
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AberColor.White)
            )
        },
        containerColor = AberColor.SurfaceGrayAlt,
        bottomBar = {
            Column {
                // The design shows the same thin divider under the input bar as under the
                // top app bar, and the bar itself sits on a solid white surface rather than
                // the screen's gray background.
                HorizontalDivider(color = AberColor.BorderGray.copy(alpha = 0.3f))
                Row(
                    modifier = Modifier
                        .background(AberColor.White)
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // AberMessageInputBar is text-field-only (no built-in send button), so the
                    // send action lives here as a standalone icon next to it, matching the design.
                    Box(modifier = Modifier.weight(1f)) {
                        AberMessageInputBar(
                            value = messageText,
                            onValueChange = { messageText = it },
                            placeholder = stringResource(R.string.chat_type_message_placeholder)
                        )
                    }
                    IconButton(onClick = onSendClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = AberColor.Yellow
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { message ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    message.timeLabel?.let { label ->
                        TimestampDivider(label)
                        Spacer(Modifier.height(4.dp))
                    }
                    MessageBubble(text = message.text, isFromMe = message.isFromMe)
                }
            }
        }
    }
}

/**
 * Centered, muted timestamp separator shown between message groups.
 */
@Composable
private fun TimestampDivider(label: String) {
    Text(
        text = label,
        style = AberTypography.Caption.copy(color = AberColor.BorderGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        textAlign = TextAlign.Center
    )
}

/**
 * Chat bubble component representing a single message.
 *
 * @param text The message content.
 * @param isFromMe Whether the message was sent by the current user (affects alignment,
 * color, and which side the speech-bubble tail points to).
 */
@Composable
private fun MessageBubble(text: String, isFromMe: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (isFromMe) AberColor.Yellow else Color.White,
            shape = messageBubbleShape(tailOnEnd = isFromMe)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = AberTypography.Subtitle.copy(fontSize = 15.sp)
            )
        }
    }
}

/**
 * Speech-bubble outline: three corners rounded normally, with a small pointed
 * tail replacing the fourth corner — on the end (right) for outgoing messages,
 * on the start (left) for incoming ones, matching the design.
 */
@Composable
private fun messageBubbleShape(tailOnEnd: Boolean): Shape {
    val density = LocalDensity.current
    val cornerPx = with(density) { 16.dp.toPx() }
    val tailWidthPx = with(density) { 8.dp.toPx() }
    val tailHeightPx = with(density) { 9.dp.toPx() }

    return remember(tailOnEnd, cornerPx, tailWidthPx, tailHeightPx) {
        GenericShape { size, _ ->
            val w = size.width
            val h = size.height
            val r = cornerPx
            val tw = tailWidthPx
            val th = tailHeightPx

            if (tailOnEnd) {
                moveTo(0f, r)
                quadraticBezierTo(0f, 0f, r, 0f)
                lineTo(w - r, 0f)
                quadraticBezierTo(w, 0f, w, r)
                lineTo(w, h)
                lineTo(w + tw, h + th)
                lineTo(w - tw, h)
                lineTo(r, h)
                quadraticBezierTo(0f, h, 0f, h - r)
                close()
            } else {
                moveTo(w, r)
                quadraticBezierTo(w, 0f, w - r, 0f)
                lineTo(r, 0f)
                quadraticBezierTo(0f, 0f, 0f, r)
                lineTo(0f, h)
                lineTo(-tw, h + th)
                lineTo(tw, h)
                lineTo(w - r, h)
                quadraticTo(w, h, w, h - r)
                close()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ChatScreen(name = "Esther Berry", onBackClick = {})
}