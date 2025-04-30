package com.napzak.market.store.datasource

import com.napzak.market.store.dto.StoreResponse
import com.napzak.market.store.service.StoreService
import javax.inject.Inject

class StoreDataSource @Inject constructor(
    private val storeService: StoreService
) {
    suspend fun getStoreInfo(): StoreResponse {
        return storeService.getStoreInfo().data
    }
}