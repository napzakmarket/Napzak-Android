package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.ChatMessageResponse
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.ReceiveMessage

fun ChatMessageResponse.toDomain(): ReceiveMessage<*> {
    return when (metadata) {
        null -> toText()
        is ChatMessageMetadata.Image -> toImage(metadata)
        is ChatMessageMetadata.Product -> toProduct(metadata)
        is ChatMessageMetadata.System -> toNotice(metadata)
        is ChatMessageMetadata.Date -> toDate(metadata)
    }
}

private fun ChatMessageResponse.toText(): ReceiveMessage.Text =
    ReceiveMessage.Text(
        roomId = roomId,
        messageId = messageId,
        text = content ?: "",
        timeStamp = createdAt,
        isRead = isRead,
        isMessageOwner = false,
    )

private fun ChatMessageResponse.toImage(metadata: ChatMessageMetadata.Image): ReceiveMessage.Image =
    ReceiveMessage.Image(
        roomId = roomId,
        messageId = messageId,
        imageUrl = metadata.imageUrls.firstOrNull() ?: "",
        timeStamp = createdAt,
        isRead = isRead,
        isMessageOwner = false,
    )

private fun ChatMessageResponse.toProduct(metadata: ChatMessageMetadata.Product): ReceiveMessage.Product =
    ReceiveMessage.Product(
        roomId = roomId,
        messageId = messageId,
        product = ProductBrief(
            productId = metadata.productId,
            photo = "",
            tradeType = metadata.tradeType,
            title = metadata.title,
            price = metadata.price,
            isPriceNegotiable = false, // 미사용
            genreName = metadata.genreName,
        ),
        timeStamp = createdAt,
        isRead = isRead,
        isMessageOwner = false,
    )

private fun ChatMessageResponse.toNotice(metadata: ChatMessageMetadata.System): ReceiveMessage.Notice =
    ReceiveMessage.Notice(
        roomId = roomId,
        messageId = messageId,
        notice = metadata.content,
        timeStamp = "",
    )

private fun ChatMessageResponse.toDate(metadata: ChatMessageMetadata.Date): ReceiveMessage.Date =
    ReceiveMessage.Date(
        roomId = roomId,
        messageId = messageId,
        date = metadata.date,
        timeStamp = "",
    )