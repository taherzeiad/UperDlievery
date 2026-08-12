package com.newuperapp.Uper.domain.model

data class WalletTransaction(
    val id: String,
    val name: String,
    val transactionNumber: String,
    val amount: Double,
    val currencySymbol: String = "$",
    val avatarUrl: String? = null
)
