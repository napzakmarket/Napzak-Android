package com.napzak.market.store.di

import com.napzak.market.store.service.StoreEditProfileService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoreEditProfileServiceModule {

    @Provides
    @Singleton
    fun provideStoreEditProfileService(retrofit: Retrofit): StoreEditProfileService =
        retrofit.create(StoreEditProfileService::class.java)
}