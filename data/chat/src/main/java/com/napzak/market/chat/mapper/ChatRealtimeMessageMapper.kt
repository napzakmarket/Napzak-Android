package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.ChatRealtimeMessage
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.ReceiveMessage

/**
 * 실시간 채팅 메시지를 도메인 모델로 변환합니다.
 * @throws IllegalArgumentException 필수 인자들이 null인 경우
 */
fun ChatRealtimeMessage.toDomain(storeId: Long): ReceiveMessage<*> {
    return when (type) {
        "JOIN" -> toJoin(storeId)
        "LEAVE" -> toLeave(storeId)
        else -> when (metadata) {
            null -> toText(storeId)
            is ChatMessageMetadata.Image -> toImage(storeId, metadata)
            is ChatMessageMetadata.Product -> toProduct(storeId, metadata)
            is ChatMessageMetadata.EXIT -> toNotice(metadata.content)
            is ChatMessageMetadata.REPORTED -> toNotice(metadata.content)
            is ChatMessageMetadata.WITHDRAWN -> toNotice(metadata.content)
            is ChatMessageMetadata.Date -> toDate(metadata)
        }
    }
}

private fun ChatRealtimeMessage.toText(storeId: Long): ReceiveMessage.Text =
    ReceiveMessage.Text(
        messageId = requireNotNull(messageId),
        roomId = requireNotNull(roomId),
        senderId = requireNotNull(senderId),
        text = content ?: "",
        timeStamp = requireNotNull(createdAt),
        isRead = isRead ?: false,
        isMessageOwner = storeId == senderId,
        isProfileNeeded = false,
    )


private fun ChatRealtimeMessage.toImage(
    storeId: Long,
    metadata: ChatMessageMetadata.Image,
): ReceiveMessage.Image =
    ReceiveMessage.Image(
        messageId = requireNotNull(messageId),
        roomId = requireNotNull(roomId),
        senderId = requireNotNull(senderId),
        imageUrl = requireNotNull(metadata.imageUrls.firstOrNull()),
        timeStamp = requireNotNull(createdAt),
        isRead = isRead ?: false,
        isMessageOwner = storeId == senderId,
        isProfileNeeded = false,
    )

private fun ChatRealtimeMessage.toProduct(
    storeId: Long,
    metadata: ChatMessageMetadata.Product,
): ReceiveMessage.Product =
    ReceiveMessage.Product(
        messageId = requireNotNull(messageId),
        roomId = requireNotNull(roomId),
        senderId = requireNotNull(senderId),
        product = ProductBrief(
            productId = metadata.productId,
            tradeType = metadata.tradeType,
            title = metadata.title,
            price = metadata.price,
            genreName = metadata.genreName,
            // 사용되지 않는 속성들
            isPriceNegotiable = false,
            photo = "",
            productOwnerId = 0,
            isMyProduct = false,
            isProductDeleted = metadata.isProductDeleted ?: false,
        ),
        timeStamp = requireNotNull(createdAt),
        isRead = isRead ?: false,
        isMessageOwner = storeId == senderId,
        isProfileNeeded = false,
    )

private fun ChatRealtimeMessage.toNotice(content: String): ReceiveMessage.Notice =
    ReceiveMessage.Notice(
        messageId = requireNotNull(messageId),
        roomId = requireNotNull(roomId),
        senderId = senderId,
        notice = content,
        timeStamp = requireNotNull(createdAt),
    )

private fun ChatRealtimeMessage.toDate(metadata: ChatMessageMetadata.Date): ReceiveMessage.Date =
    ReceiveMessage.Date(
        messageId = requireNotNull(messageId),
        roomId = requireNotNull(roomId),
        date = metadata.date,
        timeStamp = requireNotNull(createdAt),
    )

private fun ChatRealtimeMessage.toJoin(storeId: Long): ReceiveMessage.Join =
    ReceiveMessage.Join(
        roomId = requireNotNull(roomId),
        senderId = requireNotNull(senderId),
        isMessageOwner = storeId == senderId,
    )

private fun ChatRealtimeMessage.toLeave(storeId: Long): ReceiveMessage.Leave =
    ReceiveMessage.Leave(
        roomId = requireNotNull(roomId),
        senderId = requireNotNull(senderId),
        isMessageOwner = storeId == senderId,
    )
