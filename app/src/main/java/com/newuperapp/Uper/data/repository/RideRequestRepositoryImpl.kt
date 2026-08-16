package com.newuperapp.uper.data.repository

import com.newuperapp.uper.data.remote.ApiService
import com.newuperapp.uper.data.remote.dto.LatLngDto
import com.newuperapp.uper.data.remote.dto.NavigationStepDto
import com.newuperapp.uper.data.remote.dto.RideRequestDto
import com.newuperapp.uper.domain.model.*
import com.newuperapp.uper.domain.repository.RideRequestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RideRequestRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : RideRequestRepository {

    private val _onlineState = MutableStateFlow(false)
    private val _pendingRequests = MutableStateFlow<List<RideRequest>>(emptyList())

    // Polling simulation for real-time updates from backend
    private val pollingFlow = flow {
        while (true) {
            if (_onlineState.value) {
                try {
                    val dtos = apiService.getIncomingRides()
                    val domainModels = dtos.map { it.toDomain() }
                    _pendingRequests.value = domainModels
                    emit(domainModels)
                } catch (e: Exception) {
                    // Log error
                }
            }
            delay(5000) // Poll every 5 seconds
        }
    }

    override fun observeDriverProfile(): Flow<DriverProfile> = flow {
        try {
            val dto = apiService.getProfile()
            emit(dto.toDomain())
        } catch (e: Exception) {
            // Fallback or error handling
        }
    }

    override fun observeOnlineState() = _onlineState.asStateFlow()

    override suspend fun setOnline(isOnline: Boolean) = withContext(Dispatchers.IO) {
        try {
            apiService.setOnline(isOnline)
            _onlineState.value = isOnline
            if (!isOnline) _pendingRequests.value = emptyList()
        } catch (e: Exception) {
            // Handle failure
        }
    }

    override fun observeIncomingRideRequest(): Flow<RideRequest?> = pollingFlow.map { it.firstOrNull() }

    override fun observePendingRequests(): Flow<List<RideRequest>> = _pendingRequests.asStateFlow()

    override suspend fun acceptRide(rideId: String) {
        withContext(Dispatchers.IO) {
            apiService.acceptRide(rideId)
        }
    }

    override suspend fun ignoreRide(rideId: String) {
        withContext(Dispatchers.IO) {
            apiService.ignoreRide(rideId)
            _pendingRequests.value = _pendingRequests.value.filterNot { it.id == rideId }
        }
    }

    override suspend fun getBookingDetails(rideId: String): BookingDetails = withContext(Dispatchers.IO) {
        val dto = apiService.getBookingDetails(rideId)
        BookingDetails(
            bookingId = dto.bookingId,
            request = dto.ride.toDomain(),
            riderPhone = dto.riderPhone,
            note = dto.note ?: "",
            fareBreakdown = dto.fareBreakdown.map { FareLine(it.label, it.amount) },
            paidAmount = dto.paidAmount
        )
    }

    override suspend fun cancelBooking(rideId: String) {
        withContext(Dispatchers.IO) {
            apiService.cancelBooking(rideId)
            _pendingRequests.value = _pendingRequests.value.filterNot { it.id == rideId }
        }
    }

    override fun observePickupNavigation(rideId: String): Flow<PickupNavigationState> = flow {
        while (true) {
            try {
                val dto = apiService.getNavigationState(rideId)
                emit(
                    PickupNavigationState(
                        rideId = dto.rideId,
                        pickupAddress = dto.pickupAddress,
                        etaMinutes = dto.etaMinutes,
                        distanceKm = dto.distanceKm,
                        fare = dto.fare,
                        currentBanner = dto.currentStep.toDomain(),
                        steps = dto.allSteps.map { it.toDomain() },
                        routePolyline = dto.polylinePoints.map { LatLngPoint(it.lat, it.lng) },
                        driverLocation = LatLngPoint(dto.driverLat, dto.driverLng)
                    )
                )
            } catch (e: Exception) {
                // Handle error
            }
            delay(3000) // Update navigation every 3 seconds
        }
    }

    override suspend fun markArrivedAtPickup(rideId: String) {
        withContext(Dispatchers.IO) {
            apiService.markArrived(rideId)
        }
    }

    // --- Mappers ---

    private fun RideRequestDto.toDomain() = RideRequest(
        id = id,
        riderName = riderName,
        riderAvatarUrl = riderAvatarUrl,
        price = price,
        distanceKm = distanceKm,
        tags = tags.map { RidePaymentTag.valueOf(it) },
        pickupAddress = pickupAddress,
        pickupLocation = LatLngPoint(pickupLat, pickupLng),
        dropoffAddress = dropoffAddress,
        dropoffLocation = LatLngPoint(dropoffLat, dropoffLng)
    )

    private fun com.newuperapp.uper.data.remote.dto.DriverProfileDto.toDomain() = DriverProfile(
        id = id,
        name = name,
        level = level,
        avatarUrl = avatarUrl,
        totalEarned = totalEarned,
        hoursOnline = hoursOnline,
        totalDistanceKm = totalDistanceKm,
        totalJobs = totalJobs,
        currentLat = currentLat,
        currentLng = currentLng,
        currencySymbol = currencySymbol
    )

    private fun NavigationStepDto.toDomain() = NavigationStep(
        maneuver = TurnManeuver.valueOf(maneuver),
        instruction = instruction,
        distanceText = distanceText,
        subtext = subtext,
        isActive = isActive
    )
}
