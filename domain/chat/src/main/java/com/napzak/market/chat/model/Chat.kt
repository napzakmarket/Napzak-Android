package com.napzak.market.chat.model

sealed class Chat<T>(
    open val roomId: Long,
    open val content: String?,
    open val metadata: T?,
) {
    data class Text(
        override val roomId: Long,
        override val content: String?,
    ) : Chat<String>(
        roomId = roomId,
        content = content,
        metadata = null,
    )

    data class Image(
        override val roomId: Long,
        override val content: String?,
        val imageUrls: List<String>,
    ) : Chat<List<String>>(
        roomId = roomId,
        content = content,
        metadata = imageUrls,
    )

    data class Product(
        override val roomId: Long,
        override val content: String?,
        val product: ProductBrief,
    ) : Chat<ProductBrief>(
        roomId = roomId,
        content = content,
        metadata = product,
    )
}