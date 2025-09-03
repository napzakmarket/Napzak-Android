package com.napzak.market.remote.socket.type

enum class SocketMessageType {
    CONNECTED, MESSAGE, ERROR;

    companion object {
        fun from(text: String): SocketMessageType {
            val header = text
                .lineSequence()
                .firstOrNull()

            return when (header) {
                "CONNECTED" -> CONNECTED
                "MESSAGE" -> MESSAGE
                "ERROR" -> ERROR
                else -> throw IllegalArgumentException("Unknown message type: $header")
            }
        }
    }
}