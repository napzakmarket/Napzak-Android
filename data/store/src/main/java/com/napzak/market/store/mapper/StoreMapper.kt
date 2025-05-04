package com.napzak.market.store.mapper

import com.napzak.market.store.dto.response.GenreDto
import com.napzak.market.store.dto.response.WithdrawResponse
import com.napzak.market.store.model.Genre
import com.napzak.market.store.model.UserWithdrawal
import com.napzak.market.store.dto.response.StoreResponse
import com.napzak.market.store.model.StoreInfo

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
    nickname = storeNickname,
    photoUrl = storePhoto,
    salesCount = totalSellCount,
    purchaseCount = totalBuyCount,
    serviceLink = serviceLink,
)