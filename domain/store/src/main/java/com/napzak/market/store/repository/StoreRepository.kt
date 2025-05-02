package com.napzak.market.store.repository

import com.napzak.market.store.model.Genre
import com.napzak.market.store.model.UserWithdrawal

interface StoreRepository {
    suspend fun getValidateNickname(nickname: String): Result<Unit>
    suspend fun postRegisterNickname(nickname: String): Result<Unit>
    suspend fun postRegisterGenres(genreIds: List<Long>): Result<Genre>
    suspend fun logout(): Result<Unit>
    suspend fun withdraw(title: String, description: String?): Result<UserWithdrawal>
}