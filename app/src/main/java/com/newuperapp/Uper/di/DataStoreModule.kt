package com.newuperapp.Uper.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.newuperapp.Uper.data.repository.AuthRepositoryImpl
import com.newuperapp.Uper.data.repository.RideRequestRepositoryImpl
import com.newuperapp.Uper.data.onboarding.OnboardingRepositoryImpl
import com.newuperapp.Uper.domain.repository.AuthRepository
import com.newuperapp.Uper.domain.repository.RideRequestRepository
import com.newuperapp.Uper.domain.onboarding.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "aber_prefs")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        impl: OnboardingRepositoryImpl
    ): OnboardingRepository

    // TODO: بدّلي هالاتنين لتنفيذ حقيقي (Retrofit/سوكيت) قبل الإنتاج — حالياً Fake ثابت.
    @Binds
    @Singleton
    abstract fun bindRideRequestRepository(
        impl: RideRequestRepositoryImpl
    ): RideRequestRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}