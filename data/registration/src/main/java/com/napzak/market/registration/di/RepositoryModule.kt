package com.napzak.market.registration.di

import com.napzak.market.registration.repository.RegistrationRepository
import com.napzak.market.registration.repository.RegistrationRepositoryImpl
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
    abstract fun bindRegistrationRepository(
        repositoryImpl: RegistrationRepositoryImpl,
    ): RegistrationRepository
}
