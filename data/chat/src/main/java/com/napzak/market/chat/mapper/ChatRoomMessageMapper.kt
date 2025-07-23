package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.MessageItem
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.ReceiveMessage

fun MessageItem.toDomain(roomId: Long): ReceiveMessage<*> {
    return when (metadata) {
        null -> toText(roomId)
        is ChatMessageMetadata.Image -> toImage(roomId, metadata)
        is ChatMessageMetadata.Product -> toProduct(roomId, metadata)
        is ChatMessageMetadata.System -> toNotice(roomId, metadata)
        is ChatMessageMetadata.Date -> toDate(roomId, metadata)
    }
}

private fun MessageItem.toText(roomId: Long): ReceiveMessage.Text =
    ReceiveMessage.Text(
        roomId = roomId,
        messageId = messageId ?: throw IllegalArgumentException(),
        text = content ?: "",
        timeStamp = createdAt ?: "",
        isRead = isRead ?: false,
        isMessageOwner = isMessageOwner ?: false,
    )

private fun MessageItem.toImage(
    roomId: Long,
    metadata: ChatMessageMetadata.Image
): ReceiveMessage.Image =
    ReceiveMessage.Image(
        roomId = roomId,
        messageId = messageId ?: throw IllegalArgumentException(),
        imageUrl = metadata.imageUrls.firstOrNull() ?: "",
        timeStamp = createdAt ?: "",
        isRead = isRead ?: false,
        isMessageOwner = isMessageOwner ?: false,
    )

private fun MessageItem.toProduct(
    roomId: Long,
    metadata: ChatMessageMetadata.Product
): ReceiveMessage.Product =
    ReceiveMessage.Product(
        roomId = roomId,
        messageId = messageId ?: throw IllegalArgumentException(),
        product = ProductBrief(
            productId = metadata.productId,
            photo = "",
            tradeType = metadata.tradeType,
            title = metadata.title,
            price = metadata.price,
            isPriceNegotiable = false, // 미사용
            genreName = metadata.genreName,
        ),
        timeStamp = createdAt ?: "",
        isRead = isRead ?: false,
        isMessageOwner = isMessageOwner ?: false,
    )

private fun MessageItem.toNotice(
    roomId: Long,
    metadata: ChatMessageMetadata.System
): ReceiveMessage.Notice =
    ReceiveMessage.Notice(
        roomId = roomId,
        messageId = messageId ?: throw IllegalArgumentException(),
        notice = metadata.content,
        timeStamp = "",
    )

private fun MessageItem.toDate(
    roomId: Long,
    metadata: ChatMessageMetadata.Date
): ReceiveMessage.Date =
    ReceiveMessage.Date(
        roomId = roomId,
        messageId = messageId ?: throw IllegalArgumentException(),
        date = metadata.date,
        timeStamp = "",
    )
