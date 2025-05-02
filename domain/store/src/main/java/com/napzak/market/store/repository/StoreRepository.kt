package com.napzak.market.store.repository

import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.model.StoreEditProfile
import com.napzak.market.store.model.StoreInfo

interface StoreRepository {
    suspend fun fetchStoreInfo(): Result<StoreInfo>

    suspend fun fetchEditProfile(): Result<StoreEditProfile>

    suspend fun fetchStoreDetail(): Result<StoreDetail>

    suspend fun updateEditProfile(): Result<StoreEditProfile>
}