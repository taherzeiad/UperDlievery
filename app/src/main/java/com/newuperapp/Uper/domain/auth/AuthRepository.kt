package com.newuperapp.Uper.domain.auth

sealed interface AuthResult {
    data object Success : AuthResult
    data class Error(val message: String) : AuthResult
}

interface AuthRepository {
    suspend fun requestOtp(phoneNumber: String): AuthResult
    suspend fun verifyOtp(phoneNumber: String, code: String): AuthResult
    suspend fun resendOtp(phoneNumber: String): AuthResult
    suspend fun signUp(email: String, phoneNumber: String): AuthResult
}
