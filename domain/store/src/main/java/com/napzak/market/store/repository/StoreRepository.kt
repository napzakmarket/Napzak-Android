package com.napzak.market.store.repository

import com.napzak.market.store.model.StoreInfo

interface StoreRepository {
    suspend fun fetchStoreInfo(token: String): Result<StoreInfo>
}