package com.napzak.market.store.repository

import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.model.StoreEditProfile
import com.napzak.market.store.model.StoreInfo

interface StoreRepository {
    suspend fun fetchStoreInfo(token: String): Result<StoreInfo>

    suspend fun fetchEditProfile(token: String): Result<StoreEditProfile>

    suspend fun fetchStoreDetail(token: String): Result<StoreDetail>

    suspend fun updateEditProfile(): Result<StoreEditProfile>}