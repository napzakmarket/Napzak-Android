package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.MessageItem
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.ReceiveMessage

/**
 * 채팅 메시지를 도메인 모델로 변환합니다.
 * @throws IllegalArgumentException 필수 인자들이 null인 경우
 */
fun MessageItem.toDomain(roomId: Long): ReceiveMessage<*> {
    return when (metadata) {
        null -> toText(roomId)
        is ChatMessageMetadata.Image -> toImage(roomId, metadata)
        is ChatMessageMetadata.Product -> toProduct(roomId, metadata)
        is ChatMessageMetadata.EXIT -> toNotice(roomId, metadata.content)
        is ChatMessageMetadata.REPORTED -> toNotice(roomId, metadata.content)
        is ChatMessageMetadata.WITHDRAWN -> toNotice(roomId, metadata.content)
        is ChatMessageMetadata.Date -> toDate(roomId, metadata)
    }
}

private fun MessageItem.toText(roomId: Long): ReceiveMessage.Text =
    ReceiveMessage.Text(
        messageId = requireNotNull(messageId),
        roomId = roomId,
        senderId = requireNotNull(senderId),
        text = content ?: "",
        timeStamp = requireNotNull(createdAt),
        isRead = isRead ?: false,
        isMessageOwner = isMessageOwner ?: false,
        isProfileNeeded = isProfileNeeded ?: false,
    )

private fun MessageItem.toImage(
    roomId: Long,
    metadata: ChatMessageMetadata.Image
): ReceiveMessage.Image =
    ReceiveMessage.Image(
        messageId = requireNotNull(messageId),
        roomId = roomId,
        senderId = requireNotNull(senderId),
        imageUrl = requireNotNull(metadata.imageUrls.firstOrNull { !it.isNullOrBlank() }),
        timeStamp = requireNotNull(createdAt),
        isRead = isRead ?: false,
        isMessageOwner = isMessageOwner ?: false,
        isProfileNeeded = isProfileNeeded ?: false,
    )

private fun MessageItem.toProduct(
    roomId: Long,
    metadata: ChatMessageMetadata.Product
): ReceiveMessage.Product =
    ReceiveMessage.Product(
        roomId = roomId,
        messageId = messageId ?: throw IllegalArgumentException(),
        senderId = requireNotNull(senderId),
        product = ProductBrief(
            productId = metadata.productId,
            tradeType = metadata.tradeType,
            title = metadata.title,
            price = metadata.price,
            genreName = metadata.genreName,
            isProductDeleted = metadata.isProductDeleted ?: false,
            // 사용되지 않는 속성들
            isPriceNegotiable = false,
            photo = "",
            productOwnerId = 0,
            isMyProduct = false,
        ),
        timeStamp = requireNotNull(createdAt),
        isRead = isRead ?: false,
        isMessageOwner = isMessageOwner ?: false,
        isProfileNeeded = isProfileNeeded ?: false,
    )

private fun MessageItem.toNotice(
    roomId: Long,
    content: String,
): ReceiveMessage.Notice =
    ReceiveMessage.Notice(
        messageId = requireNotNull(messageId),
        roomId = roomId,
        senderId = senderId,
        notice = content,
        timeStamp = requireNotNull(createdAt),
    )

private fun MessageItem.toDate(
    roomId: Long,
    metadata: ChatMessageMetadata.Date
): ReceiveMessage.Date =
    ReceiveMessage.Date(
        messageId = requireNotNull(messageId),
        roomId = roomId,
        date = metadata.date,
        timeStamp = requireNotNull(createdAt),
    )
