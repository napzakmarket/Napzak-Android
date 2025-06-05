package com.napzak.market.store.di

import com.napzak.market.store.repositoryimpl.StoreStateManagerImpl
import com.napzak.market.util.android.StoreStateManager
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
    fun provideStoreStateManager(): StoreStateManager = StoreStateManagerImpl()
}