package com.napzak.market.store.datasource

import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.remote.model.EmptyDataResponse
import com.napzak.market.store.dto.request.GenreRegistrationRequest
import com.napzak.market.store.dto.request.NicknameRequest
import com.napzak.market.store.dto.request.StoreEditProfileRequest
import com.napzak.market.store.dto.request.WithdrawRequest
import com.napzak.market.store.dto.response.GenreRegistrationResponse
import com.napzak.market.store.dto.response.StoreDetailResponse
import com.napzak.market.store.dto.response.StoreEditProfileResponse
import com.napzak.market.store.dto.response.StoreResponse
import com.napzak.market.store.dto.response.TermsResponse
import com.napzak.market.store.dto.response.WithdrawResponse
import com.napzak.market.store.service.StoreService
import javax.inject.Inject

class StoreDataSource @Inject constructor(
    private val storeService: StoreService,
) {
    suspend fun getValidateNickname(nickname: String): EmptyDataResponse {
        return storeService.getNicknameValidation(NicknameRequest(nickname))
    }

    suspend fun postRegistrationNickname(request: NicknameRequest): EmptyDataResponse {
        return storeService.postNicknameRegistration(request)
    }

    suspend fun postRegistrationGenre(request: GenreRegistrationRequest): BaseResponse<GenreRegistrationResponse> {
        return storeService.postGenresRegistration(request)
    }

    suspend fun logout(): EmptyDataResponse {
        return storeService.postLogout()
    }

    suspend fun withdraw(request: WithdrawRequest): BaseResponse<WithdrawResponse> {
        return storeService.postWithdraw(request)
    }

    suspend fun getStoreInfo(): StoreResponse {
        return storeService.getStoreInfo().data
    }

    suspend fun getStoreDetail(storeId: Long): StoreDetailResponse {
        return storeService.getStoreDetail(storeId).data
    }

    suspend fun getEditProfile(): StoreEditProfileResponse {
        return storeService.getStoreEditProfile().data
    }

    suspend fun updateEditProfile(request: StoreEditProfileRequest): StoreEditProfileResponse {
        return storeService.updateStoreProfile(request).data
    }

    suspend fun getTermsAgreement() : TermsResponse{
        return storeService.getTermsAgreement().data
    }

    suspend fun postTermsAgreement(bundleId: Int) : EmptyDataResponse{
        return storeService.postTermsAgreement(bundleId)
    }

    suspend fun blockStore(storeId: Long): EmptyDataResponse {
        return storeService.blockStore(storeId)
    }

    suspend fun unblockStore(storeId: Long): EmptyDataResponse {
        return storeService.unblockStore(storeId)
    }
}