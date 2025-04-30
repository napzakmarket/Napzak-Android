package com.napzak.market.store.di

import com.napzak.market.store.repository.StoreDetailRepository
import com.napzak.market.store.repositoryimpl.StoreDetailRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StoreDetailRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStoreDetailRepository(
        impl: StoreDetailRepositoryImpl
    ): StoreDetailRepository
}