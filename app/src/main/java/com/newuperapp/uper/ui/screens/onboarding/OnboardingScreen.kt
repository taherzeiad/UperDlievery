package com.newuperapp.uper.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newuperapp.uper.R
import com.newuperapp.uper.ui.components.AberButton
import com.newuperapp.uper.ui.components.AberPageIndicator
import com.newuperapp.uper.ui.components.AberTextLink
import com.newuperapp.uper.ui.theme.AberColor

/**
 * Pager-based onboarding screen introduce the app's core features to new drivers.
 *
 * @param onFinished Callback invoked when the user skips or completes the onboarding.
 * @param viewModel The state management unit for onboarding logic.
 */
@Composable
fun OnboardingRoute(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                OnboardingEvent.NavigateNext -> onFinished()
            }
        }
    }
    OnboardingScreen(
        pages = onboardingPages,
        onSkip = viewModel::onOnboardingFinished,
        onGetStarted = viewModel::onOnboardingFinished,
    )
}

@Composable
fun OnboardingScreen(
    pages: List<OnboardingPage>,
    onSkip: () -> Unit,
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { pages.size }
    val isLastPage = pagerState.currentPage == pages.lastIndex

    Column(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState, 
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(page = pages[page])
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLastPage) {
                AberButton(
                    text = stringResource(id = R.string.onboarding_get_started),
                    onClick = onGetStarted
                )
            } else {
                AberTextLink(
                    text = stringResource(id = R.string.onboarding_skip), 
                    onClick = onSkip
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Custom indicator matching the design's dot style
        AberPageIndicator(
            pageCount = pages.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 125.dp)
        )
    }
}

/**
 * Individual onboarding page layout.
 */
@Composable
private fun OnboardingPageContent(page: OnboardingPage, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Image(
            painter = painterResource(id = page.illustrationRes),
            contentDescription = null,
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(id = page.titleRes),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = AberColor.Ink,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = stringResource(id = page.descriptionRes),
            fontSize = 17.sp,
            color = AberColor.Ink,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen(
        pages = onboardingPages,
        onSkip = {},
        onGetStarted = {},
    )
}
