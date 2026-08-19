package com.newuperapp.uper.domain.repository

import com.newuperapp.uper.domain.model.BookingDetails
import com.newuperapp.uper.domain.model.DriverProfile
import com.newuperapp.uper.domain.model.PickupNavigationState
import com.newuperapp.uper.domain.model.RideRequest
import com.newuperapp.uper.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RideRequestRepository {

    /** Current driver's profile + running stats (Offline bottom card). */
    fun observeDriverProfile(): Flow<Resource<DriverProfile>>

    /** True when the driver has toggled Online. Persisted across process death. */
    fun observeOnlineState(): Flow<Boolean>

    suspend fun setOnline(isOnline: Boolean): Resource<Unit>

    /**
     * Emits incoming ride offers while the driver is online. Emits `null` when
     * there is no pending offer (idle "searching" state).
     */
    fun observeIncomingRideRequest(): Flow<Resource<RideRequest?>>

    /** Full queue of pending offers shown on the "swipe up" expanded sheet. */
    fun observePendingRequests(): Flow<Resource<List<RideRequest>>>

    suspend fun acceptRide(rideId: String): Resource<Unit>

    suspend fun ignoreRide(rideId: String): Resource<Unit>

    /** Accepted-trip summary shown on the Booking Details screen (`#123456`). */
    suspend fun getBookingDetails(rideId: String): Resource<BookingDetails>

    suspend fun cancelBooking(rideId: String): Resource<Unit>

    /** Turn-by-turn state for the "Go to pickup" navigation screen. */
    fun observePickupNavigation(rideId: String): Flow<Resource<PickupNavigationState>>

    /** Marks the driver as arrived at pickup (drives the "DROP OFF" CTA). */
    suspend fun markArrivedAtPickup(rideId: String): Resource<Unit>
}
