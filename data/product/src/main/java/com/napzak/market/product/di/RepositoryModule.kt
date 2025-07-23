package com.napzak.market.product.di

import com.napzak.market.product.repository.ProductDetailRepository
import com.napzak.market.product.repository.ProductExploreRepository
import com.napzak.market.product.repository.ProductInterestRepository
import com.napzak.market.product.repository.ProductRecommendationRepository
import com.napzak.market.product.repository.ProductStoreRepository
import com.napzak.market.product.repositoryimpl.ProductDetailRepositoryImpl
import com.napzak.market.product.repositoryimpl.ProductExploreRepositoryImpl
import com.napzak.market.product.repositoryimpl.ProductInterestRepositoryImpl
import com.napzak.market.product.repositoryimpl.ProductRecommendationRepositoryImpl
import com.napzak.market.product.repositoryimpl.ProductStoreRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindProductExploreRepository(
        repositoryImpl: ProductExploreRepositoryImpl,
    ): ProductExploreRepository

    @Binds
    @Singleton
    abstract fun bindProductStoreRepository(
        repositoryImpl: ProductStoreRepositoryImpl,
    ): ProductStoreRepository

    @Binds
    @Singleton
    abstract fun bindProductDetailRepository(
        repositoryImpl: ProductDetailRepositoryImpl
    ): ProductDetailRepository

    @Binds
    @Singleton
    abstract fun bindProductInterestRepository(
        repositoryImpl: ProductInterestRepositoryImpl,
    ): ProductInterestRepository
}
