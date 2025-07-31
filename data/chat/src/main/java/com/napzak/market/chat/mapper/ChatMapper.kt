package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.model.SendMessage

fun SendMessage<*>.toRequest(): ChatMessageRequest {
    return when (this) {
        is SendMessage.Text -> toRequest()
        is SendMessage.Image -> toRequest()
        is SendMessage.Product -> toRequest()
    }
}

private fun SendMessage.Text.toRequest() = ChatMessageRequest(
    roomId = roomId,
    type = "TEXT",
    content = content,
    metadata = null,
)


private fun SendMessage.Image.toRequest() = ChatMessageRequest(
    roomId = roomId,
    type = "IMAGE",
    content = null,
    metadata = ChatMessageMetadata.Image(
        imageUrls = imageUrls,
    ),
)

private fun SendMessage.Product.toRequest() = ChatMessageRequest(
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