package com.napzak.market.chat

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import retrofit2.Retrofit
import java.nio.charset.StandardCharsets

abstract class ApiAbstract<T> {

    lateinit var mockWebServer: MockWebServer

    fun createMockServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    fun stopServer() {
        mockWebServer.shutdown()
    }

    fun enqueueResponse(fileName: String) {
        enqueueResponse(fileName, emptyMap())
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String>) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockResponse.setBody(source.readString(StandardCharsets.UTF_8))
        mockWebServer.enqueue(mockResponse)
    }

    fun createService(clazz: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                Json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(clazz)
    }
}