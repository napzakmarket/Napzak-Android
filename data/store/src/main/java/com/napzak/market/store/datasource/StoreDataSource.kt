package com.napzak.market.store.datasource

import com.napzak.market.store.dto.StoreDetailResponse
import com.napzak.market.store.dto.StoreEditProfileResponse
import com.napzak.market.store.dto.StoreResponse
import com.napzak.market.store.service.StoreService
import javax.inject.Inject

class StoreDataSource @Inject constructor(
    private val storeService: StoreService
) {
    suspend fun getStoreInfo(): StoreResponse {
        return storeService.getStoreInfo().data
    }

    suspend fun getStoreDetail(): StoreDetailResponse {
        return storeService.getStoreDetail().data
    }

    suspend fun getEditProfile(): StoreEditProfileResponse {
        return storeService.getStoreEditProfile().data
    }

    suspend fun updateEditProfile(): StoreEditProfileResponse {
        return storeService.updateStoreProfile().data
    }
}