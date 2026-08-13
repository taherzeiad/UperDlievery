package com.newuperapp.Uper.data.remote

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val uri = chain.request().url.toUri().toString()
        val responseString = when {
            // --- Auth ---
            uri.contains("auth/signup") || uri.contains("auth/verify-otp") -> {
                """{"status": "success", "token": "mock_jwt_token_999", "message": "Authenticated"}"""
            }
            uri.contains("auth/request-otp") -> {
                """{"status": "success", "message": "OTP sent"}"""
            }

            // --- Driver ---
            uri.contains("driver/profile") -> {
                """{
                    "id": "drv_001",
                    "name": "Taher Al-Sayer",
                    "level": "Gold Member",
                    "totalEarned": 1250.50,
                    "hoursOnline": 45.2,
                    "totalDistanceKm": 120.0,
                    "totalJobs": 54,
                    "currentLat": 60.1699,
                    "currentLng": 24.9384,
                    "currencySymbol": "${'$'}"
                }"""
            }
            uri.contains("driver/history") -> {
                """[
                    { 
                        "id": "1", 
                        "riderName": "Ali Ahmed", 
                        "date": "2026-08-12", 
                        "price": 25.0, 
                        "distanceKm": 5.2, 
                        "pickupAddress": "Main Street, Tower 5", 
                        "dropoffAddress": "City Center Mall", 
                        "paymentTags": ["CASH"] 
                    },
                    { 
                        "id": "2", 
                        "riderName": "Sarah Khaled", 
                        "date": "2026-08-11", 
                        "price": 18.5, 
                        "distanceKm": 3.1, 
                        "pickupAddress": "Airport Terminal 1", 
                        "dropoffAddress": "Hilton Hotel", 
                        "paymentTags": ["CARD"] 
                    }
                ]"""
            }
            uri.contains("driver/wallet") -> {
                """{
                    "totalEarned": 1250.50,
                    "balance": 325.00,
                    "currency": "${'$'}",
                    "transactions": [
                        {"id": "t1", "name": "Trip #102", "transactionNumber": "#TR-7722", "amount": 25.0, "type": "EARN"},
                        {"id": "t2", "name": "Trip #101", "transactionNumber": "#TR-7721", "amount": 15.0, "type": "EARN"}
                    ]
                }"""
            }
            uri.contains("driver/notifications") -> {
                """[
                    {"id": "n1", "type": "SYSTEM", "title": "Welcome", "message": "Welcome to Aber Driver!", "timestamp": "1 hour ago"},
                    {"id": "n2", "type": "WALLET", "title": "Payment", "message": "You received ${'$'}25.00", "timestamp": "2 hours ago"}
                ]"""
            }

            // --- Rides ---
            uri.contains("rides/incoming") -> {
                """[
                    {
                        "id": "ride_101",
                        "riderName": "Emma Watson",
                        "price": 45.0,
                        "distanceKm": 3.5,
                        "pickupAddress": "Green Garden Apt 4",
                        "pickupLat": 60.1719,
                        "pickupLng": 24.9350,
                        "dropoffAddress": "West Park Mall",
                        "dropoffLat": 60.1750,
                        "dropoffLng": 24.9410,
                        "tags": ["CASH", "DISCOUNT"]
                    }
                ]"""
            }
            uri.contains("rides/") && uri.contains("/details") -> {
                """{
                    "bookingId": "BK-9922",
                    "ride": {
                        "id": "ride_101",
                        "riderName": "Emma Watson",
                        "price": 45.0,
                        "distanceKm": 3.5,
                        "pickupAddress": "Green Garden Apt 4",
                        "pickupLat": 60.1719,
                        "pickupLng": 24.9350,
                        "dropoffAddress": "West Park Mall",
                        "dropoffLat": 60.1750,
                        "dropoffLng": 24.9410,
                        "tags": ["CASH", "DISCOUNT"]
                    },
                    "riderPhone": "+1 555 123 4567",
                    "note": "Please call when you arrive.",
                    "fareBreakdown": [
                        {"label": "Base Fare", "amount": 40.0},
                        {"label": "Discount", "amount": -5.0}
                    ],
                    "paidAmount": 45.0
                }"""
            }
            uri.contains("rides/") && uri.contains("/navigation") -> {
                """{
                    "rideId": "ride_101",
                    "pickupAddress": "Green Garden Apt 4",
                    "etaMinutes": 4,
                    "distanceKm": 1.2,
                    "fare": 45.0,
                    "currentStep": {
                        "maneuver": "TURN_RIGHT",
                        "instruction": "Turn right at Lake St",
                        "distanceText": "200m",
                        "isActive": true
                    },
                    "allSteps": [
                        {"maneuver": "STRAIGHT", "instruction": "Head south", "distanceText": "1km"},
                        {"maneuver": "TURN_RIGHT", "instruction": "Turn right at Lake St", "distanceText": "200m", "isActive": true}
                    ],
                    "polylinePoints": [
                        {"lat": 60.1699, "lng": 24.9384},
                        {"lat": 60.1710, "lng": 24.9370}
                    ],
                    "driverLat": 60.1699,
                    "driverLng": 24.9384
                }"""
            }

            // --- Default Success ---
            else -> """{"status": "success", "success": true, "message": "Action completed"}"""
        }

        return Response.Builder()
            .code(200)
            .message(responseString)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(responseString.toResponseBody("application/json".toMediaType()))
            .addHeader("content-type", "application/json")
            .build()
    }
}
