package com.napzak.market.chat.model

data class StoreBrief(
    val storeId: Long,
    val nickname: String,
    val storePhoto: String,
    val isWithdrawn: Boolean,
)