package com.newuperapp.Uper.ui.screens.home.invite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.Uper.domain.model.Contact
import com.newuperapp.Uper.ui.components.AberButton
import com.newuperapp.Uper.ui.components.AberTextField
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteFriendsScreen(
    onBackClick: () -> Unit
) {
    var showContacts by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Invite Friends", style = AberTypography.ScreenTitle.copy(fontSize = 20.sp)) },
                navigationIcon = {
                    IconButton(onClick = if (showContacts) { { showContacts = false } } else onBackClick) {
                        Icon(if (showContacts) Icons.Default.ArrowBack else Icons.Default.Menu, contentDescription = "Back", tint = AberColor.Yellow)
                    }
                },
                actions = {
                    if (showContacts) {
                        TextButton(onClick = { /* Handle Next */ }) {
                            Text("Next", color = AberColor.Orange, style = AberTypography.Subtitle.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AberColor.White)
            )
        },
        containerColor = AberColor.White
    ) { padding ->
        if (!showContacts) {
            InviteReferralContent(padding) { showContacts = true }
        } else {
            InviteContactsContent(padding)
        }
    }
}

@Composable
private fun InviteReferralContent(padding: PaddingValues, onInviteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(AberColor.Yellow),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(100.dp), tint = AberColor.Ink)
        }
        
        Spacer(Modifier.height(40.dp))
        
        Text("Invite Friends", style = AberTypography.HeroTitleBold)
        Text("Earn up to $150 a day", style = AberTypography.HeroTitle.copy(fontSize = 28.sp))
        
        Spacer(Modifier.height(24.dp))
        
        Text(
            "When your friend sign up with your referral code, you can receive up to $150 a day.",
            style = AberTypography.Subtitle.copy(color = AberColor.Ink.copy(alpha = 0.6f)),
            textAlign = TextAlign.Center
        )
        
        Spacer(Modifier.height(60.dp))
        
        Text("SHARE YOUR INVITE CODE", style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray))
        
        Spacer(Modifier.height(12.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AberColor.SurfaceGray),
            contentAlignment = Alignment.Center
        ) {
            Text("0905070017", style = AberTypography.CardTitle)
        }
        
        Spacer(Modifier.height(24.dp))
        
        AberButton(text = "INVITE", onClick = onInviteClick)
    }
}

@Composable
private fun InviteContactsContent(padding: PaddingValues) {
    Column(modifier = Modifier.padding(padding)) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.Mic, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = AberColor.SurfaceGray,
                focusedContainerColor = AberColor.SurfaceGray,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            )
        )

        val contacts = listOf(
            Contact("1", "Jackson Daniel", null, 5),
            Contact("2", "Nellie Scott", null, 5),
            Contact("3", "Shane Morales", null, 5),
            Contact("4", "Sophie Bell", null, 5),
            Contact("5", "Rhoda Palmer", null, 5)
        )

        LazyColumn {
            items(contacts) { contact ->
                ContactItem(contact)
            }
        }
    }
}

@Composable
private fun ContactItem(contact: Contact) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(AberColor.SurfaceGray))
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(contact.name, style = AberTypography.CardTitle.copy(fontSize = 17.sp))
            Text("${contact.mutualFriendsCount} mutual friends", style = AberTypography.Caption)
        }
        RadioButton(selected = contact.isSelected, onClick = { /* Toggle */ }, colors = RadioButtonDefaults.colors(selectedColor = AberColor.Orange))
    }
}
