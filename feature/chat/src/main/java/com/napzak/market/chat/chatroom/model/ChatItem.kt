package com.napzak.market.chat.chatroom.model

import com.napzak.market.product.model.Product as ProductModel

internal sealed class ChatItem<T>(
    open val direction: ChatDirection?,
    open val message: T,
    open val timeStamp: String,
    open val isRead: Boolean,
) {
    data class Text(
        override val direction: ChatDirection,
        val text: String,
        override val timeStamp: String,
        override val isRead: Boolean,
    ) : ChatItem<String>(
        direction = direction,
        message = text,
        timeStamp = timeStamp,
        isRead = isRead,
    )

    data class Image(
        override val direction: ChatDirection,
        val imageUrl: String,
        override val timeStamp: String,
        override val isRead: Boolean,
    ) : ChatItem<String>(
        direction = direction,
        message = imageUrl,
        timeStamp = timeStamp,
        isRead = isRead,
    )

    data class Product(
        override val direction: ChatDirection,
        val product: ProductModel,
        override val timeStamp: String,
        override val isRead: Boolean,
    ) : ChatItem<ProductModel>(
        direction = direction,
        message = product,
        timeStamp = timeStamp,
        isRead = isRead,
    )

    data class Date(
        val date: String,
        override val timeStamp: String,
    ) : ChatItem<String>(
        direction = null,
        message = date,
        timeStamp = timeStamp,
        isRead = true
    )

    data class Notice(
        val notice: String,
        override val timeStamp: String,
    ) : ChatItem<String>(
        direction = null,
        message = notice,
        timeStamp = "",
        isRead = true
    )
}