package com.napzak.market.chat.model

data class StoreBrief(
    val storeId: Long,
    val nickname: String,
    val storePhoto: String,
    val isWithdrawn: Boolean,
    val isReported: Boolean,
    val isOpponentStoreBlocked: Boolean,
    val isMyStoreBlocked: Boolean,
) {
    companion object {
        fun mock() = StoreBrief(
            storeId = 1,
            nickname = "납자기",
            storePhoto = "",
            isWithdrawn = false,
            isReported = false,
            isOpponentStoreBlocked = false,
            isMyStoreBlocked = false,
        )
    }
}
