package com.napzak.market.remote.socket

import com.napzak.market.remote.socket.type.StompHeaderType
import com.napzak.market.remote.socket.type.StompHeaderType.CONTENT_LENGTH
import com.napzak.market.remote.socket.type.StompHeaderType.DESTINATION
import com.napzak.market.remote.socket.type.StompHeaderType.HOST
import com.napzak.market.remote.socket.type.StompHeaderType.ID
import com.napzak.market.remote.socket.type.StompType
import com.napzak.market.remote.socket.type.StompType.CONNECT
import com.napzak.market.remote.socket.type.StompType.DISCONNECT
import com.napzak.market.remote.socket.type.StompType.SEND
import com.napzak.market.remote.socket.type.StompType.SUBSCRIBE


interface StompFrameManager {
    fun connectFrame(host: String): String
    fun disconnectFrame(): String
    fun subscribeFrame(destination: String, id: String?): String
    fun sendFrame(
        message: String? = null,
        destination: String,
        headers: Map<StompHeaderType, String> = emptyMap()
    ): String

    fun decodeMessage(frame: String): String
}

class StompFrameManagerImpl : StompFrameManager {
    companion object {
        private const val END_OF_FRAME = "\u0000"
    }

    override fun connectFrame(
        host: String,
    ): String {
        val config = mapOf(
            HOST to host,
        ).toStringMap()

        val header = header(CONNECT, config)
        val body = body()
        return header + body
    }

    override fun disconnectFrame(): String {
        val header = header(DISCONNECT)
        val body = body()
        return header + body
    }

    override fun subscribeFrame(
        destination: String,
        id: String?,
    ): String {
        val config = buildMap {
            if (id != null) put(ID, id)
            put(DESTINATION, destination)
        }.toStringMap()

        val header = header(SUBSCRIBE, config)
        val body = body()
        return header + body
    }

    override fun sendFrame(
        message: String?,
        destination: String,
        headers: Map<StompHeaderType, String>,
    ): String {
        val bodyBytes = message?.toByteArray(Charsets.UTF_8)
        val contentLength = bodyBytes?.size
        val config = buildMap {
            putAll(headers)
            put(DESTINATION, destination)
            contentLength?.let { put(CONTENT_LENGTH, it.toString()) }
        }.toStringMap()

        val header = header(SEND, config)
        val body = body(message)
        return header + body
    }

    override fun decodeMessage(
        frame: String,
    ): String {
        val parts = frame.split("\n\n", limit = 2)
        val bodyWithNull = parts.getOrNull(1) ?: return ""
        return bodyWithNull.trimEnd('\u0000', '\r', '\n')
    }

    private fun Map<StompHeaderType, String>.toStringMap(): Map<String, String> {
        return mapKeys { it.key.value }
    }

    private fun header(
        type: StompType,
        config: Map<String, String> = emptyMap(),
    ): String {
        val newHeader = StringBuilder("${type.name}\n")
        config.forEach { (key, value) -> newHeader.append("$key:$value\n") }
        newHeader.append("\n")
        return newHeader.toString()
    }

    private fun body(
        message: String? = null,
    ): String {
        return StringBuilder()
            .apply { if (message != null) append(message) }
            .append(END_OF_FRAME)
            .toString()
    }
}
