package com.napzak.market.store.di

import com.napzak.market.store.repository.SettingRepository
import com.napzak.market.store.repositoryimpl.SettingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSettingRepository(
        impl: SettingRepositoryImpl
    ): SettingRepository
}