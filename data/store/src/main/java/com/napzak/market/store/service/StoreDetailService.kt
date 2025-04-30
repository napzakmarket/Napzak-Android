package com.napzak.market.store.service

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.store.dto.StoreDetailResponse
import retrofit2.http.GET

interface StoreDetailService {

    @GET("api/v1/store/mypage")
    suspend fun getStoreDetail(
    ): BaseResponse<StoreDetailResponse>
}