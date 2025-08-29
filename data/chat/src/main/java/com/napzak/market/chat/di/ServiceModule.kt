package com.napzak.market.chat.di

import com.napzak.market.chat.service.ChatRoomService
import com.napzak.market.chat.service.ChatService
import com.napzak.market.chat.service.ChatSocketService
import com.napzak.market.chat.service.ChatSocketServiceImpl
import com.napzak.market.remote.socket.StompWebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideChatRoomService(retrofit: Retrofit): ChatRoomService = retrofit.create()

    @Provides
    @Singleton
    fun provideChatService(retrofit: Retrofit): ChatService = retrofit.create()

    @Provides
    @Singleton
    fun provideChatSocketService(
        stompWebSocketClient: StompWebSocketClient,
    ): ChatSocketService = ChatSocketServiceImpl(stompWebSocketClient)
}
