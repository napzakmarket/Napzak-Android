package com.napzak.market.chat.model

data class ProductBrief(
    val productId: Long,
    val photo: String,
    val tradeType: String,
    val title: String,
    val price: Int,
    val isPriceNegotiable: Boolean,
    val genreName: String,
    val productOwnerId: Long,
    val isMyProduct: Boolean,
    val isProductDeleted: Boolean,
) {
    companion object {
        fun mock() = ProductBrief(
            productId = 1,
            genreName = "은혼",
            title = "은혼 긴토키 히지카타 룩업",
            photo = "",
            price = 129000,
            tradeType = "SELL",
            isPriceNegotiable = false,
            isMyProduct = false,
            productOwnerId = 1,
            isProductDeleted = false
        )
    }
}
