package com.napzak.market.store.repositoryimpl

import com.napzak.market.store.datasource.SettingDataSource
import com.napzak.market.store.mapper.toDomain
import com.napzak.market.store.model.SettingInfo
import com.napzak.market.store.repository.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val dataSource: SettingDataSource
) : SettingRepository {

    override suspend fun fetchSettingInfo(token: String): Result<SettingInfo> = runCatching {
        dataSource.getSettingInfo().toDomain()
    }
}