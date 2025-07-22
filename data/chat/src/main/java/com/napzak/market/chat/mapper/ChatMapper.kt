package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.model.Chat

fun Chat<*>.toRequest(): ChatMessageRequest {
    return when (this) {
        is Chat.Text -> toRequest()
        is Chat.Image -> toRequest()
        is Chat.Product -> toRequest()
    }
}

private fun Chat.Text.toRequest() = ChatMessageRequest(
    roomId = roomId,
    type = "TEXT",
    content = content,
    metadata = null,
)


private fun Chat.Image.toRequest() = ChatMessageRequest(
    roomId = roomId,
    type = "IMAGE",
    content = null,
    metadata = ChatMessageMetadata.Image(
        imageUrls = imageUrls,
    ),
)

private fun Chat.Product.toRequest() = ChatMessageRequest(
    roomId = roomId,
    type = "PRODUCT",
    content = null,
    metadata = ChatMessageMetadata.Product(
        tradeType = product.tradeType,
        productId = product.productId,
        genreName = product.genreName,
        title = product.title,
        price = product.price,
    ),
)