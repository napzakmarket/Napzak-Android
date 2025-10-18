package com.napzak.market.chat.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.mapper.toEntity
import com.napzak.market.chat.mapper.toProductEntity
import com.napzak.market.local.room.NapzakDatabase
import com.napzak.market.local.room.dao.ChatMessageDao
import com.napzak.market.local.room.dao.ChatProductDao
import com.napzak.market.local.room.dao.ChatRemoteKeyDao
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.entity.ChatRemoteKeyEntity
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
) : RemoteMediator<Int, ChatMessageEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChatMessageEntity>
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
            val response = chatRoomDataSource.getChatRoomMessages(roomId, cursor, PAGE_SIZE).data
            val messages = response.messages
            val cursor = response.cursor
            val entities = messages.map { it.toEntity(roomId) }

            database.withTransaction {
                chatMessageDao.insertChatMessages(entities)
                messages
                    .filter { it.metadata == ChatMessageMetadata.Product }
                    .forEach { message ->
                        message.toProductEntity()?.let { product ->
                            chatProductDao.upsertProduct(product)
                        }
                    }
                updateChatRemoteKey(remoteKey, cursor)
            }

            val endOfPaginationReached = entities.isEmpty() || cursor == null
            return MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            Timber.e("ChatRoomMessages Load failed: ${e.message}")
            return MediatorResult.Error(e)
        }
    }

    private suspend fun loadNewMessagesAndBridge(remoteKey: ChatRemoteKeyEntity?): MediatorResult {
        return try {
            val fetchMessages = mutableListOf<ChatMessageEntity>()
            val anchorMessageId = remoteKey?.refreshAnchorMessageId
            var cursor: String? = null
            var bridge = false
            var currentLoadCount = 0
            val maxAttempt = if (anchorMessageId != null) BRIDGE_MAX_COUNT else 1

            while (currentLoadCount < maxAttempt && !bridge) {
                val (entities, nextCursor) = getMessagesAndCursor(cursor)
                bridge = entities.any { entity -> entity.messageId == anchorMessageId }

                fetchMessages.addAll(entities)
                cursor = nextCursor
                currentLoadCount++

                if (cursor == null) break
            }

            database.withTransaction {
                if (!bridge && currentLoadCount >= BRIDGE_MAX_COUNT) {
                    chatMessageDao.deleteChatMessages(roomId)
                    remoteKeyDao.deleteRemoteKey(roomId)
                } else {
                    chatMessageDao.insertChatMessages(fetchMessages)
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

    private suspend fun updateChatRemoteKey(remoteKey: ChatRemoteKeyEntity?, cursor: String?) {
        val newAnchorMessageId = chatMessageDao.getLatestMessage(roomId)?.messageId
        val newKey =
            remoteKey?.copy(refreshAnchorMessageId = newAnchorMessageId, appendCursor = cursor)
                ?: ChatRemoteKeyEntity(roomId, newAnchorMessageId, cursor)
        remoteKeyDao.upsertKey(newKey)
    }

    private suspend fun getMessagesAndCursor(cursor: String?): Pair<List<ChatMessageEntity>, String?> {
        val response = chatRoomDataSource.getChatRoomMessages(roomId, cursor, PAGE_SIZE).data
        val chatMessageEntities = response.messages.map { it.toEntity(roomId) }
        val cursor = response.cursor
        return chatMessageEntities to cursor
    }

    private suspend fun getRemoteKey(): ChatRemoteKeyEntity? {
        return remoteKeyDao.getRemoteKey(roomId)
    }

    companion object {
        private const val BRIDGE_MAX_COUNT = 5
        const val PAGE_SIZE = 30
    }
}