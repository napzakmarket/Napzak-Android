package com.napzak.market.presigned_url.di

import com.napzak.market.presigned_url.service.PresignedUrlService
import com.napzak.market.remote.qualifier.S3
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
    fun providePresignedUrlService(@S3 retrofit: Retrofit): PresignedUrlService =
        retrofit.create()
}
