package com.newuperapp.uper.data.repository

import com.newuperapp.uper.data.remote.ApiService
import com.newuperapp.uper.data.remote.dto.HistoryItemDto
import com.newuperapp.uper.data.remote.dto.NotificationDto
import com.newuperapp.uper.data.remote.dto.TransactionDto
import com.newuperapp.uper.domain.model.*
import com.newuperapp.uper.domain.repository.DriverRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : DriverRepository {

    override suspend fun getHistory(): List<HistoryItem> = withContext(Dispatchers.IO) {
        apiService.getHistory().map { it.toDomain() }
    }

    override suspend fun getWalletBalance(): Double = withContext(Dispatchers.IO) {
        apiService.getWallet().balance
    }

    override suspend fun getWalletTransactions(): List<WalletTransaction> =
        withContext(Dispatchers.IO) {
            apiService.getWallet().transactions.map { it.toDomain() }
        }

    override fun observeNotifications(): Flow<List<Notification>> = flow {
        while (true) {
            try {
                val dtos = apiService.getNotifications()
                emit(dtos.map { it.toDomain() })
            } catch (e: Exception) {
                // Handle error
            }
            delay(10000)
        }
    }

    override suspend fun markNotificationAsRead(id: String) {
        withContext(Dispatchers.IO) {
            // Placeholder
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
