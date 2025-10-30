package com.napzak.market.chat.mapper.chatmessage

import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.entity.ChatProductEntity
import com.napzak.market.local.room.type.ChatMessageType

internal fun ReceiveMessage<*>.toEntity(): ChatMessageEntity? = when (this) {
    is ReceiveMessage.Text -> toEntity()
    is ReceiveMessage.Image -> toEntity()
    is ReceiveMessage.Product -> toEntity()
    is ReceiveMessage.Notice -> toEntity()
    is ReceiveMessage.Date -> toEntity()
    else -> null // 채팅방 입장 및 나가기는 무시
}

internal fun ReceiveMessage.Product.toProductEntity() = with(product) {
    ChatProductEntity(
        productId = productId,
        photo = photo,
        tradeType = tradeType,
        title = title,
        price = price,
        isPriceNegotiable = isPriceNegotiable,
        genreName = genreName,
        productOwnerId = productOwnerId,
        isMyProduct = isMyProduct,
        isProductDeleted = isProductDeleted
    )
}

private fun ReceiveMessage.Text.toEntity() = ChatMessageEntity(
    messageId = messageId,
    roomId = roomId,
    senderId = senderId,
    messageType = ChatMessageType.TEXT,
    message = message,
    createdAt = timeStamp,
    isMessageOwner = isMessageOwner,
    isProfileNeeded = isProfileNeeded,
    isRead = isRead,
)

private fun ReceiveMessage.Image.toEntity() = ChatMessageEntity(
    messageId = messageId,
    roomId = roomId,
    senderId = senderId,
    messageType = ChatMessageType.IMAGE,
    message = message,
    createdAt = timeStamp,
    isMessageOwner = isMessageOwner,
    isProfileNeeded = isProfileNeeded,
    isRead = isRead,
)

private fun ReceiveMessage.Product.toEntity() = ChatMessageEntity(
    messageId = messageId,
    roomId = roomId,
    senderId = senderId,
    messageType = ChatMessageType.PRODUCT,
    productId = product.productId,
    createdAt = timeStamp,
    isMessageOwner = isMessageOwner,
    isProfileNeeded = isProfileNeeded,
    isRead = isRead,
)

private fun ReceiveMessage.Date.toEntity() = ChatMessageEntity(
    messageId = messageId,
    roomId = roomId,
    senderId = senderId,
    messageType = ChatMessageType.DATE,
    message = message,
    createdAt = timeStamp,
    isMessageOwner = isMessageOwner,
    isProfileNeeded = isProfileNeeded,
    isRead = isRead,
)

private fun ReceiveMessage.Notice.toEntity() = ChatMessageEntity(
    messageId = messageId,
    roomId = roomId,
    senderId = senderId,
    messageType = ChatMessageType.WITHDRAWN,
    message = message,
    createdAt = timeStamp,
)
