package com.newuperapp.uper.data.repository

import com.newuperapp.uper.data.local.dao.DriverDao
import com.newuperapp.uper.data.local.entity.DriverProfileEntity
import com.newuperapp.uper.data.remote.ApiService
import com.newuperapp.uper.data.remote.dto.NavigationStepDto
import com.newuperapp.uper.data.remote.dto.RideRequestDto
import com.newuperapp.uper.domain.model.*
import com.newuperapp.uper.domain.repository.RideRequestRepository
import com.newuperapp.uper.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class RideRequestRepositoryImpl @Inject constructor(
    private val apiService: ApiService, private val driverDao: DriverDao
) : RideRequestRepository {

    private val _onlineState = MutableStateFlow(false)
    private val _pendingRequests = MutableStateFlow<Resource<List<RideRequest>>>(Resource.Loading)

    // Polling simulation for real-time updates from backend
    private val pollingFlow = flow {
        while (true) {
            if (_onlineState.value) {
                try {
                    val dtos = apiService.getIncomingRides()
                    val domainModels = dtos.map { it.toDomain() }
                    _pendingRequests.value = Resource.Success(domainModels)
                    emit(domainModels)
                } catch (e: Exception) {
                    _pendingRequests.value =
                        Resource.Error(e.localizedMessage ?: "Failed to fetch rides")
                }
            } else {
                _pendingRequests.value = Resource.Success(emptyList())
            }
            delay(5000.milliseconds)
        }
    }

    override fun observeDriverProfile(): Flow<Resource<DriverProfile>> =
        driverDao.getDriverProfile().map { entity ->
            if (entity != null) Resource.Success(entity.toDomain()) else Resource.Loading
        }.onStart {
            fetchAndCacheProfile()
        }.catch { e ->
            emit(Resource.Error(e.localizedMessage ?: "Database error"))
        }

    private suspend fun fetchAndCacheProfile() {
        try {
            val dto = apiService.getProfile()
            driverDao.insertDriverProfile(DriverProfileEntity.fromDomain(dto.toDomain()))
        } catch (e: Exception) {
            // Background update failed, quiet fail as we have local data
        }
    }

    override fun observeOnlineState() = _onlineState.asStateFlow()

    override suspend fun setOnline(isOnline: Boolean): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                apiService.setOnline(isOnline)
                _onlineState.value = isOnline
                if (!isOnline) _pendingRequests.value = Resource.Success(emptyList())
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Failed to change online status")
            }
        }

    override fun observeIncomingRideRequest(): Flow<Resource<RideRequest?>> =
        pollingFlow.map { Resource.Success(it.firstOrNull()) as Resource<RideRequest?> }
            .catch { emit(Resource.Error(it.localizedMessage ?: "Error observing rides")) }

    override fun observePendingRequests(): Flow<Resource<List<RideRequest>>> =
        _pendingRequests.asStateFlow()

    override suspend fun acceptRide(rideId: String): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            apiService.acceptRide(rideId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to accept ride")
        }
    }

    override suspend fun ignoreRide(rideId: String): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            apiService.ignoreRide(rideId)
            val current = _pendingRequests.value
            if (current is Resource.Success) {
                _pendingRequests.value =
                    Resource.Success(current.data.filterNot { it.id == rideId })
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to ignore ride")
        }
    }

    override suspend fun getBookingDetails(rideId: String): Resource<BookingDetails> =
        withContext(Dispatchers.IO) {
            try {
                val dto = apiService.getBookingDetails(rideId)
                val details = BookingDetails(
                    bookingId = dto.bookingId,
                    request = dto.ride.toDomain(),
                    riderPhone = dto.riderPhone,
                    note = dto.note ?: "",
                    fareBreakdown = dto.fareBreakdown.map { FareLine(it.label, it.amount) },
                    paidAmount = dto.paidAmount
                )
                Resource.Success(details)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Failed to load booking details")
            }
        }

    override suspend fun cancelBooking(rideId: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                apiService.cancelBooking(rideId)
                val current = _pendingRequests.value
                if (current is Resource.Success) {
                    _pendingRequests.value =
                        Resource.Success(current.data.filterNot { it.id == rideId })
                }
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Failed to cancel booking")
            }
        }

    override fun observePickupNavigation(rideId: String): Flow<Resource<PickupNavigationState>> =
        flow {
            while (true) {
                try {
                    val dto = apiService.getNavigationState(rideId)
                    emit(
                        Resource.Success(
                            PickupNavigationState(
                                rideId = dto.rideId,
                                pickupAddress = dto.pickupAddress,
                                etaMinutes = dto.etaMinutes,
                                distanceKm = dto.distanceKm,
                                fare = dto.fare,
                                currentBanner = dto.currentStep.toDomain(),
                                steps = dto.allSteps.map { it.toDomain() },
                                routePolyline = dto.polylinePoints.map {
                                    LatLngPoint(
                                        it.lat, it.lng
                                    )
                                },
                                driverLocation = LatLngPoint(dto.driverLat, dto.driverLng)
                            )
                        )
                    )
                } catch (e: Exception) {
                    emit(Resource.Error(e.localizedMessage ?: "Navigation update failed"))
                }
                delay(3000)
            }
        }

    override suspend fun markArrivedAtPickup(rideId: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                apiService.markArrived(rideId)
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Failed to mark arrival")
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
