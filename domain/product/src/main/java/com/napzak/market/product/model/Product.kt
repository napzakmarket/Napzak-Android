package com.napzak.market.product.model

data class Product(
    val productId: Long,
    val genreName: String,
    val productName: String,
    val photo: String,
    val price: Int,
    val uploadTime: String,
    val isInterested: Boolean,
    val tradeType: String,
    val tradeStatus: String,
    val isPriceNegotiable: Boolean,
    val isOwnedByCurrentUser: Boolean,
    val interestCount: Int,
    val chatCount: Int,
)
