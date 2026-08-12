package com.newuperapp.Uper.ui.screens.home.invite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.newuperapp.Uper.ui.theme.AberColor
import com.newuperapp.Uper.ui.theme.AberTypography

data class Contact(val id: String, val name: String, val mutualFriendsCount: Int)

/**
 * Screen allowing drivers to share their referral code and invite contacts to join.
 *
 * @param onBackClick Navigation callback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteFriendsScreen(
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.invite_friends_title), 
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
                actions = {
                    TextButton(onClick = {}) {
                        Text(
                            text = stringResource(R.string.invite_next_cta), 
                            color = AberColor.Orange, 
                            style = AberTypography.Subtitle.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AberColor.White)
            )
        },
        containerColor = AberColor.White
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = AberColor.Yellow,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = AberColor.Yellow
                    )
                }
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text(text = "Referral", modifier = Modifier.padding(16.dp), style = AberTypography.Subtitle.copy(fontWeight = FontWeight.Bold))
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text(text = "Contact", modifier = Modifier.padding(16.dp), style = AberTypography.Subtitle.copy(fontWeight = FontWeight.Bold))
                }
            }

            if (selectedTab == 0) {
                InviteReferralContent(padding)
            } else {
                InviteContactsContent(padding)
            }
        }
    }
}

@Composable
private fun InviteReferralContent(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Text(text = stringResource(R.string.invite_friends_title), style = AberTypography.HeroTitleBold)
        Text(text = stringResource(R.string.invite_earn_title), style = AberTypography.HeroTitle.copy(fontSize = 28.sp))
        
        Spacer(Modifier.height(60.dp))
        
        Text(
            text = stringResource(R.string.invite_share_code_label),
            style = AberTypography.SectionLabel.copy(color = AberColor.BorderGray, fontSize = 12.sp)
        )
        
        Spacer(Modifier.height(16.dp))
        
        Surface(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(12.dp),
            color = AberColor.SurfaceGray
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "0905070017", style = AberTypography.CardTitle)
            }
        }
    }
}

@Composable
private fun InviteContactsContent(padding: PaddingValues) {
    val contacts = listOf(
        Contact("1", "Avery Weaver", 12),
        Contact("2", "Steve Bowen", 5),
        Contact("3", "Lula Briggs", 8)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            placeholder = { Text(text = stringResource(R.string.invite_search_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AberColor.SurfaceGray,
                unfocusedContainerColor = AberColor.SurfaceGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        LazyColumn {
            items(contacts) { contact ->
                ContactItem(contact)
                HorizontalDivider(color = AberColor.SurfaceGray, modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
    }
}

@Composable
private fun ContactItem(contact: Contact) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(AberColor.SurfaceGray))
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = contact.name, style = AberTypography.CardTitle.copy(fontSize = 16.sp))
            Text(
                text = stringResource(R.string.invite_mutual_friends, contact.mutualFriendsCount), 
                style = AberTypography.Caption
            )
        }
        Checkbox(checked = false, onCheckedChange = {}, colors = CheckboxDefaults.colors(checkedColor = AberColor.Yellow))
    }
}

@Preview(showBackground = true)
@Composable
private fun InviteFriendsScreenPreview() {
    InviteFriendsScreen(onBackClick = {})
}
