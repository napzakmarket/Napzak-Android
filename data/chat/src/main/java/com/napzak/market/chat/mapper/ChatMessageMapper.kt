package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.ChatMessageResponse
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.ReceiveMessage

fun ChatMessageResponse.toDomain(receiverId: Long = 0): ReceiveMessage<*> {
    return when (metadata) {
        null -> toText(receiverId)
        is ChatMessageMetadata.Image -> toImage(receiverId, metadata)
        is ChatMessageMetadata.Product -> toProduct(receiverId, metadata)
        is ChatMessageMetadata.System -> toNotice(metadata)
        is ChatMessageMetadata.Date -> toDate(metadata)
    }
}

private fun ChatMessageResponse.toText(receiverId: Long): ReceiveMessage.Text =
    ReceiveMessage.Text(
        roomId = roomId,
        messageId = messageId,
        text = content ?: "",
        timeStamp = createdAt,
        isRead = isRead,
        isMessageOwner = senderId != receiverId,
    )

private fun ChatMessageResponse.toImage(
    receiverId: Long,
    metadata: ChatMessageMetadata.Image,
): ReceiveMessage.Image =
    ReceiveMessage.Image(
        roomId = roomId,
        messageId = messageId,
        imageUrl = metadata.imageUrls.firstOrNull() ?: "",
        timeStamp = createdAt,
        isRead = isRead,
        isMessageOwner = senderId != receiverId,
    )

private fun ChatMessageResponse.toProduct(
    receiverId: Long,
    metadata: ChatMessageMetadata.Product,
): ReceiveMessage.Product =
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
        isMessageOwner = senderId != receiverId,
    )

private fun ChatMessageResponse.toNotice(metadata: ChatMessageMetadata.System): ReceiveMessage.Notice =
    ReceiveMessage.Notice(
        roomId = roomId,
        messageId = messageId,
        notice = metadata.content,
        timeStamp = createdAt,
    )

private fun ChatMessageResponse.toDate(metadata: ChatMessageMetadata.Date): ReceiveMessage.Date =
    ReceiveMessage.Date(
        roomId = roomId,
        messageId = messageId,
        date = metadata.date,
        timeStamp = createdAt,
    )