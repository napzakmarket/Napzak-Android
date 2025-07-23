package com.napzak.market.chat.repositoryimpl

import com.napzak.market.chat.datasource.ChatSocketDataSource
import com.napzak.market.chat.mapper.toDomain
import com.napzak.market.chat.mapper.toRequest
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatSocketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatSocketRepositoryImpl @Inject constructor(
    private val chatSocketDataSource: ChatSocketDataSource,
) : ChatSocketRepository {

    override val messageFlow: Flow<ReceiveMessage<*>> = chatSocketDataSource.messageFlow.map {
        it.toDomain()
    }
    override val errorFlow: Flow<Exception> = chatSocketDataSource.errorFlow

    override suspend fun connect() = runCatching {
        chatSocketDataSource.connect()
    }

    override suspend fun disconnect() = runCatching {
        chatSocketDataSource.disconnect()
    }

    override suspend fun subscribeChatRoom(roomId: Long) = runCatching {
        chatSocketDataSource.subscribeChatRoom(roomId)
    }

    override suspend fun sendChat(chat: SendMessage<*>) = runCatching {
        chatSocketDataSource.sendMessage(chat.toRequest())
    }
}