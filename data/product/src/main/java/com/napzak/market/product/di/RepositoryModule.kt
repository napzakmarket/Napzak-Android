package com.napzak.market.product.di

import com.napzak.market.product.repository.ProductRecommendationRepository
import com.napzak.market.product.repositoryimpl.ProductRecommendationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProductRecommendationRepository(
        repositoryImpl: ProductRecommendationRepositoryImpl
    ): ProductRecommendationRepository
}
