package com.newuperapp.Uper.data.auth

import com.newuperapp.Uper.domain.auth.AuthRepository
import com.newuperapp.Uper.domain.auth.AuthResult
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun requestOtp(phoneNumber: String): AuthResult {
        delay(1000)
        return AuthResult.Success
    }

    override suspend fun verifyOtp(phoneNumber: String, code: String): AuthResult {
        delay(1000)
        return if (code == "1234") AuthResult.Success else AuthResult.Error("Invalid OTP code")
    }

    override suspend fun resendOtp(phoneNumber: String): AuthResult {
        delay(500)
        return AuthResult.Success
    }

    override suspend fun signUp(email: String, phoneNumber: String): AuthResult {
        delay(1500)
        return AuthResult.Success
    }
}
