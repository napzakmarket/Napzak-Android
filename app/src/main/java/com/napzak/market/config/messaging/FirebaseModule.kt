package com.napzak.market.config.messaging

import com.napzak.market.notification.repository.FirebaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {
    @Binds
    abstract fun bindFirebaseRepository(
        impl: FirebaseRepositoryImpl,
    ): FirebaseRepository
}
