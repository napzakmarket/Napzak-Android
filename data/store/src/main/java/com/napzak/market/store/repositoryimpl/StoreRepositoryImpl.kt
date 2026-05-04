package com.napzak.market.store.repositoryimpl

import com.napzak.market.store.datasource.StoreDataSource
import com.napzak.market.store.dto.request.CodeVerificationRequest
import com.napzak.market.store.dto.request.GenreRegistrationRequest
import com.napzak.market.store.dto.request.NicknameRequest
import com.napzak.market.store.dto.request.PhoneCodeRequest
import com.napzak.market.store.dto.request.WithdrawRequest
import com.napzak.market.store.mapper.toDomain
import com.napzak.market.store.mapper.toRequest
import com.napzak.market.store.model.Genre
import com.napzak.market.store.model.PhoneCodeVerificationResult
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.model.StoreEditProfile
import com.napzak.market.store.model.StoreInfo
import com.napzak.market.store.model.TermsAgreement
import com.napzak.market.store.model.UserWithdrawal
import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeDataSource: StoreDataSource
) : StoreRepository {

    override suspend fun getValidateNickname(nickname: String): Result<Unit> = runCatching {
        storeDataSource.getValidateNickname(nickname)
        Unit
    }

    override suspend fun postRegisterNickname(nickname: String): Result<Unit> = runCatching {
        storeDataSource.postRegistrationNickname(NicknameRequest(nickname))
        Unit
    }

    override suspend fun postRegisterGenres(genreIds: List<Long>): Result<Genre> = runCatching {
        storeDataSource.postRegistrationGenre(GenreRegistrationRequest(genreIds)).data.genreList.first()
            .toDomain()
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        storeDataSource.logout()
        Unit
    }

    override suspend fun withdraw(title: String, description: String?): Result<UserWithdrawal> =
        runCatching {
            storeDataSource.withdraw(WithdrawRequest(title, description)).data.toDomain()
        }

    override suspend fun fetchStoreInfo(): Result<StoreInfo> = runCatching {
        storeDataSource.getStoreInfo().toDomain()
    }

    override suspend fun fetchEditProfile(): Result<StoreEditProfile> = runCatching {
        storeDataSource.getEditProfile().toDomain()
    }

    override suspend fun fetchStoreDetail(storeId: Long): Result<StoreDetail> = runCatching {
        storeDataSource.getStoreDetail(storeId).toDomain()
    }

    override suspend fun updateEditProfile(request: StoreEditProfile): Result<StoreEditProfile> =
        runCatching {
            storeDataSource.updateEditProfile(request.toRequest()).toDomain()
        }

    override suspend fun getTermsAgreement(): Result<TermsAgreement> = runCatching {
        storeDataSource.getTermsAgreement().toDomain()
    }

    override suspend fun postTermsAgreement(bundleId: Int): Result<Unit> = runCatching {
        storeDataSource.postTermsAgreement(bundleId)
    }

    override suspend fun blockStore(storeId: Long): Result<Unit> = runCatching {
        storeDataSource.blockStore(storeId)
    }

    override suspend fun unblockStore(storeId: Long): Result<Unit> = runCatching {
        storeDataSource.unblockStore(storeId)
    }

    override suspend fun getPhoneVerificationStatus(): Result<Boolean> = runCatching {
        storeDataSource.getPhoneVerificationStatus().toDomain()
    }

    override suspend fun postPhoneVerificationCode(phoneNumber: String): Result<Int> = runCatching {
        storeDataSource.postPhoneVerificationCode(
            request = PhoneCodeRequest(phoneNumber)
        ).toDomain()
    }

    override suspend fun checkPhoneVerificationCode(
        phoneNumber: String,
        code: String
    ): Result<PhoneCodeVerificationResult> = runCatching {
        storeDataSource.checkPhoneVerificationCode(
            request = CodeVerificationRequest(phoneNumber, code)
        ).toDomain()
    }
}
