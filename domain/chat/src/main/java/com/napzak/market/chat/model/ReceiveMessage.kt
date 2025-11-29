package com.napzak.market.chat.model

sealed class ReceiveMessage<T>(
    open val messageId: Long,
    open val roomId: Long,
    open val senderId: Long?,
    open val message: T,
    open val timeStamp: String,
    open val isRead: Boolean = true,
    open val isMessageOwner: Boolean = false,
    open val isProfileNeeded: Boolean = false,
) {
    val isMessage: Boolean get() = this is Text || this is Image || this is Notice
    val isNeutralMessage: Boolean get() = this is Date || this is Notice
    val isSystemMessage: Boolean get() = this is Join || this is Leave

    data class Text(
        val text: String,
        override val messageId: Long,
        override val roomId: Long,
        override val senderId: Long,
        override val timeStamp: String,
        override val isRead: Boolean,
        override val isMessageOwner: Boolean,
        override val isProfileNeeded: Boolean,
    ) : ReceiveMessage<String>(
        message = text,
        messageId = messageId,
        roomId = roomId,
        senderId = senderId,
        timeStamp = timeStamp,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
        isProfileNeeded = isProfileNeeded,
    )

    data class Image(
        val imageUrl: String,
        override val messageId: Long,
        override val roomId: Long,
        override val senderId: Long,
        override val timeStamp: String,
        override val isRead: Boolean,
        override val isMessageOwner: Boolean,
        override val isProfileNeeded: Boolean,
    ) : ReceiveMessage<String>(
        message = imageUrl,
        messageId = messageId,
        roomId = roomId,
        senderId = senderId,
        timeStamp = timeStamp,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
        isProfileNeeded = isProfileNeeded,
    )

    data class Product(
        val product: ProductBrief,
        override val messageId: Long,
        override val roomId: Long,
        override val senderId: Long,
        override val timeStamp: String,
        override val isRead: Boolean,
        override val isMessageOwner: Boolean,
        override val isProfileNeeded: Boolean,
    ) : ReceiveMessage<ProductBrief>(
        message = product,
        messageId = messageId,
        roomId = roomId,
        senderId = senderId,
        timeStamp = timeStamp,
        isRead = isRead,
        isMessageOwner = isMessageOwner,
        isProfileNeeded = isProfileNeeded,
    ) {

    }

    data class Date(
        val date: String,
        override val messageId: Long,
        override val roomId: Long,
        override val timeStamp: String,
    ) : ReceiveMessage<String>(
        message = date,
        messageId = messageId,
        senderId = null,
        roomId = roomId,
        timeStamp = timeStamp,
    )

    data class Notice(
        val notice: String,
        override val messageId: Long,
        override val roomId: Long,
        override val senderId: Long?,
        override val timeStamp: String,
    ) : ReceiveMessage<String>(
        message = notice,
        messageId = messageId,
        roomId = roomId,
        senderId = senderId,
        timeStamp = timeStamp,
    )

    // TODO: 중복되는 값 초기화 제거
    data class Join(
        override val roomId: Long,
        override val senderId: Long?,
        override val isMessageOwner: Boolean,
    ) : ReceiveMessage<Unit>(
        messageId = 0,
        roomId = roomId,
        senderId = senderId,
        message = Unit,
        timeStamp = ""
    )

    data class Leave(
        override val roomId: Long,
        override val senderId: Long?,
        override val isMessageOwner: Boolean,
    ) : ReceiveMessage<Unit>(
        messageId = 0,
        roomId = roomId,
        senderId = senderId,
        message = Unit,
        timeStamp = ""
    )
}