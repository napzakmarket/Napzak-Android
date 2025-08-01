package com.napzak.market.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.napzak.market.local.datastore.TokenDataStore
import com.napzak.market.remote.qualifier.Chat
import com.napzak.market.remote.qualifier.JWT
import com.napzak.market.remote.qualifier.NoAuth
import com.napzak.market.remote.qualifier.S3
import com.napzak.market.util.android.StoreStateManager
import com.napzak.market.util.android.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.remote.BuildConfig.BASE_URL
import data.remote.BuildConfig.CHAT_URL
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val APPLICATION_JSON = "application/json"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
        classDiscriminator = "type"
    }

    @Provides
    @Singleton
    fun provideJsonConverter(json: Json): Converter.Factory =
        json.asConverterFactory(APPLICATION_JSON.toMediaType())

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor { message ->
        when {
            message.isJsonObject() ->
                Timber.tag("okhttp").d(JSONObject(message).toString(4))

            message.isJsonArray() ->
                Timber.tag("okhttp").d(JSONObject(message).toString(4))

            else -> {
                Timber.tag("okhttp").d("CONNECTION INFO -> $message")
            }
        }
    }.apply {
        level = if (com.napzak.market.data.local.BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(
        tokenDataStore: TokenDataStore
    ): HeaderInterceptor = HeaderInterceptor(tokenDataStore)

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenProvider: TokenProvider,
        storeStateManager: StoreStateManager,
    ): AuthInterceptor = AuthInterceptor(
        tokenProvider = tokenProvider,
        storeStateManager = storeStateManager,
        reissueToken = {
            tokenProvider.reissueAccessToken()
        }
    )

    @JWT
    @Provides
    @Singleton
    fun provideJwtInterceptor(
        authInterceptor: AuthInterceptor,
    ): Interceptor = authInterceptor

    @JWT
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: Interceptor,
        @JWT headerInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(headerInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideNoTokenOkHttpClient(
        loggingInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideWebSocketClient(
        @JWT client: OkHttpClient,
    ): OkHttpWebSocketClient = OkHttpWebSocketClient(client)

    @Provides
    @Singleton
    fun provideRetrofit(
        @JWT client: OkHttpClient,
        factory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(factory)
        .build()

    @Provides
    @Chat
    @Singleton
    fun provideWebSocketRetrofit(
        @JWT client: OkHttpClient,
        factory: Converter.Factory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(CHAT_URL)
        .client(client)
        .addConverterFactory(factory)
        .build()

    @S3
    @Provides
    @Singleton
    fun provideS3Retrofit(
        client: OkHttpClient,
        factory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(factory)
        .build()

    @Provides
    @NoAuth
    @Singleton
    fun provideNoAuthRetrofit(
        factory: Converter.Factory,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(factory)
        .build()

    @Provides
    @Singleton
    fun provideSocketManager(
        webSocketClient: OkHttpWebSocketClient,
        json: Json
    ): StompSocketManager = StompSocketManagerImpl(webSocketClient, json)
}