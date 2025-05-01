package com.napzak.market.product.di

import com.napzak.market.product.service.ProductDetailService
import com.napzak.market.product.service.ProductRecommendationService
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
    fun provideProductRecommendationService(retrofit: Retrofit): ProductRecommendationService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideProductDetailService(retrofit: Retrofit): ProductDetailService =
        retrofit.create()
}
