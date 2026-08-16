package com.newuperapp.uper.domain.model

data class FareLine(val label: String, val amount: Double)

data class BookingDetails(
    val bookingId: String,          // display id, e.g. "123456" (shown as "#123456")
    val request: RideRequest,
    val riderPhone: String,
    val note: String,
    val fareBreakdown: List<FareLine>,
    val paidAmount: Double
)
