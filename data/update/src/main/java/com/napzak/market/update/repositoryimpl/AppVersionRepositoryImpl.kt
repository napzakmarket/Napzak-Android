package com.napzak.market.update.repositoryimpl

import com.napzak.market.local.datastore.NapzakDataStore
import com.napzak.market.update.repository.AppVersionRepository
import javax.inject.Inject

class AppVersionRepositoryImpl @Inject constructor(
    private val napzakDataStore: NapzakDataStore,
) : AppVersionRepository {
    override suspend fun getAppVersion(): String? =
        napzakDataStore.getAppVersion()

    override suspend fun getFirebaseVersion(): String? =
        napzakDataStore.getFirebaseVersion()

    override suspend fun setAppVersion(version: String) {
        runCatching {
            napzakDataStore.setAppVersion(version)
        }
    }
}
