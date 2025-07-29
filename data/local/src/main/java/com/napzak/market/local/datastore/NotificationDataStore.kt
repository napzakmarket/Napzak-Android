package com.napzak.market.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationDataStore @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>,
) {
    suspend fun getPushToken(): String? = preferenceDataStore.data
        .map { it[preferencesPushTokenKey] }
        .firstOrNull()

    suspend fun getNotificationPermission(): Boolean? = preferenceDataStore.data
        .map { it[preferencesNotificationPermission] }
        .firstOrNull()

    suspend fun getNotificationModalShown(): Boolean? = preferenceDataStore.data
        .map { it[preferencesNotificationModalShown] }
        .firstOrNull()

    suspend fun setPushToken(pushToken: String) {
        preferenceDataStore.edit { preferences ->
            preferences[preferencesPushTokenKey] = pushToken
        }
    }

    suspend fun setNotificationPermission(permission: Boolean) {
        preferenceDataStore.edit { preferences ->
            preferences[preferencesNotificationPermission] = permission
        }
    }

    suspend fun clearPushToken() {
        preferenceDataStore.edit { preferences ->
            preferences.remove(preferencesPushTokenKey)
        }
    }

    suspend fun updateNotificationModalShown() {
        preferenceDataStore.edit { preferences ->
            preferences[preferencesNotificationModalShown] = true
        }
    }

    companion object {
        private const val PUSH_TOKEN_KEY = "push_token"
        private const val NOTIFICATION_PERMISSION_KEY = "notification_permission"
        private const val NOTIFICATION_MODAL_SHOWN_KEY = "notification_modal_shown"

        val preferencesPushTokenKey = stringPreferencesKey(PUSH_TOKEN_KEY)
        val preferencesNotificationPermission = booleanPreferencesKey(NOTIFICATION_PERMISSION_KEY)
        val preferencesNotificationModalShown = booleanPreferencesKey(NOTIFICATION_MODAL_SHOWN_KEY)
    }
}