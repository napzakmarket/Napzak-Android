package com.napzak.market.store.repository

import com.napzak.market.store.model.SettingInfo

interface SettingRepository {
    suspend fun fetchSettingInfo(): Result<SettingInfo>
}