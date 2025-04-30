package com.napzak.market.store.repositoryimpl

import com.napzak.market.store.datasource.StoreDataSource
import com.napzak.market.store.mapper.toDomain
import com.napzak.market.store.model.StoreInfo
import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val dataSource: StoreDataSource
) : StoreRepository {
    override suspend fun fetchStoreInfo(token: String): Result<StoreInfo> = runCatching {
        dataSource.getStoreInfo().toDomain()
    }
}