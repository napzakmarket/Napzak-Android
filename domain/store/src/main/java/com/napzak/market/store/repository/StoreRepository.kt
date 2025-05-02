package com.napzak.market.store.repository

import com.napzak.market.store.dto.response.GenreRegistrationResponse
import com.napzak.market.store.dto.response.WithdrawResponse

interface StoreRepository {
    suspend fun getValidateNickname(nickname: String): Result<Unit>
    suspend fun postRegisterNickname(nickname: String): Result<Unit>
    suspend fun postRegisterGenres(genreIds: List<Long>): Result<GenreRegistrationResponse>
    suspend fun logout(): Result<Unit>
    suspend fun withdraw(title: String, description: String?): Result<WithdrawResponse>
}