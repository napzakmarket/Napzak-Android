package com.napzak.market.banner.di

import com.napzak.market.banner.service.BannerService
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
    fun provideBannerService(retrofit: Retrofit): BannerService = retrofit.create()
}