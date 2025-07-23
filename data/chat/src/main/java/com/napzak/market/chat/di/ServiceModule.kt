package com.napzak.market.chat.di

import com.napzak.market.chat.service.ChatRoomService
import com.napzak.market.chat.service.ChatService
import com.napzak.market.remote.qualifier.Chat
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
    fun provideChatRoomService(@Chat retrofit: Retrofit): ChatRoomService = retrofit.create()

    @Provides
    @Singleton
    fun provideChatService(@Chat retrofit: Retrofit): ChatService = retrofit.create()
}
