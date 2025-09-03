package com.napzak.market.update.di

import com.napzak.market.update.repository.AppVersionRepository
import com.napzak.market.update.repositoryimpl.AppVersionRepositoryImpl
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
    abstract fun bindAppVersionRepository(
        appVersionRepositoryImpl: AppVersionRepositoryImpl
    ): AppVersionRepository
}
