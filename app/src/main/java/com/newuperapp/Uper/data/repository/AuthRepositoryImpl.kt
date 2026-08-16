package com.newuperapp.uper.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.newuperapp.uper.data.remote.ApiService
import com.newuperapp.uper.data.remote.dto.OtpRequestDto
import com.newuperapp.uper.data.remote.dto.SignUpRequestDto
import com.newuperapp.uper.data.remote.dto.VerifyOtpRequestDto
import com.newuperapp.uper.domain.repository.AuthRepository
import com.newuperapp.uper.domain.repository.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    override suspend fun signUp(email: String, fullPhoneNumber: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val response = apiService.signUp(SignUpRequestDto(email, fullPhoneNumber))
            if (response.status == "success") {
                response.token?.let { saveToken(it) }
                AuthResult.Success
            } else {
                AuthResult.Error(response.message ?: "Registration failed")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "Network error occurred")
        }
    }

    override suspend fun requestOtp(fullPhoneNumber: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val response = apiService.requestOtp(OtpRequestDto(fullPhoneNumber))
            if (response.status == "success") {
                AuthResult.Success
            } else {
                AuthResult.Error(response.message ?: "Failed to request OTP")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "Network error occurred")
        }
    }

    override suspend fun verifyOtp(fullPhoneNumber: String, code: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val response = apiService.verifyOtp(VerifyOtpRequestDto(fullPhoneNumber, code))
            if (response.status == "success") {
                response.token?.let { saveToken(it) }
                AuthResult.Success
            } else {
                AuthResult.Error(response.message ?: "Invalid OTP")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "Network error occurred")
        }
    }

    override suspend fun resendOtp(fullPhoneNumber: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val response = apiService.requestOtp(OtpRequestDto(fullPhoneNumber))
            if (response.status == "success") {
                AuthResult.Success
            } else {
                AuthResult.Error(response.message ?: "Failed to resend OTP")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "Network error occurred")
        }
    }

    private suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }
}
