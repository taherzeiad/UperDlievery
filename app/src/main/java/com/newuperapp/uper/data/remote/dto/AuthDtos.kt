package com.newuperapp.uper.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDto(
    val email: String,
    val phone: String
)

@Serializable
data class OtpRequestDto(
    val phone: String
)

@Serializable
data class VerifyOtpRequestDto(
    val phone: String,
    val code: String
)

@Serializable
data class AuthResponseDto(
    val status: String,
    val token: String? = null,
    val message: String? = null
)


@Serializable
data class ActionResponseDto(
    val success: Boolean,
    val message: String? = null
)
