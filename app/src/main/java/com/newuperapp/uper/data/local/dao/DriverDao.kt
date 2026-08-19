package com.newuperapp.uper.data.local.dao

import androidx.room.*
import com.newuperapp.uper.data.local.entity.DriverProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {
    @Query("SELECT * FROM driver_profile LIMIT 1")
    fun getDriverProfile(): Flow<DriverProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriverProfile(profile: DriverProfileEntity)

    @Query("DELETE FROM driver_profile")
    suspend fun deleteDriverProfile()
}
