package com.newuperapp.uper.di

import android.content.Context
import androidx.room.Room
import com.newuperapp.uper.data.local.dao.DriverDao
import com.newuperapp.uper.data.local.db.AberDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AberDatabase {
        return Room.databaseBuilder(
            context,
            AberDatabase::class.java,
            AberDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDriverDao(database: AberDatabase): DriverDao {
        return database.driverDao()
    }
}
