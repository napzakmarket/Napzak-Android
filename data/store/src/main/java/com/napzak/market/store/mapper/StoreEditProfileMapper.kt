package com.napzak.market.store.mapper

import com.napzak.market.store.dto.response.StoreEditGenreDto
import com.napzak.market.store.dto.request.StoreEditProfileRequest
import com.napzak.market.store.dto.response.StoreEditProfileResponse
import com.napzak.market.store.model.StoreEditGenre
import com.napzak.market.store.model.StoreEditProfile

fun StoreEditProfileResponse.toDomain(): StoreEditProfile = StoreEditProfile(
    coverUrl = storeCover,
    photoUrl = storePhoto,
    nickname = storeNickname,
    description = storeDescription,
    preferredGenres = preferredGenres.map { it.toDomain() },
)

fun StoreEditGenreDto.toDomain(): StoreEditGenre = StoreEditGenre(
    genreId = genreId,
    genreName = genreName,
)

fun StoreEditProfile.toRequest(): StoreEditProfileRequest =
    StoreEditProfileRequest(
        storeCover = this.coverUrl,
        storePhoto = this.photoUrl,
        storeNickName = this.nickname,
        storeDescription = this.description,
        preferredGenreList = this.preferredGenres.map { it.genreId }
    )