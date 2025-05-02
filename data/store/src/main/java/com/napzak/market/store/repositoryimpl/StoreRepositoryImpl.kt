package com.napzak.market.store.repositoryimpl

import com.napzak.market.store.datasource.StoreDataSource
import com.napzak.market.store.dto.response.GenreRegistrationResponse
import com.napzak.market.store.dto.response.WithdrawResponse
import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeDataSource: StoreDataSource
) : StoreRepository {

    override suspend fun getValidateNickname(nickname: String): Result<Unit> = runCatching {
        val response = storeDataSource.getValidateNickname(nickname)
        if (response.isSuccess) Unit else throw Exception(response.message)
    }

    override suspend fun postRegisterNickname(nickname: String): Result<Unit> = runCatching {
        val response = storeDataSource.postRegistrationNickname(nickname)
        if (response.isSuccess) Unit else throw Exception(response.message)
    }

    override suspend fun postRegisterGenres(genreIds: List<Long>): Result<GenreRegistrationResponse> = runCatching {
        val response = storeDataSource.getRegistrationGenre(genreIds)
        if (response.isSuccess) response.data else throw Exception(response.message)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val response = storeDataSource.logout()
        if (response.isSuccess) Unit else throw Exception(response.message)
    }

    override suspend fun withdraw(title: String, description: String?): Result<WithdrawResponse> = runCatching {
        val response = storeDataSource.withdraw(title, description)
        if (response.isSuccess) response.data else throw Exception(response.message)
    }
}