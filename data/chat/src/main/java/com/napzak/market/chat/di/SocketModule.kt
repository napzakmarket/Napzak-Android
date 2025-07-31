package com.napzak.market.chat.di

import com.napzak.market.chat.client.ChatSocketClient
import com.napzak.market.chat.client.ChatSocketClientImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocketModule {
    @Binds
    @Singleton
    abstract fun bindChatSocketClient(
        chatSocketClientImpl: ChatSocketClientImpl,
    ): ChatSocketClient
}