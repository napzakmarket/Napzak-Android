package com.napzak.market.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FeatureDataStore @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>,
) {

    fun getShowProductStatusTooltipFlow(): Flow<Boolean?> = preferenceDataStore.data
        .map { it[showProductStatusTooltipKey]?.toBoolean() }

    suspend fun setShowProductStatusTooltip(value: Boolean) {
        preferenceDataStore.edit { preferences ->
            preferences[showProductStatusTooltipKey] = value.toString()
        }
    }

    companion object {
        private const val SHOW_PRODUCT_STATUS_TOOLTIP_KEY = "show_product_status_tool_tip"

        val showProductStatusTooltipKey = stringPreferencesKey(SHOW_PRODUCT_STATUS_TOOLTIP_KEY)
    }
}