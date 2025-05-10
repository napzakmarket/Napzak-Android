package com.napzak.market.store.mapper

import com.napzak.market.store.dto.response.GenreDto
import com.napzak.market.store.dto.response.WithdrawResponse
import com.napzak.market.store.model.Genre
import com.napzak.market.store.model.UserWithdrawal
import com.napzak.market.store.dto.response.StoreResponse
import com.napzak.market.store.dto.response.TermsResponse
import com.napzak.market.store.model.StoreInfo
import com.napzak.market.store.model.Terms
import com.napzak.market.store.model.TermsAgreement

fun GenreDto.toDomain(): Genre {
    return Genre(
        id = this.genreId,
        name = this.genreName,
    )
}

fun WithdrawResponse.toDomain(): UserWithdrawal {
    return UserWithdrawal(
        storeId = this.storeId,
        title = this.withdrawTitle,
        description = this.withdrawDescription,
    )
}

fun StoreResponse.toDomain(): StoreInfo = StoreInfo(
    storeId = storeId,
    nickname = storeNickName,
    photoUrl = storePhoto,
    salesCount = totalSellCount,
    purchaseCount = totalBuyCount,
    serviceLink = serviceLink,
)

fun TermsResponse.toDomain(): TermsAgreement = TermsAgreement(
    bundleId = bundleId,
    termList = termList.map { dto ->
        Terms(
            termsId = dto.termsId,
            termsTitle = dto.termsTitle,
            termsUrl = dto.termsUrl,
            isRequired = dto.isRequired,
        )
    }
)