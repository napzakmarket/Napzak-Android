package com.napzak.market.store.di

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
object StoreServiceModule {

    @Provides
    @Singleton
    fun provideStoreService(retrofit: Retrofit): StoreService = retrofit.create()

    @Provides
    @Singleton
    fun provideSettingService(retrofit: Retrofit): SettingService = retrofit.create()
}
