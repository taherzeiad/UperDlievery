package com.newuperapp.Uper.ui.screens.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    name: String,
    onBackClick: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }

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
            Box(modifier = Modifier.padding(20.dp)) {
                AberMessageInputBar(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = stringResource(R.string.chat_type_message_placeholder)
                )
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
            item {
                MessageBubble(
                    text = "Hello! I am waiting at the main entrance.",
                    isFromMe = false
                )
            }
            item {
                MessageBubble(
                    text = "Got it. I'll be there in 2 minutes.",
                    isFromMe = true
                )
            }
            item {
                MessageBubble(
                    text = "Great, see you soon!",
                    isFromMe = false
                )
            }
        }
    }
}

/**
 * Chat bubble component representing a single message.
 *
 * @param text The message content.
 * @param isFromMe Whether the message was sent by the current user (affects alignment and color).
 */
@Composable
private fun MessageBubble(text: String, isFromMe: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (isFromMe) AberColor.Yellow else Color.White,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isFromMe) 16.dp else 0.dp,
                bottomEnd = if (isFromMe) 0.dp else 16.dp
            )
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = AberTypography.Subtitle.copy(fontSize = 15.sp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ChatScreen(name = "Esther Berry", onBackClick = {})
}
