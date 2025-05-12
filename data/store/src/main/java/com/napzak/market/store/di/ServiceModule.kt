package com.napzak.market.store.di

import com.napzak.market.remote.qualifier.NoAuth
import com.napzak.market.store.service.AuthService
import com.napzak.market.store.service.SettingService
import com.napzak.market.store.service.StoreService
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
    fun provideStoreService(retrofit: Retrofit): StoreService = retrofit.create()

    @Provides
    @Singleton
    fun provideSettingService(retrofit: Retrofit): SettingService = retrofit.create()

    @Provides
    @Singleton
    fun provideAuthService(
        @NoAuth retrofit: Retrofit
    ): AuthService = retrofit.create(AuthService::class.java)
}