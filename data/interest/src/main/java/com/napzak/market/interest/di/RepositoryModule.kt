package com.napzak.market.interest.di

import com.napzak.market.interest.repository.InterestProductRepository
import com.napzak.market.interest.repositoryimpl.InterestProductRepositoryImpl
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
    abstract fun bindInterestProductRepository(
        interestProductRepositoryImpl: InterestProductRepositoryImpl,
    ): InterestProductRepository
}