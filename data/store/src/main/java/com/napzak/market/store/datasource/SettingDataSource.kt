package com.napzak.market.store.datasource

import com.napzak.market.store.dto.SettingInfoResponse
import com.napzak.market.store.service.SettingService
import javax.inject.Inject

class SettingDataSource @Inject constructor(
    private val settingService: SettingService
) {
    suspend fun getSettingInfo(): SettingInfoResponse {
        return settingService.getSettingInfo().data
    }
}