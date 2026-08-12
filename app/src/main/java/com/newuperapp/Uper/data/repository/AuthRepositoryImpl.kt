package com.newuperapp.Uper.data.repository

import com.newuperapp.Uper.domain.repository.AuthRepository
import com.newuperapp.Uper.domain.repository.AuthResult
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stub implementation — swap the `delay(...)` calls for real Retrofit/Firebase Auth
 * calls once the backend contract is available. Kept behind the [AuthRepository]
 * interface so ViewModels never need to change.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun signUp(email: String, fullPhoneNumber: String): AuthResult {
        delay(700)
        if (!email.contains("@")) return AuthResult.Error("Enter a valid email address")
        if (fullPhoneNumber.length < 8) return AuthResult.Error("Enter a valid phone number")
        return AuthResult.Success
    }

    override suspend fun requestOtp(fullPhoneNumber: String): AuthResult {
        delay(600)
        if (fullPhoneNumber.length < 8) return AuthResult.Error("Enter a valid phone number")
        return AuthResult.Success
    }

    override suspend fun verifyOtp(fullPhoneNumber: String, code: String): AuthResult {
        delay(600)
        return if (code.length == 4) AuthResult.Success
        else AuthResult.Error("Invalid verification code")
    }

    override suspend fun resendOtp(fullPhoneNumber: String): AuthResult {
        delay(500)
        return AuthResult.Success
    }
}
