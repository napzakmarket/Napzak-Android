package com.napzak.market.store.datasource

import com.napzak.market.store.dto.StoreEditProfileResponse
import com.napzak.market.store.service.StoreEditProfileService
import javax.inject.Inject

class StoreEditProfileDataSource @Inject constructor(
    private val service: StoreEditProfileService
) {
    suspend fun getEditProfile(): StoreEditProfileResponse {
        return service.getStoreEditProfile().data
    }
}