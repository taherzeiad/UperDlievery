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
        illustrationRes = R.drawable.mobile,
        titleRes = R.string.onboarding_page1_title,
        descriptionRes = R.string.onboarding_page1_description
    ), OnboardingPage(
        illustrationRes = R.drawable.gps,
        titleRes = R.string.onboarding_page2_title,
        descriptionRes = R.string.onboarding_page2_description
    ), OnboardingPage(
        illustrationRes = R.drawable.money,
        titleRes = R.string.onboarding_page3_title,
        descriptionRes = R.string.onboarding_page3_description
    )
)
