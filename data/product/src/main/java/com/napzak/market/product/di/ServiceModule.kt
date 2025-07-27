package com.napzak.market.product.di

import com.napzak.market.product.service.ProductDetailService
import com.napzak.market.product.service.ProductExploreService
import com.napzak.market.product.service.ProductInterestService
import com.napzak.market.product.service.ProductRecommendationService
import com.napzak.market.product.service.ProductStoreService
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
    fun provideProductExploreService(retrofit: Retrofit): ProductExploreService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideProductStoreService(retrofit: Retrofit): ProductStoreService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideProductDetailService(retrofit: Retrofit): ProductDetailService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideProductInterestService(retrofit: Retrofit): ProductInterestService =
        retrofit.create()
}
