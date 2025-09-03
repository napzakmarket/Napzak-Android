package com.napzak.market.remote.socket


internal interface StompFrameBuilder {
    fun build(block: StompDsl.() -> Unit): String
    fun decodeMessage(frame: String): String
}

internal class StompFrameManagerDslImpl : StompFrameBuilder {
    override fun build(block: StompDsl.() -> Unit): String =
        StompDslImpl().apply(block).build()

    override fun decodeMessage(frame: String): String {
        val parts = frame.split("\n\n", limit = 2)
        val bodyWithNull = parts.getOrNull(1) ?: return ""
        return bodyWithNull.trimEnd('\u0000', '\r', '\n')
    }
}

internal fun stompFrame(block: StompDsl.() -> Unit): String =
    StompFrameManagerDslImpl().build(block)

internal fun decodeMessage(frame: String): String =
    StompFrameManagerDslImpl().decodeMessage(frame)