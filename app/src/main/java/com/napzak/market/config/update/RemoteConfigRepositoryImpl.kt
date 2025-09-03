package com.napzak.market.config.update

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import kotlin.coroutines.resume
import com.napzak.market.update.repository.RemoteConfigRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

private const val ANDROID_APP_VERSION = "android_app_version"

class RemoteConfigRepositoryImpl @Inject constructor() : RemoteConfigRepository {
    override suspend fun getFirebaseRemoteConfig() : String {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // TODO : 시간 바꾸기
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf(ANDROID_APP_VERSION to "0.0.0"))

        val appVersion = suspendCancellableCoroutine<String> { cont ->
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val version = remoteConfig.getString(ANDROID_APP_VERSION)
                    cont.resume(version)
                } else {
                    cont.resumeWithException(Exception("RemoteConfig fetch failed"))
                }
            }
        }

        return appVersion
    }
}