package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.MessageItem
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.local.room.entity.ChatProductEntity

fun MessageItem.toProductEntity(): ChatProductEntity? {
    return if (metadata is ChatMessageMetadata.Product) {
        ChatProductEntity(
            productId = metadata.productId,
            tradeType = metadata.tradeType,
            title = metadata.title,
            price = metadata.price,
            genreName = metadata.genreName,
            isProductDeleted = metadata.isProductDeleted ?: false,
        )
    } else null
}

fun ChatProductEntity.toDomain(): ProductBrief = ProductBrief(
    productId = productId,
    tradeType = tradeType,
    title = title,
    price = price,
    genreName = genreName,
    isProductDeleted = isProductDeleted,
    isPriceNegotiable = isPriceNegotiable,
    photo = photo ?: "",
    productOwnerId = productOwnerId ?: 0L,
    isMyProduct = isMyProduct,
)