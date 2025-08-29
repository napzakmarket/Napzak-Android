package com.napzak.market.remote.socket

import com.napzak.market.remote.qualifier.JWT
import com.napzak.market.remote.qualifier.Socket
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.remote.BuildConfig.WEBSOCKET_URL
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {

    @Socket
    @Provides
    @Singleton
    fun provideSocketRequest() = Request.Builder().url(WEBSOCKET_URL).build()

    @Socket
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
    }

    @Socket
    @Provides
    @Singleton
    fun provideSocketOkHttpClient(
        loggingInterceptor: Interceptor,
        @JWT headerInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(headerInterceptor)
        .callTimeout(Duration.ofMinutes(10))
        .pingInterval(Duration.ofSeconds(30))
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Singleton
    fun provideStompWebSocketClient(
        @Socket json: Json,
        @Socket request: Request,
        @Socket client: OkHttpClient,
    ): StompWebSocketClient = StompWebSocketClientImpl(json, request, client)

}