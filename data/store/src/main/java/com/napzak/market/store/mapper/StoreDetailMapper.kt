package com.napzak.market.store.mapper

import com.napzak.market.store.dto.StoreDetailGenreDto
import com.napzak.market.store.dto.StoreDetailResponse
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.model.StoreDetailGenre

fun StoreDetailResponse.toDomain(): StoreDetail = StoreDetail(
    storeId = storeId,
    nickname = storeNickname,
    description = storeDescription,
    photoUrl = storePhoto,
    coverUrl = storeCover,
    isOwner = isStoreOwner,
    genrePreferences = genrePreferences.map { it.toDomain() },
)

fun StoreDetailGenreDto.toDomain(): StoreDetailGenre = StoreDetailGenre(
    genreId = genreId,
    genreName = genreName,
)