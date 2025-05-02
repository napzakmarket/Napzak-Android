package com.napzak.market.store.datasource

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.store.dto.request.GenreRegistrationRequest
import com.napzak.market.store.dto.request.NicknameRequest
import com.napzak.market.store.dto.request.WithdrawRequest
import com.napzak.market.store.dto.response.GenreRegistrationResponse
import com.napzak.market.store.dto.response.WithdrawResponse
import com.napzak.market.store.service.StoreService
import javax.inject.Inject

class StoreDataSource @Inject constructor(
    private val storeService: StoreService,
) {
    suspend fun getValidateNickname(nickname: String): BaseResponse<Unit> {
        return storeService.getNicknameValidation(NicknameRequest(nickname))
    }

    suspend fun postRegistrationNickname(nickname: String): BaseResponse<Unit> {
        return storeService.postNicknameRegistration(NicknameRequest(nickname))
    }

    suspend fun getRegistrationGenre(genreIds: List<Long>): BaseResponse<GenreRegistrationResponse> {
        return storeService.getGenresRegistration(GenreRegistrationRequest(genreIds))
    }

    suspend fun logout(): BaseResponse<Unit> {
        return storeService.postLogout()
    }

    suspend fun withdraw(
        title: String,
        description: String?,
    ): BaseResponse<WithdrawResponse> {
        return storeService.postWithdraw(WithdrawRequest(title, description))
    }
}