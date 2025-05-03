package com.napzak.market.presigned_url.di

import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import com.napzak.market.presigned_url.repository.PresignedUrlRepositoryImpl
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
        repositoryImpl: PresignedUrlRepositoryImpl,
    ): PresignedUrlRepository
}
