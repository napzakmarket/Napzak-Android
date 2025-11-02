package com.napzak.market.chat.mapper.chatmessage

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.MessageItem
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.entity.ChatProductEntity
import com.napzak.market.local.room.type.ChatMessageType
import com.napzak.market.local.room.type.ChatStatusType

internal fun MessageItem.toEntity(roomId: Long): ChatMessageEntity {
    return when (metadata) {
        null -> toTextEntity(roomId)
        is ChatMessageMetadata.Image -> toImageEntity(roomId, metadata)
        is ChatMessageMetadata.Product -> toProductEntity(roomId, metadata)
        is ChatMessageMetadata.EXIT -> toNoticeEntity(
            roomId,
            metadata.content,
            ChatMessageType.EXIT
        )

        is ChatMessageMetadata.REPORTED -> toNoticeEntity(
            roomId,
            metadata.content,
            ChatMessageType.REPORTED
        )

        is ChatMessageMetadata.WITHDRAWN -> toNoticeEntity(
            roomId,
            metadata.content,
            ChatMessageType.WITHDRAWN
        )

        is ChatMessageMetadata.Date -> toDateEntity(roomId, metadata)
    }
}

internal fun MessageItem.toProductEntity(): ChatProductEntity? {
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

private fun MessageItem.toTextEntity(roomId: Long): ChatMessageEntity =
    ChatMessageEntity(
        messageId = requireNotNull(messageId),
        roomId = roomId,
        senderId = requireNotNull(senderId),
        messageType = ChatMessageType.TEXT,
        message = content ?: "",
        createdAt = requireNotNull(createdAt),
        isRead = isRead ?: false,
        isMessageOwner = isMessageOwner ?: false,
        isProfileNeeded = isProfileNeeded ?: false,
        status = ChatStatusType.RECEIVED,
    )

private fun MessageItem.toImageEntity(
    roomId: Long,
    metadata: ChatMessageMetadata.Image
): ChatMessageEntity = ChatMessageEntity(
    messageId = requireNotNull(messageId),
    roomId = roomId,
    senderId = requireNotNull(senderId),
    messageType = ChatMessageType.IMAGE,
    message = metadata.imageUrls.firstOrNull { !it.isNullOrBlank() } ?: "",
    createdAt = requireNotNull(createdAt),
    isRead = isRead ?: false,
    isMessageOwner = isMessageOwner ?: false,
    isProfileNeeded = isProfileNeeded ?: false,
    status = ChatStatusType.RECEIVED,
)

private fun MessageItem.toProductEntity(
    roomId: Long,
    metadata: ChatMessageMetadata.Product
): ChatMessageEntity = ChatMessageEntity(
    messageId = requireNotNull(messageId),
    roomId = roomId,
    senderId = requireNotNull(senderId),
    messageType = ChatMessageType.PRODUCT,
    productId = metadata.productId,
    createdAt = requireNotNull(createdAt),
    isRead = isRead ?: false,
    isMessageOwner = isMessageOwner ?: false,
    isProfileNeeded = isProfileNeeded ?: false,
    status = ChatStatusType.RECEIVED,
)

private fun MessageItem.toNoticeEntity(
    roomId: Long,
    content: String,
    noticeType: ChatMessageType
): ChatMessageEntity = ChatMessageEntity(
    messageId = requireNotNull(messageId),
    roomId = roomId,
    senderId = senderId,
    messageType = noticeType,
    message = content,
    createdAt = requireNotNull(createdAt),
    isRead = true,
    status = ChatStatusType.RECEIVED,
)

private fun MessageItem.toDateEntity(
    roomId: Long,
    metadata: ChatMessageMetadata.Date
): ChatMessageEntity = ChatMessageEntity(
    messageId = requireNotNull(messageId),
    roomId = roomId,
    messageType = ChatMessageType.DATE,
    message = metadata.date,
    createdAt = requireNotNull(createdAt),
    isRead = true,
    status = ChatStatusType.RECEIVED,
)