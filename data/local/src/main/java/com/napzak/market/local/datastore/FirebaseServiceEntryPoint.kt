package com.napzak.market.local.datastore

import com.napzak.market.notification.usecase.UpdatePushTokenUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FirebaseServiceEntryPoint {
    fun dataStore(): TokenDataStore
    fun updatePushTokenUseCase(): UpdatePushTokenUseCase
}
