package com.newuperapp.uper.data.repository

import com.newuperapp.uper.data.remote.ApiService
import com.newuperapp.uper.data.remote.dto.HistoryItemDto
import com.newuperapp.uper.data.remote.dto.NotificationDto
import com.newuperapp.uper.data.remote.dto.TransactionDto
import com.newuperapp.uper.domain.model.*
import com.newuperapp.uper.domain.repository.DriverRepository
import com.newuperapp.uper.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : DriverRepository {

    override suspend fun getHistory(): Resource<List<HistoryItem>> = withContext(Dispatchers.IO) {
        try {
            Resource.Success(apiService.getHistory().map { it.toDomain() })
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to fetch history")
        }
    }

    override suspend fun getWalletBalance(): Resource<Double> = withContext(Dispatchers.IO) {
        try {
            Resource.Success(apiService.getWallet().balance)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to fetch wallet balance")
        }
    }

    override suspend fun getWalletTransactions(): Resource<List<WalletTransaction>> =
        withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiService.getWallet().transactions.map { it.toDomain() })
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Failed to fetch transactions")
            }
        }

    override fun observeNotifications(): Flow<Resource<List<Notification>>> = flow {
        while (true) {
            try {
                val dtos = apiService.getNotifications()
                emit(Resource.Success(dtos.map { it.toDomain() }))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Failed to fetch notifications"))
            }
            delay(10000)
        }
    }.catch { e ->
        emit(Resource.Error(e.localizedMessage ?: "Notification stream error"))
    }

    override suspend fun markNotificationAsRead(id: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                // apiService.markNotificationAsRead(id) // Placeholder
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Failed to mark notification as read")
            }
        }

    // --- Mappers ---

    private fun HistoryItemDto.toDomain() = HistoryItem(
        id = id,
        riderName = riderName,
        riderAvatarUrl = riderAvatarUrl,
        price = price,
        distanceKm = distanceKm,
        pickupAddress = pickupAddress,
        dropoffAddress = dropoffAddress,
        date = date,
        paymentTags = paymentTags.mapNotNull { RidePaymentTag.fromString(it) }
    )

    private fun TransactionDto.toDomain() = WalletTransaction(
        id = id,
        name = name,
        transactionNumber = transactionNumber,
        amount = amount,
        avatarUrl = avatarUrl
    )

    private fun NotificationDto.toDomain() = Notification(
        id = id,
        type = NotificationType.valueOf(type),
        title = title,
        message = message,
        timestamp = timestamp
    )
}
