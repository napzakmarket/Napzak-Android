package com.napzak.market.store.repository

import com.napzak.market.store.model.StoreDetail

interface StoreDetailRepository {
    suspend fun fetchStoreDetail(token: String): Result<StoreDetail>
}