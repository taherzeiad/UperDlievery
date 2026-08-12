package com.newuperapp.Uper.domain.repository

/** Thin result wrapper so ViewModels don't leak network/Firebase exceptions into UI state. */
sealed interface AuthResult {
    data object Success : AuthResult
    data class Error(val message: String) : AuthResult
}

interface AuthRepository {

    /** Registers a new driver with email + phone, triggers OTP send, returns success/failure. */
    suspend fun signUp(email: String, fullPhoneNumber: String): AuthResult

    /** Starts a phone-only login by sending an OTP to [fullPhoneNumber]. */
    suspend fun requestOtp(fullPhoneNumber: String): AuthResult

    /** Confirms the OTP code sent to the phone used in [signUp] or [requestOtp]. */
    suspend fun verifyOtp(fullPhoneNumber: String, code: String): AuthResult

    /** Resends a fresh OTP to the same phone number (rate-limited server-side). */
    suspend fun resendOtp(fullPhoneNumber: String): AuthResult
}
