package com.newuperapp.Uper.ui.screens.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.ui.components.AberMessageInputBar
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    name: String,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(name, style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AberColor.Yellow)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AberColor.White)
            )
        },
        bottomBar = {
            // Simplified input bar for now
            Surface(tonalElevation = 2.dp, color = AberColor.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message...") },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = AberColor.SurfaceGray,
                            focusedContainerColor = AberColor.SurfaceGray,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )
                    Spacer(Modifier.width(12.dp))
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = AberColor.Yellow)
                    }
                }
            }
        },
        containerColor = AberColor.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Today at 5:03 PM",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = AberTypography.Caption.copy(fontSize = 12.sp)
                )
            }
            
            item {
                MessageBubble(
                    text = "Hello, are you nearby?",
                    isFromMe = false
                )
            }
            
            item {
                MessageBubble(
                    text = "I'll be there in a few mins",
                    isFromMe = true
                )
            }
            
            item {
                MessageBubble(
                    text = "OK, I am waiting at Vinmark Store",
                    isFromMe = false
                )
            }
            
            item {
                Text(
                    "5:33 PM",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = AberTypography.Caption.copy(fontSize = 12.sp)
                )
            }
            
            item {
                MessageBubble(
                    text = "Sorry, I'm stuck in traffic. Please give me a moment.",
                    isFromMe = true
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(text: String, isFromMe: Boolean) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isFromMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (isFromMe) 12.dp else 0.dp,
                        bottomEnd = if (isFromMe) 0.dp else 12.dp
                    )
                )
                .background(if (isFromMe) AberColor.Yellow else AberColor.SurfaceGray)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = text,
                style = AberTypography.Subtitle.copy(
                    fontSize = 16.sp,
                    color = AberColor.Ink
                )
            )
        }
    }
}
