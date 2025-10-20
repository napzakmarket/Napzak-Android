package com.napzak.market.local.room.type

enum class ChatMessageType {
    TEXT, IMAGE, PRODUCT, EXIT, REPORTED, WITHDRAWN, DATE;

    companion object {
        fun from(value: String?): ChatMessageType {
            return ChatMessageType.entries.find { it.name == value?.uppercase() } ?: TEXT
        }
    }
}