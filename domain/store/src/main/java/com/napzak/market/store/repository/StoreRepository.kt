package com.napzak.market.store.repository

import com.napzak.market.store.model.Genre
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.model.StoreEditProfile
import com.napzak.market.store.model.StoreInfo
import com.napzak.market.store.model.TermsAgreement
import com.napzak.market.store.model.UserWithdrawal

interface StoreRepository {
    suspend fun getValidateNickname(nickname: String): Result<Unit>

    suspend fun postRegisterNickname(nickname: String): Result<Unit>

    suspend fun postRegisterGenres(genreIds: List<Long>): Result<Genre>

    suspend fun logout(): Result<Unit>

    suspend fun withdraw(title: String, description: String?): Result<UserWithdrawal>

    suspend fun fetchStoreInfo(): Result<StoreInfo>

    suspend fun fetchEditProfile(): Result<StoreEditProfile>

    suspend fun fetchStoreDetail(storeId: Long): Result<StoreDetail>

    suspend fun updateEditProfile(request: StoreEditProfile): Result<StoreEditProfile>

    suspend fun getTermsAgreement() : Result<TermsAgreement>

    suspend fun postTermsAgreement(bundleId: Int): Result<Unit>

    suspend fun blockStore(storeId: Long): Result<Unit>

    suspend fun unblockStore(storeId: Long): Result<Unit>
}