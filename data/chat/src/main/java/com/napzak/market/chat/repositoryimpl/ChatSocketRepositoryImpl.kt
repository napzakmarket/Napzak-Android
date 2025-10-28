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
import com.napzak.market.local.room.type.ChatMessageType
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
import java.time.LocalDate
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

    override fun collectNewChatRequestFromSocket(coroutineScope: CoroutineScope) {
        newChatRequestCollectJob = coroutineScope.launch {
            chatSocketDataSource.getNewChatRequestFlow().collect { roomId ->
                subscribeChatRoom(roomId)
            }
        }
    }

    override fun collectMessagesFromSocket(
        storeId: Long,
        coroutineScope: CoroutineScope
    ) {
        messageCollectJob = coroutineScope.launch {
            chatSocketDataSource.getMessageFlow().collect { message ->
                runCatching {
                    if (message.type in arrayOf(MESSAGE_TYPE_JOIN, MESSAGE_TYPE_LEAVE)) {
                        updateOpponentOnlineStatus(storeId, message)
                    } else {
                        val messageEntity = getMessageEntityMarkedAsRead(message, storeId)
                        val productEntity = getProductEntity(message)
                        withContext(ioDispatcher) {
                            napzakDatabase.withTransaction {
                                insertMessageIntoDB(messageEntity, productEntity)
                                setChatRoomLatestMessage(messageEntity)
                                updateChatRoomStatus(messageEntity)
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun updateOpponentOnlineStatus(
        storeId: Long,
        message: ChatRealtimeMessage,
    ) = withContext(ioDispatcher) {
        if (message.senderId != storeId) {
            val roomId = message.roomId
            val isOpponentOnline = message.type == MESSAGE_TYPE_JOIN
            chatRoomDao.updateOpponentOnlineStatus(roomId, isOpponentOnline)
        }
    }

    @Throws(NullPointerException::class)
    private fun getMessageEntityMarkedAsRead(
        message: ChatRealtimeMessage,
        storeId: Long,
    ): ChatMessageEntity {
        val entity = message.messageId?.let { messageId ->
            message.toEntity(storeId, messageId)
        } ?: throw NullPointerException()
        val chatRoom = chatRoomDao.getChatRoom(entity.roomId)
        return entity.copy(isRead = chatRoom?.isOpponentOnline == true || !entity.isMessageOwner)
    }

    private fun getProductEntity(
        message: ChatRealtimeMessage,
    ): ChatProductEntity? =
        if (message.metadata is ChatMessageMetadata.Product) message.metadata.toProductEntity()
        else null

    private suspend fun insertMessageIntoDB(
        messageEntity: ChatMessageEntity,
        productEntity: ChatProductEntity?,
    ) {
        chatMessageDao.insertChatMessages(listOf(messageEntity))
        productEntity?.let { chatProductDao.updateProductPartially(it) }
    }

    private suspend fun updateChatRoomStatus(messageEntity: ChatMessageEntity) {
        val roomId = messageEntity.roomId
        when (messageEntity.messageType) {
            ChatMessageType.EXIT -> chatRoomDao.updateWithdrawnStatus(roomId, true)
            ChatMessageType.WITHDRAWN -> chatRoomDao.updateWithdrawnStatus(roomId, true)
            ChatMessageType.REPORTED -> chatRoomDao.updateStoreReportedStatus(roomId, true)
            else -> return
        }
    }

    private suspend fun setChatRoomLatestMessage(messageEntity: ChatMessageEntity) {
        val lastMessage = when (messageEntity.messageType) {
            ChatMessageType.IMAGE -> "사진"
            ChatMessageType.TEXT, ChatMessageType.WITHDRAWN -> messageEntity.message ?: ""
            else -> return
        }
        val roomId = messageEntity.roomId
        chatRoomDao.getChatRoom(roomId)?.unreadCount?.let { unreadCount ->
            chatRoomDao.updateLastMessage(
                roomId = messageEntity.roomId,
                lastMessage = lastMessage,
                lastMessageAt = messageEntity.createdAt,
                unreadCount = unreadCount + 1,
                lastUpdated = LocalDate.now().toString()
            )
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