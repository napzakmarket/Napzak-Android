package com.napzak.market.store.repositoryimpl

import com.napzak.market.store.datasource.StoreDataSource
import com.napzak.market.store.dto.request.GenreRegistrationRequest
import com.napzak.market.store.dto.request.NicknameRequest
import com.napzak.market.store.dto.request.WithdrawRequest
import com.napzak.market.store.mapper.toDomain
import com.napzak.market.store.model.Genre
import com.napzak.market.store.model.UserWithdrawal
import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeDataSource: StoreDataSource
) : StoreRepository {

    override suspend fun getValidateNickname(nickname: String): Result<Unit> = runCatching {
        storeDataSource.getValidateNickname(NicknameRequest(nickname))
        Unit
    }

    override suspend fun postRegisterNickname(nickname: String): Result<Unit> = runCatching {
        storeDataSource.postRegistrationNickname(NicknameRequest(nickname))
        Unit
    }

    override suspend fun postRegisterGenres(genreIds: List<Long>): Result<Genre> = runCatching {
        val response = storeDataSource.getRegistrationGenre(GenreRegistrationRequest(genreIds))
        response.data.genreList.first().toDomain()
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        storeDataSource.logout()
        Unit
    }

    override suspend fun withdraw(title: String, description: String?): Result<UserWithdrawal> =
        runCatching {
            val response = storeDataSource.withdraw(WithdrawRequest(title, description))
            response.data.toDomain()
    }
}