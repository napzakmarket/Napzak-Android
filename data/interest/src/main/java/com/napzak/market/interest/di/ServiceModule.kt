package com.napzak.market.interest.di

import com.napzak.market.interest.service.InterestProductService
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
    fun provideInterestProductService(retrofit: Retrofit): InterestProductService =
        retrofit.create()
}