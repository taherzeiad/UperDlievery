package com.newuperapp.uper.domain.repository

import com.newuperapp.uper.domain.model.HistoryItem
import com.newuperapp.uper.domain.model.Notification
import com.newuperapp.uper.domain.model.WalletTransaction
import com.newuperapp.uper.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface DriverRepository {
    suspend fun getHistory(): Resource<List<HistoryItem>>
    suspend fun getWalletBalance(): Resource<Double>
    suspend fun getWalletTransactions(): Resource<List<WalletTransaction>>
    fun observeNotifications(): Flow<Resource<List<Notification>>>
    suspend fun markNotificationAsRead(id: String): Resource<Unit>
}
