package com.napzak.market.store.model

import com.napzak.market.designsystem.component.bottomsheet.Genre

data class StoreInfo(
    val storeId: Long,
    val storeNickName: String,
    val storeDescription: String,
    val storePhoto: String,
    val storeCover: String,
    val isStoreOwner: Boolean,
    val genrePreferences: List<Genre>, // TODO: 추후 domain Genre로 변경
)
