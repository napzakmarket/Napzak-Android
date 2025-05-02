package com.napzak.market.store.mapper

import com.napzak.market.store.dto.response.GenreDto
import com.napzak.market.store.dto.response.WithdrawResponse
import com.napzak.market.store.model.Genre
import com.napzak.market.store.model.UserWithdrawal

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