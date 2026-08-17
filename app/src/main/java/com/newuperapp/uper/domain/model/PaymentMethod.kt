package com.newuperapp.uper.domain.model

enum class PaymentMethodType {
    VISA, PAYPAL, MASTERCARD
}

data class PaymentMethod(
    val id: String,
    val type: PaymentMethodType,
    val details: String,
    val isSelected: Boolean = false
)
