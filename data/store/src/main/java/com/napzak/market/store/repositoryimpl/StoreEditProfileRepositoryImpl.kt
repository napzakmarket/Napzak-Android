package com.napzak.market.store.repositoryimpl

import com.napzak.market.store.datasource.StoreEditProfileDataSource
import com.napzak.market.store.mapper.toDomain
import com.napzak.market.store.model.StoreEditProfile
import com.napzak.market.store.repository.StoreEditProfileRepository
import javax.inject.Inject

class StoreEditProfileRepositoryImpl @Inject constructor(
    private val dataSource: StoreEditProfileDataSource
) : StoreEditProfileRepository {

    override suspend fun fetchEditProfile(token: String): Result<StoreEditProfile> = runCatching {
        dataSource.getEditProfile().toDomain()
    }
}