package com.napzak.market.store.model

data class StoreInfo(
    val storeId: Long,
    val storeNickname: String,
    val storePhoto: String,
    val totalSellCount: Int,
    val totalBuyCount: Int,
    val serviceLink: String,
)