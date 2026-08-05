package com.newuperapp.Uper.ui.screens.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.newuperapp.Uper.R

data class OnboardingPage(
    @param:DrawableRes val illustrationRes: Int,
    @param:StringRes val titleRes: Int,
    @param:StringRes val descriptionRes: Int
)

val onboardingPages = listOf(
    OnboardingPage(
        illustrationRes = R.drawable.ic_onboarding_accept_job,
        titleRes = R.string.onboarding_page1_title,
        descriptionRes = R.string.onboarding_page1_description
    ),
    OnboardingPage(
        illustrationRes = R.drawable.ic_onboarding_tracking,
        titleRes = R.string.onboarding_page2_title,
        descriptionRes = R.string.onboarding_page2_description
    ),
    OnboardingPage(
        illustrationRes = R.drawable.ic_onboarding_earn_money,
        titleRes = R.string.onboarding_page3_title,
        descriptionRes = R.string.onboarding_page3_description
    )
)
