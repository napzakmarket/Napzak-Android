package com.napzak.market.store.repositoryimpl

import com.napzak.market.store.datasource.StoreDataSource
import com.napzak.market.store.mapper.toDomain
import com.napzak.market.store.mapper.toRequest
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.model.StoreEditProfile
import com.napzak.market.store.model.StoreInfo
import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val dataSource: StoreDataSource
) : StoreRepository {
    override suspend fun fetchStoreInfo(): Result<StoreInfo> = runCatching {
        dataSource.getStoreInfo().toDomain()
    }

    override suspend fun fetchEditProfile(): Result<StoreEditProfile> = runCatching {
        dataSource.getEditProfile().toDomain()
    }

    override suspend fun fetchStoreDetail(storeId: Long): Result<StoreDetail> = runCatching {
        dataSource.getStoreDetail(storeId).toDomain()
    }

    override suspend fun updateEditProfile(request: StoreEditProfile): Result<StoreEditProfile> = runCatching {
        dataSource.updateEditProfile(request.toRequest()).toDomain()
    }
}