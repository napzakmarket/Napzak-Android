package com.napzak.market.store.repository

import com.napzak.market.store.model.StoreEditProfile

interface StoreEditProfileRepository {
    suspend fun fetchEditProfile(token: String): Result<StoreEditProfile>
}