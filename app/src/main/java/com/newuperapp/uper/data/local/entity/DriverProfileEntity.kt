package com.newuperapp.uper.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.newuperapp.uper.domain.model.DriverProfile

@Entity(tableName = "driver_profile")
data class DriverProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val level: String,
    val avatarUrl: String?,
    val totalEarned: Double,
    val hoursOnline: Double,
    val totalDistanceKm: Double,
    val totalJobs: Int,
    val currentLat: Double,
    val currentLng: Double,
    val currencySymbol: String
) {
    fun toDomain() = DriverProfile(
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

    companion object {
        fun fromDomain(domain: DriverProfile) = DriverProfileEntity(
            id = domain.id,
            name = domain.name,
            level = domain.level,
            avatarUrl = domain.avatarUrl,
            totalEarned = domain.totalEarned,
            hoursOnline = domain.hoursOnline,
            totalDistanceKm = domain.totalDistanceKm,
            totalJobs = domain.totalJobs,
            currentLat = domain.currentLat,
            currentLng = domain.currentLng,
            currencySymbol = domain.currencySymbol
        )
    }
}
