package com.napzak.market.chat.model

sealed class ReceiveMessage<T>(
    open val roomId: Long?,
    open val messageId: Long,
    open val message: T,
    open val timeStamp: String,
    open val isRead: Boolean,
    open val isMessageOwner: Boolean?,
) {
    data class Text(
        val text: String,
        override val roomId: Long?,
        override val messageId: Long,
        override val timeStamp: String,
        override val isRead: Boolean,
        override val isMessageOwner: Boolean,
    ) : ReceiveMessage<String>(
        message = text,
        roomId = roomId,
        messageId = messageId,
        timeStamp = timeStamp,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
    )

    data class Image(
        val imageUrl: String,
        override val roomId: Long?,
        override val messageId: Long,
        override val timeStamp: String,
        override val isRead: Boolean,
        override val isMessageOwner: Boolean,
    ) : ReceiveMessage<String>(
        message = imageUrl,
        roomId = roomId,
        messageId = messageId,
        timeStamp = timeStamp,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
    )

    data class Product(
        val product: ProductBrief,
        override val roomId: Long?,
        override val messageId: Long,
        override val timeStamp: String,
        override val isRead: Boolean,
        override val isMessageOwner: Boolean,
    ) : ReceiveMessage<ProductBrief>(
        message = product,
        roomId = roomId,
        messageId = messageId,
        timeStamp = timeStamp,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
    ) {

    }

    data class Date(
        val date: String,
        override val roomId: Long?,
        override val messageId: Long,
        override val timeStamp: String,
    ) : ReceiveMessage<String>(
        message = date,
        roomId = roomId,
        messageId = messageId,
        timeStamp = timeStamp,
        isRead = true,
        isMessageOwner = null,
    )

    data class Notice(
        val notice: String,
        override val roomId: Long?,
        override val messageId: Long,
        override val timeStamp: String,
    ) : ReceiveMessage<String>(
        message = notice,
        roomId = roomId,
        messageId = messageId,
        timeStamp = "",
        isRead = true,
        isMessageOwner = null,
    )
}