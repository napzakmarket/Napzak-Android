package com.napzak.market.store.datasource

import com.napzak.market.store.dto.StoreDetailResponse
import com.napzak.market.store.service.StoreDetailService
import javax.inject.Inject

class StoreDetailDataSource @Inject constructor(
    private val storeDetailService: StoreDetailService
) {
    suspend fun getStoreDetail(): StoreDetailResponse {
        return storeDetailService.getStoreDetail().data
    }
}