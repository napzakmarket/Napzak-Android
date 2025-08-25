package com.napzak.market.store.di

import com.napzak.market.store.repository.AuthRepository
import com.napzak.market.store.repository.SettingRepository
import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.store.repositoryimpl.AuthRepositoryImpl
import com.napzak.market.store.repositoryimpl.SettingRepositoryImpl
import com.napzak.market.store.repositoryimpl.StoreRepositoryImpl
import com.napzak.market.store.repositoryimpl.TokenProviderImpl
import com.napzak.market.util.android.TokenProvider
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
    abstract fun bindStoreRepository(
        storeRepositoryImpl: StoreRepositoryImpl,
    ): StoreRepository

    @Binds
    @Singleton
    abstract fun bindSettingRepository(
        impl: SettingRepositoryImpl
    ): SettingRepository

    @Binds
    @Singleton
    abstract fun bindTokenReissuer(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindTokenProvider(
        impl: TokenProviderImpl
    ): TokenProvider
}