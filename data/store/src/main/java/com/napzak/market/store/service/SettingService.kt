package com.napzak.market.store.service

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.store.dto.SettingInfoResponse
import retrofit2.http.GET

interface SettingService {

    @GET("api/v1/stores/mypage/setting")
    suspend fun getSettingInfo(
    ): BaseResponse<SettingInfoResponse>
}