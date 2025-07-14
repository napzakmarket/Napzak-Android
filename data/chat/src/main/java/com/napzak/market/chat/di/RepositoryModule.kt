package com.napzak.market.chat.di

import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.repositoryimpl.ChatRoomRepositoryImpl
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
}