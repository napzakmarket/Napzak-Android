package com.napzak.market.chat.repositoryimpl

import com.napzak.market.chat.datasource.ChatSocketDataSource
import com.napzak.market.chat.dto.ChatRealtimeMessage
import com.napzak.market.chat.mapper.toDomain
import com.napzak.market.chat.mapper.toRequest
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatSocketRepository
import com.napzak.market.remote.socket.type.SocketConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatSocketRepositoryImpl @Inject constructor(
    private val chatSocketDataSource: ChatSocketDataSource,
) : ChatSocketRepository {
    override fun getMessageFlow(storeId: Long): Flow<ReceiveMessage<*>> {
        return chatSocketDataSource.messageFlow.mapNotNull {
            runCatching {
                val response = json.decodeFromString(ChatRealtimeMessage.serializer(), it)
                response.toDomain(storeId)
            }.getOrNull()
        }
    }

    override fun getChatRoomCreationFlow(): Flow<Long> {
        return chatSocketDataSource.messageFlow.mapNotNull {
            runCatching {
                json.decodeFromString(Long.serializer(), it)
            }.getOrNull()
        }
    }

    override fun getConnectState(): Flow<Boolean> {
        return chatSocketDataSource.socketConnectionState.map {
            it == SocketConnectionState.CONNECTED
        }
    }

    override suspend fun connect() = runCatching {
        chatSocketDataSource.connect()
    }

    override suspend fun disconnect() = runCatching {
        chatSocketDataSource.disconnect()
    }

    override suspend fun subscribeChatRoom(roomId: Long, storeId: Long) = runCatching {
        chatSocketDataSource.subscribeChatRoom(roomId)
    }

    override suspend fun subscribeCreateChatRoom(storeId: Long) = runCatching {
        chatSocketDataSource.subscribeCreateChatRoom(storeId)
    }

    override suspend fun sendChat(chat: SendMessage<*>) = runCatching {
        chatSocketDataSource.sendMessage(chat.toRequest())
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}
