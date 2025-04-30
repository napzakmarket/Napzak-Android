package com.napzak.market.store.repositoryimpl

import com.napzak.market.store.datasource.StoreDetailDataSource
import com.napzak.market.store.mapper.toDomain
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.repository.StoreDetailRepository
import javax.inject.Inject

class StoreDetailRepositoryImpl @Inject constructor(
    private val dataSource: StoreDetailDataSource
) : StoreDetailRepository {

    override suspend fun fetchStoreDetail(token: String): Result<StoreDetail> = runCatching {
        dataSource.getStoreDetail().toDomain()
    }
}