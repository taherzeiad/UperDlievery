package com.newuperapp.uper.domain.model

data class Vehicle(
    val id: String,
    val brand: String,
    val model: String,
    val year: String,
    val licensePlate: String,
    val color: String,
    val bookingType: String,
    val isSelected: Boolean = false
)
