package com.newuperapp.Uper.domain.onboarding

import kotlinx.coroutines.flow.Flow

/**
 * يحدد هل المستخدم خلّص أول-تشغيل (Onboarding + Enable Location) ولا لسا،
 * عشان شاشة الـ Splash تقرر تروح عالـ Onboarding ولا مباشرة عالـ Home.
 */
interface OnboardingRepository {
    val hasCompletedOnboarding: Flow<Boolean>
    suspend fun setOnboardingCompleted()
}