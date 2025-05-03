package com.napzak.market.registration.di

import com.napzak.market.registration.service.RegistrationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideRegistrationService(retrofit: Retrofit): RegistrationService =
        retrofit.create()
}
