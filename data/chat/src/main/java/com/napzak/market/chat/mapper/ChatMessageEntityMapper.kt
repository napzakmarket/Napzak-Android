package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.MessageItem
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.type.ChatMessageType
import com.napzak.market.local.room.type.ChatStatusType

fun MessageItem.toEntity(roomId: Long): ChatMessageEntity {
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
    message = metadata.productId.toString(),
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

fun ChatMessageEntity.toDomain(
    roomId: Long,
    product: ProductBrief? = null,
): ReceiveMessage<*> {
    return when (messageType) {
        ChatMessageType.TEXT -> toText(roomId)
        ChatMessageType.IMAGE -> toImage(roomId)
        ChatMessageType.PRODUCT -> product?.let { toProduct(roomId, product) }
            ?: toText(roomId) // TODO: 물품 메시지 파싱 에러 처리
        ChatMessageType.EXIT -> toNotice(roomId)
        ChatMessageType.REPORTED -> toNotice(roomId)
        ChatMessageType.WITHDRAWN -> toNotice(roomId)
        ChatMessageType.DATE -> toDate(roomId)
    }
}

private fun ChatMessageEntity.toText(roomId: Long): ReceiveMessage.Text =
    ReceiveMessage.Text(
        messageId = requireNotNull(messageId),
        roomId = roomId,
        senderId = requireNotNull(senderId),
        text = message ?: "",
        timeStamp = requireNotNull(createdAt),
        isRead = isRead,
        isMessageOwner = isMessageOwner,
        isProfileNeeded = isProfileNeeded,
    )

private fun ChatMessageEntity.toImage(
    roomId: Long,
): ReceiveMessage.Image =
    ReceiveMessage.Image(
        messageId = requireNotNull(messageId),
        roomId = roomId,
        senderId = requireNotNull(senderId),
        imageUrl = message ?: "",
        timeStamp = requireNotNull(createdAt),
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
        messageId = messageId ?: throw IllegalArgumentException(),
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
        messageId = requireNotNull(messageId),
        roomId = roomId,
        senderId = senderId,
        notice = message ?: "",
        timeStamp = requireNotNull(createdAt),
    )

private fun ChatMessageEntity.toDate(
    roomId: Long,
): ReceiveMessage.Date =
    ReceiveMessage.Date(
        messageId = requireNotNull(messageId),
        roomId = roomId,
        date = message ?: "",
        timeStamp = requireNotNull(createdAt),
    )
