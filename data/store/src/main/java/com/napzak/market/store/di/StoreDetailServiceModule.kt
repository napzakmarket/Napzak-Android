package com.napzak.market.store.di

import com.napzak.market.store.service.StoreDetailService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoreDetailServiceModule {

    @Provides
    @Singleton
    fun provideStoreDetailService(retrofit: Retrofit): StoreDetailService = retrofit.create(StoreDetailService::class.java)
}