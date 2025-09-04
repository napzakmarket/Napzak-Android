package com.napzak.market.config

import com.napzak.market.config.messaging.FirebaseRepositoryImpl
import com.napzak.market.config.update.RemoteConfigRepositoryImpl
import com.napzak.market.notification.repository.FirebaseRepository
import com.napzak.market.update.repository.RemoteConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {
    @Binds
    @Singleton
    abstract fun bindFirebaseRepository(
        impl: FirebaseRepositoryImpl,
    ): FirebaseRepository

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRepository(
        impl: RemoteConfigRepositoryImpl,
    ): RemoteConfigRepository
}
