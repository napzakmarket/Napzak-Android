package com.napzak.market.store.di

import com.napzak.market.store.repository.StoreStateManager
import com.napzak.market.store.repositoryimpl.StoreStateManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideUserStateManager(): StoreStateManager = StoreStateManagerImpl()
}