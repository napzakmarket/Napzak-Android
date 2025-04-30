package com.napzak.market.store.model

data class StoreEditProfile(
    val coverUrl: String,
    val photoUrl: String,
    val nickname: String,
    val description: String,
    val preferredGenres: List<StoreEditGenre>
)

data class StoreEditGenre(
    val genreId: Long,
    val genreName: String
)