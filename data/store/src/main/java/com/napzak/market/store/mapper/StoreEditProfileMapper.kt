package com.napzak.market.store.mapper

import com.napzak.market.store.dto.StoreEditGenreDto
import com.napzak.market.store.dto.StoreEditProfileRequest
import com.napzak.market.store.dto.StoreEditProfileResponse
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