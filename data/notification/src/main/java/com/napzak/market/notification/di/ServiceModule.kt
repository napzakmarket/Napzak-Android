package com.napzak.market.notification.di

import com.napzak.market.notification.service.NotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import jakarta.inject.Singleton
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(ServiceModule::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideNotificationService(retrofit: Retrofit): NotificationService =
        retrofit.create()
}
