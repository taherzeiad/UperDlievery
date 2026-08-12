package com.newuperapp.Uper.domain.repository

import com.newuperapp.Uper.domain.model.BookingDetails
import com.newuperapp.Uper.domain.model.DriverProfile
import com.newuperapp.Uper.domain.model.PickupNavigationState
import com.newuperapp.Uper.domain.model.RideRequest
import kotlinx.coroutines.flow.Flow

interface RideRequestRepository {

    /** Current driver's profile + running stats (Offline bottom card). */
    fun observeDriverProfile(): Flow<DriverProfile>

    /** True when the driver has toggled Online. Persisted across process death. */
    fun observeOnlineState(): Flow<Boolean>

    suspend fun setOnline(isOnline: Boolean)

    /**
     * Emits incoming ride offers while the driver is online. Emits `null` when
     * there is no pending offer (idle "searching" state).
     */
    fun observeIncomingRideRequest(): Flow<RideRequest?>

    /** Full queue of pending offers shown on the "swipe up" expanded sheet. */
    fun observePendingRequests(): Flow<List<RideRequest>>

    suspend fun acceptRide(rideId: String)

    suspend fun ignoreRide(rideId: String)

    /** Accepted-trip summary shown on the Booking Details screen (`#123456`). */
    suspend fun getBookingDetails(rideId: String): BookingDetails

    suspend fun cancelBooking(rideId: String)

    /** Turn-by-turn state for the "Go to pickup" navigation screen. */
    fun observePickupNavigation(rideId: String): Flow<PickupNavigationState>

    /** Marks the driver as arrived at pickup (drives the "DROP OFF" CTA). */
    suspend fun markArrivedAtPickup(rideId: String)
}
