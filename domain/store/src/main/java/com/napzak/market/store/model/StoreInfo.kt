package com.napzak.market.store.model

data class StoreInfo(
    val storeId: Long,
    val nickname: String,
    val photoUrl: String,
    val salesCount: Int,
    val purchaseCount: Int,
    val serviceLink: String
)