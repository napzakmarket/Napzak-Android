package com.napzak.market.presigned_url.di

import com.napzak.market.presigned_url.service.PresignedUrlService
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
    fun providePresignedUrlService(retrofit: Retrofit): PresignedUrlService =
        retrofit.create()
}
