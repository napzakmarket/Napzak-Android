package com.napzak.market.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NapzakDataStore @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>,
) {
    suspend fun getAppVersion(): String? = preferenceDataStore.data
        .map { it[preferencesAppVersionKey] }
        .firstOrNull()

    suspend fun getFirebaseVersion(version: String) = preferenceDataStore.data
        .map { it[preferencesFirebaseVersionKey] }
        .firstOrNull()

    suspend fun setAppVersion(version: String) {
        preferenceDataStore.edit { preferences ->
            preferences[preferencesAppVersionKey] = version
        }
    }

    suspend fun setFirebaseVersion(version: String) {
        preferenceDataStore.edit { preferences ->
            preferences[preferencesFirebaseVersionKey] = version
        }
    }

    companion object {
        private const val APP_VERSION_KEY = "app_version"
        private const val FIREBASE_VERSION_KEY = "firebase_version"

        val preferencesAppVersionKey = stringPreferencesKey(APP_VERSION_KEY)
        val preferencesFirebaseVersionKey = stringPreferencesKey(FIREBASE_VERSION_KEY)
    }
}
