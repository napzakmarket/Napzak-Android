package com.napzak.market.update.repository

interface RemoteConfigRepository {
    suspend fun getFirebaseRemoteConfig(): String
}
