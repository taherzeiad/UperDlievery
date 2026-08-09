package com.newuperapp.Uper.data.home

import com.newuperapp.Uper.domain.home.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * تنفيذ مؤقت (Fake) لغرض التطوير والمعاينة. بدّليه بتنفيذ حقيقي يقرأ من
 * الـ API/DB الفعلي (Retrofit/Room) قبل الإنتاج.
 */
@Singleton
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
@Singleton
class FakeRideRequestRepository @Inject constructor() : RideRequestRepository {
    override fun observeIncomingRequests(): Flow<RideRequest?> = flowOf(
        RideRequest(
            passengerName = "Esther Berry",
            passengerAvatarUrl = null,
            fare = 25.0,
            distanceKm = 2.2,
            tags = listOf(RidePaymentTag.APPLE_PAY, RidePaymentTag.DISCOUNT),
            pickupAddress = "7958 Swift Village",
            dropoffAddress = "105 William St, Chicago, US",
            driverLocation = LatLng(60.1699, 24.9384),
            pickupLocation = LatLng(60.1710, 24.9400),
            dropoffLocation = LatLng(60.1740, 24.9420)
        )
    )

    override suspend fun getBookingDetails(rideId: String): BookingDetails? {
        delay(500)
        return BookingDetails(
            bookingId = "6857",
            request = RideRequestShort(
                riderName = "Esther Berry",
                riderRating = 4.8f,
                tags = listOf(RidePaymentTag.APPLE_PAY, RidePaymentTag.DISCOUNT),
                price = 25.0,
                distanceKm = 2.2,
                pickupAddress = "7958 Swift Village",
                dropoffAddress = "105 William St, Chicago, US"
            ),
            riderPhone = "+1 234 567 890",
            note = "Please wait for 5 minutes, I'm coming down.",
            fareBreakdown = listOf(
                FareLine("Trip fare", 20.0), FareLine("Service fee", 3.0), FareLine("Tax", 2.0)
            ),
            paidAmount = 25.0
        )
    }

    override suspend fun cancelBooking(rideId: String) {
        delay(500)
    }

    override fun observePickupNavigation(rideId: String): Flow<PickupNavigationState?> = flow {
        emit(
            PickupNavigationState(
                rideId = rideId,
                riderName = "Esther Berry",
                pickupAddress = "7958 Swift Village",
                distanceToPickup = "1.2 km",
                timeToPickup = "4 min",
                driverLocation = LatLng(60.1699, 24.9384),
                riderLocation = LatLng(60.1710, 24.9400),
                routePolyline = listOf(
                    LatLng(60.1699, 24.9384), LatLng(60.1705, 24.9390), LatLng(60.1710, 24.9400)
                ),
                currentBanner = NavigationStep(
                    instruction = "Turn left onto Main St",
                    distanceText = "200 m",
                    maneuver = TurnManeuver.TURN_LEFT,
                    isActive = true
                ),
                steps = listOf(
                    NavigationStep(
                        instruction = "Turn left onto Main St",
                        distanceText = "200 m",
                        maneuver = TurnManeuver.TURN_LEFT,
                        isActive = true
                    ), NavigationStep(
                        instruction = "Go straight for 1 km",
                        distanceText = "1 km",
                        maneuver = TurnManeuver.STRAIGHT
                    )
                ),
                etaMinutes = 4,
                distanceKm = 1.2,
                fare = 25.0
            )
        )
    }

    override suspend fun markArrivedAtPickup(rideId: String) {
        delay(500)
    }
}