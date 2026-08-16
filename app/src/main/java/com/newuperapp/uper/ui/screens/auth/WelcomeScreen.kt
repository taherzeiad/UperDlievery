package com.newuperapp.uper.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.uper.R
import com.newuperapp.uper.ui.components.AberButton
import com.newuperapp.uper.ui.components.AberButtonStyle
import com.newuperapp.uper.ui.components.AberTextLink
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

/**
 * Welcome screen presented after onboarding, allowing users to choose between
 * signing up or signing in.
 *
 * @param onNavigateToSignUp Callback to navigate to the Sign Up flow.
 * @param onNavigateToSignIn Callback to navigate to the Sign In flow.
 */
@Composable
fun WelcomeScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    Scaffold(containerColor = AberColor.Yellow) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Background Illustration (matching Splash/Theme style)
            Image(
                painter = painterResource(id = R.drawable.group_2),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(240.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = stringResource(R.string.welcome_title),
                    style = AberTypography.HeroTitleBold.copy(fontSize = 32.sp)
                )
                
                Text(
                    text = stringResource(R.string.welcome_subtitle),
                    style = AberTypography.Subtitle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                AberButton(
                    text = stringResource(R.string.welcome_sign_up_cta),
                    onClick = onNavigateToSignUp,
                    style = AberButtonStyle.Dark
                )

                Spacer(modifier = Modifier.height(20.dp))

                AberTextLink(
                    text = stringResource(R.string.welcome_already_have_account),
                    onClick = onNavigateToSignIn
                )

                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    WelcomeScreen(onNavigateToSignUp = {}, onNavigateToSignIn = {})
}
