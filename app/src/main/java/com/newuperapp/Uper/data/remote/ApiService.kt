package com.newuperapp.uper.data.remote

import com.newuperapp.uper.data.remote.dto.*
import retrofit2.http.*

/**
 * Core API interface for all backend communications.
 */
interface ApiService {

    // --- Auth ---
    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequestDto): AuthResponseDto

    @POST("auth/request-otp")
    suspend fun requestOtp(@Body request: OtpRequestDto): AuthResponseDto

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequestDto): AuthResponseDto

    // --- Driver ---
    @GET("driver/profile")
    suspend fun getProfile(): DriverProfileDto

    @PUT("driver/profile")
    suspend fun updateProfile(@Body profile: DriverProfileDto): ActionResponseDto

    @GET("driver/history")
    suspend fun getHistory(): List<HistoryItemDto>

    @GET("driver/wallet")
    suspend fun getWallet(): WalletDto

    @GET("driver/notifications")
    suspend fun getNotifications(): List<NotificationDto>

    @GET("driver/vehicles")
    suspend fun getVehicles(): List<VehicleDto>

    @POST("driver/online")
    suspend fun setOnline(@Query("isOnline") isOnline: Boolean): ActionResponseDto

    // --- Rides ---
    @GET("rides/incoming")
    suspend fun getIncomingRides(): List<RideRequestDto>

    @POST("rides/{id}/accept")
    suspend fun acceptRide(@Path("id") rideId: String): ActionResponseDto

    @POST("rides/{id}/ignore")
    suspend fun ignoreRide(@Path("id") rideId: String): ActionResponseDto

    @GET("rides/{id}/details")
    suspend fun getBookingDetails(@Path("id") rideId: String): BookingDetailsDto

    @POST("rides/{id}/cancel")
    suspend fun cancelBooking(@Path("id") rideId: String): ActionResponseDto

    @GET("rides/{id}/navigation")
    suspend fun getNavigationState(@Path("id") rideId: String): NavigationStateDto

    @POST("rides/{id}/arrived")
    suspend fun markArrived(@Path("id") rideId: String): ActionResponseDto
}
