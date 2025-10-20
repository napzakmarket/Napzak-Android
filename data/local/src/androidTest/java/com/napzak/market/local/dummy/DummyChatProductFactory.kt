package com.napzak.market.local.dummy

import com.napzak.market.local.room.entity.ChatProductEntity

object DummyChatProductFactory {
    fun createEntity(
        productId: Long,
        tradeType: String = "tradeType",
        title: String = "product-${productId}",
        price: Int = 1000,
        genreName: String = "product-${productId}",
        isProductDeleted: Boolean = false,
        photo: String = "product-${productId}",
        isPriceNegotiable: Boolean = false,
        productOwnerId: Long = productId,
        isMyProduct: Boolean = false,
    ) = ChatProductEntity(
        productId = productId,
        tradeType = tradeType,
        title = title,
        price = price,
        genreName = genreName,
        isProductDeleted = isProductDeleted,
        photo = photo,
        isPriceNegotiable = isPriceNegotiable,
        productOwnerId = productOwnerId,
        isMyProduct = isMyProduct,
    )
}