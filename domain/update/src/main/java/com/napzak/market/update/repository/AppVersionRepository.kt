package com.napzak.market.update.repository

interface AppVersionRepository {
    suspend fun getAppVersion(): String?
    suspend fun getFirebaseVersion(): String?
    suspend fun setAppVersion(version: String)
}
