package com.newuperapp.Uper.domain.repository

import com.newuperapp.Uper.domain.model.HistoryItem
import com.newuperapp.Uper.domain.model.Notification
import com.newuperapp.Uper.domain.model.WalletTransaction
import kotlinx.coroutines.flow.Flow

interface DriverRepository {
    suspend fun getHistory(): List<HistoryItem>
    suspend fun getWalletBalance(): Double
    suspend fun getWalletTransactions(): List<WalletTransaction>
    fun observeNotifications(): Flow<List<Notification>>
    suspend fun markNotificationAsRead(id: String)
}
