package com.napzak.market.chat.mapper.chatmessage

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.ChatRealtimeMessage
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.entity.ChatProductEntity
import com.napzak.market.local.room.type.ChatMessageType
import com.napzak.market.local.room.type.ChatMessageType.DATE
import com.napzak.market.local.room.type.ChatMessageType.EXIT
import com.napzak.market.local.room.type.ChatMessageType.IMAGE
import com.napzak.market.local.room.type.ChatMessageType.PRODUCT
import com.napzak.market.local.room.type.ChatMessageType.REPORTED
import com.napzak.market.local.room.type.ChatMessageType.TEXT
import com.napzak.market.local.room.type.ChatMessageType.WITHDRAWN


fun ChatRealtimeMessage.toEntity(storeId: Long): ChatMessageEntity {
    val messageId = messageId ?: 0
    return when (metadata) {
        is ChatMessageMetadata.Image -> toImage(storeId, messageId, metadata)
        is ChatMessageMetadata.Product -> toProduct(storeId, messageId, metadata)
        is ChatMessageMetadata.EXIT -> toNotice(messageId, metadata.content, EXIT)
        is ChatMessageMetadata.REPORTED -> toNotice(messageId, metadata.content, REPORTED)
        is ChatMessageMetadata.WITHDRAWN -> toNotice(messageId, metadata.content, WITHDRAWN)
        is ChatMessageMetadata.Date -> toDate(messageId, metadata)
        else -> toText(storeId, messageId)
    }
}

fun ChatMessageMetadata.Product.toProductEntity() =
    ChatProductEntity(
        productId = productId,
        tradeType = tradeType,
        title = title,
        price = price,
        isPriceNegotiable = false,
        genreName = genreName,
        isProductDeleted = isProductDeleted == true,
    )

private fun ChatRealtimeMessage.toText(
    storeId: Long,
    messageId: Long,
) = ChatMessageEntity(
    messageId = messageId,
    roomId = roomId,
    senderId = senderId,
    messageType = TEXT,
    message = content,
    createdAt = createdAt ?: "",
    isMessageOwner = senderId == storeId,
    isRead = isRead ?: false,
)

private fun ChatRealtimeMessage.toImage(
    storeId: Long,
    messageId: Long,
    metadata: ChatMessageMetadata.Image,
) = ChatMessageEntity(
    messageId = messageId,
    roomId = roomId,
    senderId = senderId,
    messageType = IMAGE,
    message = metadata.imageUrls.firstOrNull() ?: "",
    createdAt = createdAt ?: "",
    isMessageOwner = senderId == storeId,
    isRead = isRead ?: false,
)

private fun ChatRealtimeMessage.toProduct(
    storeId: Long,
    messageId: Long,
    metadata: ChatMessageMetadata.Product,
) = ChatMessageEntity(
    messageId = messageId,
    roomId = roomId,
    senderId = senderId,
    messageType = PRODUCT,
    productId = metadata.productId,
    createdAt = createdAt ?: "",
    isMessageOwner = senderId == storeId,
    isRead = isRead ?: false,
)

private fun ChatRealtimeMessage.toDate(
    messageId: Long,
    metadata: ChatMessageMetadata.Date,
) = ChatMessageEntity(
    messageId = messageId,
    roomId = roomId,
    messageType = DATE,
    message = metadata.date,
    createdAt = createdAt ?: ""
)

private fun ChatRealtimeMessage.toNotice(
    messageId: Long,
    content: String,
    messageType: ChatMessageType,
) = ChatMessageEntity(
    messageId = messageId,
    roomId = roomId,
    message = content,
    messageType = messageType,
    createdAt = createdAt ?: ""
)