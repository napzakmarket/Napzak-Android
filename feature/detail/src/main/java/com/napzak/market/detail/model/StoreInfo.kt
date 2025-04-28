package com.napzak.market.detail.model

// TODO: 도메인으로 이동
data class StoreInfo(
    val userId: Long,
    val storePhoto: String,
    val nickname: String,
    val totalSellCount: Int,
    val totalBuyCount: Int
) {
    companion object {
        val mock = StoreInfo(
            userId = 1,
            storePhoto = "",
            nickname = "납작한 자기",
            totalSellCount = 100,
            totalBuyCount = 100,
        )
    }
}