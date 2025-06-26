package com.napzak.market.chat.chatroom.model

internal enum class ChatDirection {
    SENT, RECEIVED;

    companion object {
        fun from(value: String): ChatDirection = runCatching {
            ChatDirection.valueOf(value.uppercase())
        }.getOrElse { SENT }
    }
}