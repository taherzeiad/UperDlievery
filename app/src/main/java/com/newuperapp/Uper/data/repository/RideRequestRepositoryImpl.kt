package com.newuperapp.Uper.data.repository

import com.newuperapp.Uper.domain.model.BookingDetails
import com.newuperapp.Uper.domain.model.DriverProfile
import com.newuperapp.Uper.domain.model.FareLine
import com.newuperapp.Uper.domain.model.LatLngPoint
import com.newuperapp.Uper.domain.model.NavigationStep
import com.newuperapp.Uper.domain.model.PickupNavigationState
import com.newuperapp.Uper.domain.model.RidePaymentTag
import com.newuperapp.Uper.domain.model.RideRequest
import com.newuperapp.Uper.domain.model.TurnManeuver
import com.newuperapp.Uper.domain.repository.RideRequestRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Simulated real-time source. Replace with a Firestore/WebSocket-backed implementation
 * once the live dispatch backend is wired up — the [RideRequestRepository] contract
 * (Flow-based) is designed to make that swap a one-file change.
 */
@Singleton
class RideRequestRepositoryImpl @Inject constructor() : RideRequestRepository {

    private val onlineState = MutableStateFlow(false)

    private val driverProfile = MutableStateFlow(
        DriverProfile(
            id = "drv_001",
            name = "Jeremiah Curtis",
            level = "Basic level",
            avatarUrl = null,
            totalEarned = 325.0,
            hoursOnline = 10.2,
            totalDistanceKm = 30.0,
            totalJobs = 20,
            currentLat = 60.1699,
            currentLng = 24.9384
        )
    )

    private val pendingRequests = MutableStateFlow<List<RideRequest>>(emptyList())

    override fun observeDriverProfile() = driverProfile.asStateFlow()

    override fun observeOnlineState() = onlineState.asStateFlow()

    override suspend fun setOnline(isOnline: Boolean) {
        onlineState.value = isOnline
        pendingRequests.value = if (isOnline) DEMO_QUEUE else emptyList()
    }

    override fun observeIncomingRideRequest() = flow {
        emit(pendingRequests.value.firstOrNull())
    }

    override fun observePendingRequests() = pendingRequests.asStateFlow()

    override suspend fun acceptRide(rideId: String) {
        // TODO: call backend "accept" endpoint. Demo: leave the queue as-is so
        // BookingDetailsScreen can still look it up by id.
    }

    override suspend fun ignoreRide(rideId: String) {
        pendingRequests.value = pendingRequests.value.filterNot { it.id == rideId }
    }

    override suspend fun getBookingDetails(rideId: String): BookingDetails {
        delay(300)
        val request = pendingRequests.value.firstOrNull { it.id == rideId } ?: DEMO_QUEUE.first()
        return BookingDetails(
            bookingId = "123456",
            request = request,
            riderPhone = "+1 555 010 2244",
            note = "Lorem ipsum dolor sit amet, consectetur adipisc elit. Nullam ac vestibulum erat. " +
                "Cras vulputate auctor lectus at consequat.",
            fareBreakdown = listOf(
                FareLine("Apple Pay", 15.0),
                FareLine("Discount", 10.0)
            ),
            paidAmount = request.price
        )
    }

    override suspend fun cancelBooking(rideId: String) {
        pendingRequests.value = pendingRequests.value.filterNot { it.id == rideId }
    }

    override fun observePickupNavigation(rideId: String) = flow {
        emit(
            PickupNavigationState(
                rideId = rideId,
                pickupAddress = "7958 Swift Village",
                etaMinutes = 5,
                distanceKm = 2.2,
                fare = 25.0,
                currentBanner = NavigationStep(
                    maneuver = TurnManeuver.TURN_RIGHT,
                    instruction = "Turn right at 105 William St, Chicago, US",
                    distanceText = "250m",
                    isActive = true
                ),
                steps = listOf(
                    NavigationStep(TurnManeuver.STRAIGHT, "Head southwest on Madison St", "18 miles"),
                    NavigationStep(TurnManeuver.TURN_LEFT, "Turn left onto 4th Ave", "12 miles"),
                    NavigationStep(
                        TurnManeuver.TURN_RIGHT, "Turn right at 105th N Link Rd", "40 miles",
                        subtext = "Pass by Executive Hotel Pacific (on the left)"
                    ),
                    NavigationStep(
                        TurnManeuver.TURN_RIGHT, "Turn right at 105 William St, Chicago, US", "250 miles",
                        isActive = true
                    ),
                    NavigationStep(
                        TurnManeuver.STRAIGHT, "Continue straight to stay on Vancouver", "24 miles",
                        subtext = "Entering California"
                    ),
                    NavigationStep(TurnManeuver.TURN_LEFT, "Keep left, follow signs for SF Intl Airport", "")
                ),
                routePolyline = listOf(
                    LatLngPoint(60.1699, 24.9384),
                    LatLngPoint(60.1719, 24.9350)
                ),
                driverLocation = LatLngPoint(60.1699, 24.9384)
            )
        )
    }

    override suspend fun markArrivedAtPickup(rideId: String) {
        // TODO: call backend "arrived"/"start trip" endpoint, then navigate to Drop-off nav.
    }

    companion object {
        private val DEMO_QUEUE = listOf(
            RideRequest(
                id = "ride_demo_1",
                riderName = "Esther Berry",
                riderAvatarUrl = null,
                price = 25.0,
                distanceKm = 2.2,
                tags = listOf(RidePaymentTag.APPLE_PAY, RidePaymentTag.DISCOUNT),
                pickupAddress = "7958 Swift Village",
                pickupLocation = LatLngPoint(60.1719, 24.9350),
                dropoffAddress = "105 William St, Chicago, US",
                dropoffLocation = LatLngPoint(60.1750, 24.9410)
            ),
            RideRequest(
                id = "ride_demo_2",
                riderName = "Callie Greer",
                riderAvatarUrl = null,
                price = 20.0,
                distanceKm = 1.5,
                tags = listOf(RidePaymentTag.APPLE_PAY, RidePaymentTag.DISCOUNT),
                pickupAddress = "62 Kobe Trafficway",
                pickupLocation = LatLngPoint(60.1705, 24.9360),
                dropoffAddress = "280 Icie Park Suite 496",
                dropoffLocation = LatLngPoint(60.1740, 24.9400)
            ),
            RideRequest(
                id = "ride_demo_3",
                riderName = "Earl Guerrero",
                riderAvatarUrl = null,
                price = 10.0,
                distanceKm = 0.5,
                tags = listOf(RidePaymentTag.APPLE_PAY),
                pickupAddress = "9965 Soledad Ports",
                pickupLocation = LatLngPoint(60.1701, 24.9370),
                dropoffAddress = "12 Marion Ridges",
                dropoffLocation = LatLngPoint(60.1730, 24.9390)
            )
        )
    }
}
