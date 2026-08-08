package com.newuperapp.Uper.domain.auth

data class CountryCode(
    val code: String,
    val name: String,
    val flag: String,
    val phoneCode: String
)

val defaultCountryCodes = listOf(
    CountryCode("US", "United States", "🇺🇸", "+1"),
    CountryCode("SA", "Saudi Arabia", "🇸🇦", "+966"),
    CountryCode("EG", "Egypt", "🇪🇬", "+20"),
    CountryCode("AE", "United Arab Emirates", "🇦🇪", "+971"),
    CountryCode("JO", "Jordan", "🇯🇴", "+962")
)
