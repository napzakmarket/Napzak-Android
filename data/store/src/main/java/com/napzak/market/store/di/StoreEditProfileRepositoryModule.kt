package com.napzak.market.store.di

import com.napzak.market.store.repository.StoreEditProfileRepository
import com.napzak.market.store.repositoryimpl.StoreEditProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StoreEditProfileRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStoreEditProfileRepository(
        impl: StoreEditProfileRepositoryImpl
    ): StoreEditProfileRepository
}