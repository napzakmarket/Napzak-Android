package com.napzak.market.chat.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.dto.MessageItem
import com.napzak.market.chat.mapper.toEntity
import com.napzak.market.chat.mapper.toProductEntity
import com.napzak.market.local.room.NapzakDatabase
import com.napzak.market.local.room.dao.ChatMessageDao
import com.napzak.market.local.room.dao.ChatProductDao
import com.napzak.market.local.room.dao.ChatRemoteKeyDao
import com.napzak.market.local.room.entity.ChatRemoteKeyEntity
import com.napzak.market.local.room.relation.ChatMessageWithProduct
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ChatRoomMessageMediator @Inject constructor(
    private val roomId: Long,
    private val chatRoomDataSource: ChatRoomDataSource,
    private val database: NapzakDatabase,
    private val chatMessageDao: ChatMessageDao,
    private val chatProductDao: ChatProductDao,
    private val remoteKeyDao: ChatRemoteKeyDao,
) : RemoteMediator<Int, ChatMessageWithProduct>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChatMessageWithProduct>
    ): MediatorResult {
        val remoteKey = getRemoteKey()
        val cursor = when (loadType) {
            LoadType.REFRESH -> return loadNewMessagesAndBridge(remoteKey)
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                remoteKey?.appendCursor
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        try {
            val (messages, nextCursor) = fetchMessagesAndCursorFromRemote(cursor)

            database.withTransaction {
                upsertChatMessages(messages)
                updateChatRemoteKey(remoteKey, nextCursor)
            }

            val endOfPaginationReached = messages.isEmpty() || nextCursor == null
            return MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            Timber.e("ChatRoomMessages Load failed: ${e.message}")
            return MediatorResult.Error(e)
        }
    }

    private suspend fun loadNewMessagesAndBridge(remoteKey: ChatRemoteKeyEntity?): MediatorResult {
        return try {
            val fetchedMessages = mutableListOf<MessageItem>()
            val anchorMessageId = remoteKey?.refreshAnchorMessageId
            var cursor: String? = null
            var bridge = false
            var currentLoadCount = 0
            val maxAttempt = if (anchorMessageId != null) BRIDGE_MAX_COUNT else 1

            // 앵커로 설정된 메세지 ID가 나타날 때까지 페이지 요청을 반복한다.
            while (currentLoadCount < maxAttempt && !bridge) {
                val (messages, nextCursor) = fetchMessagesAndCursorFromRemote(cursor)

                bridge = messages.any { message -> message.messageId == anchorMessageId }
                fetchedMessages.addAll(messages)
                cursor = nextCursor
                currentLoadCount++

                if (cursor == null) break
            }

            database.withTransaction {
                if (!bridge && currentLoadCount >= BRIDGE_MAX_COUNT) {
                    chatMessageDao.deleteChatMessages(roomId)
                    remoteKeyDao.deleteRemoteKey(roomId)
                } else {
                    upsertChatMessages(fetchedMessages)
                    updateChatRemoteKey(remoteKey, cursor)
                }
            }

            val endOfPaginationReached = cursor == null
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            Timber.e("ChatRoomMessages Refresh Failed : $e")
            MediatorResult.Error(e)
        }
    }

    private suspend fun upsertChatMessages(messages: List<MessageItem>) {
        val entities = messages.map { it.toEntity(roomId) }
        chatMessageDao.insertChatMessages(entities)
        messages
            .filter { it.metadata is ChatMessageMetadata.Product }
            .forEach { message ->
                message.toProductEntity()?.let { product ->
                    chatProductDao.upsertProduct(product)
                }
            }
    }

    private suspend fun fetchMessagesAndCursorFromRemote(cursor: String?): Pair<List<MessageItem>, String?> {
        val response = chatRoomDataSource.getChatRoomMessages(roomId, cursor, PAGE_SIZE).data
        val messages = response.messages
        val cursor = response.cursor
        return messages to cursor
    }

    private suspend fun updateChatRemoteKey(remoteKey: ChatRemoteKeyEntity?, cursor: String?) {
        val newAnchorMessageId = chatMessageDao.getLatestMessage(roomId)?.messageId
        val newKey =
            remoteKey?.copy(refreshAnchorMessageId = newAnchorMessageId, appendCursor = cursor)
                ?: ChatRemoteKeyEntity(roomId, newAnchorMessageId, cursor)
        remoteKeyDao.upsertKey(newKey)
    }

    private suspend fun getRemoteKey(): ChatRemoteKeyEntity? {
        return remoteKeyDao.getRemoteKey(roomId)
    }

    companion object {
        private const val BRIDGE_MAX_COUNT = 5
        const val PAGE_SIZE = 30
    }
}