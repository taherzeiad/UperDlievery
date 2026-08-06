package com.newuperapp.Uper.data.home

import com.newuperapp.Uper.domain.home.DriverProfile
import com.newuperapp.Uper.domain.home.DriverProfileRepository
import com.newuperapp.Uper.domain.home.LatLng
import com.newuperapp.Uper.domain.home.RideRequest
import com.newuperapp.Uper.domain.home.RideRequestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * تنفيذ مؤقت (Fake) لغرض التطوير والمعاينة. بدّليه بتنفيذ حقيقي يقرأ من
 * الـ API/DB الفعلي (Retrofit/Room) قبل الإنتاج.
 */
class FakeDriverProfileRepository @Inject constructor() : DriverProfileRepository {
    override fun observeProfile(): Flow<DriverProfile> = flowOf(
        DriverProfile(
            name = "Jeremiah Curtis",
            level = "Basic level",
            avatarUrl = null,
            totalEarned = 325.0,
            hoursOnline = 10.2,
            totalDistanceKm = 30.0,
            totalJobs = 20
        )
    )
}

/**
 * تنفيذ مؤقت (Fake) — بيرجّع نفس الطلب دايماً كمثال. بالتطبيق الحقيقي هاي
 * الدالة بتستمع لسوكيت أو تعمل polling لطلبات الرحلات الجديدة من السيرفر.
 */
class FakeRideRequestRepository @Inject constructor() : RideRequestRepository {
    override fun observeIncomingRequests(): Flow<RideRequest?> = flowOf(
        RideRequest(
            passengerName = "Esther Berry",
            passengerAvatarUrl = null,
            fare = 25.0,
            distanceKm = 2.2,
            tags = listOf("ApplePay", "Discount"),
            pickupAddress = "7958 Swift Village",
            dropoffAddress = "105 William St, Chicago, US",
            driverLocation = LatLng(60.1699, 24.9384),
            pickupLocation = LatLng(60.1710, 24.9400),
            dropoffLocation = LatLng(60.1740, 24.9420)
        )
    )
}