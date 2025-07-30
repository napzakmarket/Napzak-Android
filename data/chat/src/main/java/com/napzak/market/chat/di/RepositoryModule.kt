package com.napzak.market.chat.di

import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.repository.ChatSocketRepository
import com.napzak.market.chat.repositoryimpl.ChatRepositoryImpl
import com.napzak.market.chat.repositoryimpl.ChatRoomRepositoryImpl
import com.napzak.market.chat.repositoryimpl.ChatSocketRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindChatRoomRepository(
        chatRoomRepositoryImpl: ChatRoomRepositoryImpl,
    ): ChatRoomRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl,
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindChatSocketRepository(
        chatSocketRepositoryImpl: ChatSocketRepositoryImpl,
    ): ChatSocketRepository
}
