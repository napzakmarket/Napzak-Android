package com.napzak.market.remote.socket.type

enum class SocketMessageType {
    CONNECTED, MESSAGE, ERROR;

    companion object {
        fun from(text: String): SocketMessageType {
            return when (val header = text.split("\n").firstOrNull()) {
                "CONNECTED" -> CONNECTED
                "MESSAGE" -> MESSAGE
                "ERROR" -> ERROR
                else -> throw IllegalArgumentException("Unknown message type: $header")
            }
        }
    }
}