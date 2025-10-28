package com.napzak.market.chat.repositoryimpl

import androidx.room.withTransaction
import com.napzak.market.chat.datasource.ChatSocketDataSource
import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.ChatRealtimeMessage
import com.napzak.market.chat.mapper.chatmessage.toEntity
import com.napzak.market.chat.mapper.chatmessage.toProductEntity
import com.napzak.market.chat.mapper.toRequest
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatSocketRepository
import com.napzak.market.common.dispatcher.IoDispatcher
import com.napzak.market.local.room.NapzakDatabase
import com.napzak.market.local.room.dao.ChatMessageDao
import com.napzak.market.local.room.dao.ChatProductDao
import com.napzak.market.local.room.dao.ChatRoomDao
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.entity.ChatProductEntity
import com.napzak.market.remote.socket.type.SocketConnectionState
import com.napzak.market.util.android.suspendRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class ChatSocketRepositoryImpl @Inject constructor(
    private val chatSocketDataSource: ChatSocketDataSource,
    private val napzakDatabase: NapzakDatabase,
    private val chatMessageDao: ChatMessageDao,
    private val chatProductDao: ChatProductDao,
    private val chatRoomDao: ChatRoomDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ChatSocketRepository {
    private var messageCollectJob: Job? = null
    private var newChatRequestCollectJob: Job? = null

    override fun collectMessagesFromSocket(
        storeId: Long,
        coroutineScope: CoroutineScope
    ) {
        messageCollectJob = coroutineScope.launch {
            chatSocketDataSource.getMessageFlow().collect { message ->
                if (message.type in arrayOf(MESSAGE_TYPE_JOIN, MESSAGE_TYPE_LEAVE)) {
                    updateOpponentOnlineStatus(storeId, message)
                } else {
                    val messageEntity = message.toEntity(storeId)
                    val productEntity = when (message.metadata) {
                        is ChatMessageMetadata.Product -> message.metadata.toProductEntity()
                        else -> null
                    }
                    insertMessageIntoDB(messageEntity, productEntity)
                }
            }
        }
    }

    override fun collectNewChatRequestFromSocket(coroutineScope: CoroutineScope) {
        newChatRequestCollectJob = coroutineScope.launch {
            chatSocketDataSource.getNewChatRequestFlow().collect { roomId ->
                subscribeChatRoom(roomId)
            }
        }
    }

    override suspend fun connect(): Result<Unit> = suspendRunCatching {
        chatSocketDataSource.connect()
    }

    override suspend fun disconnect(): Result<Unit> = suspendRunCatching {
        chatSocketDataSource.disconnect()
        messageCollectJob?.cancel()
        newChatRequestCollectJob?.cancel()
    }

    override suspend fun subscribeChatRoom(roomId: Long): Result<Unit> = suspendRunCatching {
        if (awaitConnected()) {
            chatSocketDataSource.subscribeChatRoom(roomId)
        }
    }

    override suspend fun subscribeCreateChatRoom(storeId: Long) = runCatching {
        if (awaitConnected()) {
            chatSocketDataSource.subscribeCreateChatRoom(storeId)
        }
    }

    override suspend fun sendChat(chat: SendMessage<*>): Result<Unit> = suspendRunCatching {
        if (awaitConnected()) {
            chatSocketDataSource.sendMessage(chat.toRequest())
        }
    }

    private suspend fun insertMessageIntoDB(
        messageEntity: ChatMessageEntity,
        productEntity: ChatProductEntity?,
    ) = withContext(ioDispatcher) {
        napzakDatabase.withTransaction {
            chatMessageDao.insertChatMessages(listOf(messageEntity))
            productEntity?.let { chatProductDao.updateProductPartially(it) }

        }
    }

    private suspend fun updateOpponentOnlineStatus(
        storeId: Long,
        message: ChatRealtimeMessage,
    ) = withContext(ioDispatcher) {
        if (message.senderId != storeId) {
            val roomId = message.roomId ?: return@withContext
            val isOpponentOnline = message.type == MESSAGE_TYPE_JOIN
            chatRoomDao.updateOpponentOnlineStatus(roomId, isOpponentOnline)
        }
    }

    private suspend fun awaitConnected(timeoutMs: Long = 7_000): Boolean {
        return withTimeoutOrNull(timeoutMs) {
            chatSocketDataSource.socketConnectionState
                .distinctUntilChanged()
                .filter { it == SocketConnectionState.CONNECTED }
                .first()
            true
        } ?: false
    }

    companion object Companion {
        private const val MESSAGE_TYPE_JOIN = "JOIN"
        private const val MESSAGE_TYPE_LEAVE = "LEAVE"
    }
}