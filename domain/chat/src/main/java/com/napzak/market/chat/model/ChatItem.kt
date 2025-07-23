package com.napzak.market.chat.model

sealed class ChatItem<T>(
    open val messageId: Long,
    open val message: T,
    open val timeStamp: String,
    open val isRead: Boolean,
    open val isMessageOwner: Boolean?,
) {
    data class Text(
        val text: String,
        override val messageId: Long,
        override val timeStamp: String,
        override val isRead: Boolean,
        override val isMessageOwner: Boolean,
    ) : ChatItem<String>(
        message = text,
        messageId = messageId,
        timeStamp = timeStamp,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
    )

    data class Image(
        val imageUrl: String,
        override val messageId: Long,
        override val timeStamp: String,
        override val isRead: Boolean,
        override val isMessageOwner: Boolean,
    ) : ChatItem<String>(
        message = imageUrl,
        messageId = messageId,
        timeStamp = timeStamp,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
    )

    data class Product(
        val product: ProductBrief,
        override val messageId: Long,
        override val timeStamp: String,
        override val isRead: Boolean,
        override val isMessageOwner: Boolean,
    ) : ChatItem<ProductBrief>(
        message = product,
        messageId = messageId,
        timeStamp = timeStamp,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
    ) {

    }

    data class Date(
        val date: String,
        override val messageId: Long,
        override val timeStamp: String,
    ) : ChatItem<String>(
        message = date,
        messageId = messageId,
        timeStamp = timeStamp,
        isRead = true,
        isMessageOwner = null,
    )

    data class Notice(
        val notice: String,
        override val messageId: Long,
        override val timeStamp: String,
    ) : ChatItem<String>(
        message = notice,
        messageId = messageId,
        timeStamp = "",
        isRead = true,
        isMessageOwner = null,
    )
}