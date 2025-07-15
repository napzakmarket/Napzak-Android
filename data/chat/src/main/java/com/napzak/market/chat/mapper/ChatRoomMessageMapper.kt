package com.napzak.market.chat.mapper

import com.napzak.market.chat.dto.MessageItem
import com.napzak.market.chat.dto.MessageMetadata
import com.napzak.market.chat.model.ChatItem
import com.napzak.market.chat.model.ProductBrief

fun MessageItem.toDomain(): ChatItem<*> {
    return when (metadata) {
        null -> toText()
        is MessageMetadata.Image -> toImage(metadata)
        is MessageMetadata.Product -> toProduct(metadata)
        is MessageMetadata.System -> toNotice(metadata)
        is MessageMetadata.Date -> toDate(metadata)
    }
}

private fun MessageItem.toText(): ChatItem.Text =
    ChatItem.Text(
        messageId = messageId ?: throw IllegalArgumentException(),
        text = content ?: "",
        timeStamp = createdAt ?: "",
        isRead = isRead ?: false,
        isMessageOwner = isMessageOwner ?: false,
    )

private fun MessageItem.toImage(metadata: MessageMetadata.Image): ChatItem.Image =
    ChatItem.Image(
        messageId = messageId ?: throw IllegalArgumentException(),
        imageUrl = metadata.imageUrls.firstOrNull() ?: "",
        timeStamp = createdAt ?: "",
        isRead = isRead ?: false,
        isMessageOwner = isMessageOwner ?: false,
    )

private fun MessageItem.toProduct(metadata: MessageMetadata.Product): ChatItem.Product =
    ChatItem.Product(
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

private fun MessageItem.toNotice(metadata: MessageMetadata.System): ChatItem.Notice =
    ChatItem.Notice(
        messageId = messageId ?: throw IllegalArgumentException(),
        notice = metadata.content,
        timeStamp = "",
    )

private fun MessageItem.toDate(metadata: MessageMetadata.Date): ChatItem.Date =
    ChatItem.Date(
        messageId = messageId ?: throw IllegalArgumentException(),
        date = metadata.content,
        timeStamp = "",
    )
