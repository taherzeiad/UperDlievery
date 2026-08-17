package com.newuperapp.uper.ui.screens.home.invite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.uper.R
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

/**
 * Screen allowing drivers to share their referral code and invite riders to join.
 * Matches the design: a hamburger menu (no top-bar action), a large yellow
 * "group" icon, a bold headline with the reward amount emphasized inline,
 * an explanatory paragraph, the shareable invite code, and a full-width
 * Invite button pinned near the bottom of the screen.
 *
 * @param onBackClick Navigation callback (hamburger menu / drawer toggle).
 * @param onInviteClick Called when the user taps the "INVITE" button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteFriendsScreen(
    onBackClick: () -> Unit, onInviteClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.invite_friends_title),
                        style = AberTypography.ScreenTitle.copy(
                            fontSize = 22.sp, fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = AberColor.Yellow
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AberColor.White)
            )
        }, containerColor = AberColor.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            // Large yellow circle with the "group" icon

            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(AberColor.Yellow),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.teamwork_1),
                    contentDescription = null,
                    modifier = Modifier.size(88.dp)
                )
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.invite_friends_title),
                style = AberTypography.HeroTitleBold.copy(
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                )
            )

            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.invite_earn_up_to_prefix))
                    append(" ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(R.string.invite_earn_amount))
                    }
                    append(" ")
                    append(stringResource(R.string.invite_earn_up_to_suffix))
                }, style = AberTypography.HeroTitle.copy(
                    fontSize = 28.sp, textAlign = TextAlign.Center
                )
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.invite_description),
                style = AberTypography.Subtitle.copy(
                    color = AberColor.Ink, textAlign = TextAlign.Center
                )
            )

            Spacer(Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.invite_share_code_label),
                style = AberTypography.SectionLabel.copy(
                    color = AberColor.BorderGray, fontSize = 12.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            Spacer(Modifier.height(20.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                color = AberColor.SurfaceGray
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "0905070017",
                        style = AberTypography.CardTitle.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onInviteClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AberColor.Yellow, contentColor = AberColor.Ink
                )
            ) {
                Text(
                    text = stringResource(R.string.invite_cta),
                    style = AberTypography.Subtitle.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InviteFriendsScreenPreview() {
    InviteFriendsScreen(onBackClick = {})
}