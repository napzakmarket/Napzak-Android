package com.napzak.market.dummy.di

import com.napzak.market.dummy.repository.DummyRepository
import com.napzak.market.dummy.repositoryimpl.DummyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DummyRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDummyRepository(
        dummyRepositoryImpl: DummyRepositoryImpl
    ): DummyRepository
}