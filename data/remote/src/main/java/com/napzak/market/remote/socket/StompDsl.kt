package com.napzak.market.remote.socket

@DslMarker
annotation class StompMarker

@StompMarker
internal interface StompDsl {
    fun connect(block: ConnectBuilder.() -> Unit)
    fun subscribe(block: SubscribeBuilder.() -> Unit)
    fun send(block: SendBuilder.() -> Unit)
    fun disconnect(block: DisconnectBuilder.() -> Unit = {})
}

internal class StompDslImpl : StompDsl {
    private val frames = mutableListOf<String>()
    override fun connect(block: ConnectBuilder.() -> Unit) {
        frames += ConnectBuilder().apply(block).build()
    }

    override fun subscribe(block: SubscribeBuilder.() -> Unit) {
        frames += SubscribeBuilder().apply(block).build()
    }

    override fun send(block: SendBuilder.() -> Unit) {
        frames += SendBuilder().apply(block).build()
    }

    override fun disconnect(block: DisconnectBuilder.() -> Unit) {
        frames += DisconnectBuilder().apply(block).build()
    }

    fun build(): String = frames.joinToString("")
}

private const val END = "\u0000"

private fun headerLine(k: String, v: String) = "$k:$v\n"
private fun header(cmd: String, headers: Map<String, String>) =
    buildString {
        append(cmd).append('\n')
        headers.forEach { (k, v) -> append(headerLine(k, v)) }
        append('\n')
    }

internal class ConnectBuilder {
    var host: String = "/"
    var acceptVersion: List<String> = listOf("1.1", "1.0")
    var heartBeat: Pair<Int, Int>? = null
    val headers = mutableMapOf<String, String>()

    fun build(): String {
        require(host.isNotBlank())
        val h = linkedMapOf(
            "host" to host,
            "accept-version" to acceptVersion.joinToString(","),
        ).apply {
            heartBeat?.let { put("heart-beat", "${it.first},${it.second}") }
            putAll(headers)
        }

        return header("CONNECT", h) + END
    }
}

internal class SubscribeBuilder {
    var destination: String = ""
    var id: String = ""
    var receipt: Boolean = false
    val headers = mutableMapOf<String, String>()

    fun build(): String {
        require(destination.isNotBlank()) { "destination required" }
        val subId = id.ifBlank { "sub-${System.nanoTime()}" }
        val h = linkedMapOf(
            "id" to subId,
            "destination" to destination
        ).apply {
            if (receipt) put("receipt", "rcpt-$subId")
            putAll(headers)
        }
        return header("SUBSCRIBE", h) + END
    }
}

internal class SendBuilder {
    var destination: String = ""
    var contentType: String? = null
    var body: String? = null
    val headers = mutableMapOf<String, String>()

    fun build(): String {
        require(destination.isNotBlank())
        val bytes = body?.toByteArray(Charsets.UTF_8)
        val h = linkedMapOf("destination" to destination).apply {
            contentType?.let { put("content-type", it) }
            bytes?.let { put("content-length", it.size.toString()) }
            putAll(headers)
        }
        return header("SEND", h) + (body ?: "") + END
    }
}

internal class DisconnectBuilder {
    val headers = mutableMapOf<String, String>()
    fun build(): String = header("DISCONNECT", headers) + END
}