package com.napzak.market.chat.model

data class ProductBrief(
    val productId: Long,
    val photo: String,
    val tradeType: String,
    val title: String,
    val price: Int,
    val isPriceNegotiable: Boolean,
    val genreName: String,
)