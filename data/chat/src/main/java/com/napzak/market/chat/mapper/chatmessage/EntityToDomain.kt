package com.napzak.market.chat.mapper.chatmessage

import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.type.ChatMessageType

internal fun ChatMessageEntity.toDomain(
    roomId: Long,
    product: ProductBrief? = null,
): ReceiveMessage<*> {
    return when (messageType) {
        ChatMessageType.TEXT -> toText(roomId)
        ChatMessageType.IMAGE -> toImage(roomId)
        ChatMessageType.PRODUCT -> {
            product?.let { toProduct(roomId, product) }
                ?: toText(roomId)
        } // TODO: 물품 메시지 파싱 에러 처리
        ChatMessageType.EXIT,
        ChatMessageType.REPORTED,
        ChatMessageType.WITHDRAWN -> toNotice(roomId)

        ChatMessageType.DATE -> toDate(roomId)
    }
}

private fun ChatMessageEntity.toText(roomId: Long): ReceiveMessage.Text =
    ReceiveMessage.Text(
        messageId = messageId,
        roomId = roomId,
        senderId = requireNotNull(senderId),
        text = message ?: "",
        timeStamp = createdAt,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
        isProfileNeeded = isProfileNeeded,
    )

private fun ChatMessageEntity.toImage(
    roomId: Long,
): ReceiveMessage.Image =
    ReceiveMessage.Image(
        messageId = messageId,
        roomId = roomId,
        senderId = requireNotNull(senderId),
        imageUrl = message ?: "",
        timeStamp = createdAt,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
        isProfileNeeded = isProfileNeeded,
    )

private fun ChatMessageEntity.toProduct(
    roomId: Long,
    product: ProductBrief,
): ReceiveMessage.Product =
    ReceiveMessage.Product(
        roomId = roomId,
        messageId = messageId,
        senderId = requireNotNull(senderId),
        product = product,
        timeStamp = requireNotNull(createdAt),
        isRead = isRead,
        isMessageOwner = isMessageOwner,
        isProfileNeeded = isProfileNeeded,
    )

private fun ChatMessageEntity.toNotice(
    roomId: Long,
): ReceiveMessage.Notice =
    ReceiveMessage.Notice(
        messageId = messageId,
        roomId = roomId,
        senderId = senderId,
        notice = message ?: "",
        timeStamp = createdAt,
    )

private fun ChatMessageEntity.toDate(
    roomId: Long,
): ReceiveMessage.Date =
    ReceiveMessage.Date(
        messageId = messageId,
        roomId = roomId,
        date = message ?: "",
        timeStamp = createdAt,
    )