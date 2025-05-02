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
    suspend fun getValidateNickname(request: NicknameRequest): BaseResponse<Unit> {
        return storeService.getNicknameValidation(request)
    }

    suspend fun postRegistrationNickname(request: NicknameRequest): BaseResponse<Unit> {
        return storeService.postNicknameRegistration(request)
    }

    suspend fun getRegistrationGenre(request: GenreRegistrationRequest): BaseResponse<GenreRegistrationResponse> {
        return storeService.getGenresRegistration(request)
    }

    suspend fun logout(): BaseResponse<Unit> {
        return storeService.postLogout()
    }

    suspend fun withdraw(request: WithdrawRequest): BaseResponse<WithdrawResponse> {
        return storeService.postWithdraw(request)
    }
}