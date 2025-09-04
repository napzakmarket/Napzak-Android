package com.napzak.market.config.update

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.napzak.market.update.repository.RemoteConfigRepository
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "RemoteConfigRepositoryImpl"
private const val ANDROID_APP_VERSION = "android_app_version"

class RemoteConfigRepositoryImpl @Inject constructor() : RemoteConfigRepository {
    override suspend fun getFirebaseRemoteConfig(): String {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // TODO : 시간 바꾸기
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf(ANDROID_APP_VERSION to "0.0.0"))

        return try {
            remoteConfig.fetchAndActivate().await()
            remoteConfig.getString(ANDROID_APP_VERSION)
        } catch (t: Throwable) {
            Timber.tag(TAG).d("Failed to get App Latest Version : $t")
            remoteConfig.getString(ANDROID_APP_VERSION)
        }
    }
}