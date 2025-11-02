package com.napzak.market.chat.repositoryimpl

import com.napzak.market.chat.datasource.ChatSocketDataSource
import com.napzak.market.chat.mapper.chatmessage.toDomain
import com.napzak.market.chat.mapper.chatmessage.toRequest
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatSocketRepository
import com.napzak.market.remote.socket.type.SocketConnectionState
import com.napzak.market.util.android.suspendRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class ChatSocketRepositoryImpl @Inject constructor(
    private val chatSocketDataSource: ChatSocketDataSource,
) : ChatSocketRepository {
    override suspend fun connect(): Result<Unit> = suspendRunCatching {
        chatSocketDataSource.connect()
    }

    override suspend fun disconnect(): Result<Unit> = suspendRunCatching {
        chatSocketDataSource.disconnect()
    }

    override suspend fun subscribeChatRoom(roomId: Long): Result<Unit> = suspendRunCatching {
        awaitConnected()
        chatSocketDataSource.subscribeChatRoom(roomId)
    }

    override suspend fun subscribeCreateChatRoom(storeId: Long) = runCatching {
        awaitConnected()
        chatSocketDataSource.subscribeCreateChatRoom(storeId)
    }

    override suspend fun sendChat(chat: SendMessage<*>): Result<Unit> = suspendRunCatching {
        awaitConnected()
        chatSocketDataSource.sendMessage(chat.toRequest())
    }

    override fun getChatMessageStream(storeId: Long): Flow<ReceiveMessage<*>> {
        return chatSocketDataSource.getMessageFlow().map { it.toDomain(storeId) }
    }

    override fun getNewChatRequestStream(): Flow<Long> {
        return chatSocketDataSource.getNewChatRequestFlow()
    }

    private suspend fun awaitConnected(timeoutMs: Long = 7_000) {
        val connected = withTimeoutOrNull(timeoutMs) {
            chatSocketDataSource.socketConnectionState
                .distinctUntilChanged()
                .filter { it == SocketConnectionState.CONNECTED }
                .first()
            true
        } ?: false

        if (!connected) throw IllegalStateException("소켓이 연결되어 있지 않습니다.")
    }
}