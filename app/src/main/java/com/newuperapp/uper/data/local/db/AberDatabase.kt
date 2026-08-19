package com.newuperapp.uper.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.newuperapp.uper.data.local.dao.DriverDao
import com.newuperapp.uper.data.local.entity.DriverProfileEntity

@Database(entities = [DriverProfileEntity::class], version = 1, exportSchema = false)
abstract class AberDatabase : RoomDatabase() {
    abstract fun driverDao(): DriverDao

    companion object {
        const val DATABASE_NAME = "aber_database"
    }
}
