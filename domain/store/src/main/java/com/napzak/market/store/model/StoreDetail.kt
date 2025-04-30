package com.napzak.market.store.model

data class StoreDetail(
    val storeId: Long,
    val nickname: String,
    val description: String,
    val photoUrl: String,
    val coverUrl: String,
    val isOwner: Boolean,
    val genrePreferences: List<StoreDetailGenre>
)

data class StoreDetailGenre(
    val genreId: Long,
    val genreName: String
)