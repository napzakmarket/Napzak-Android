package com.napzak.market.chat.repositoryimpl

import com.napzak.market.chat.datasource.ChatDataSource
import com.napzak.market.chat.mapper.chatroom.toDomain
import com.napzak.market.chat.mapper.chatroom.toEntities
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.common.dispatcher.IoDispatcher
import com.napzak.market.local.room.dao.ChatRoomDao
import com.napzak.market.local.room.entity.ChatRoomEntity
import com.napzak.market.util.android.suspendRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatRoomDao: ChatRoomDao,
    private val chatDataSource: ChatDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ChatRepository {
    override suspend fun getChatRoomIds(): Result<List<Long>> {
        return suspendRunCatching {
            chatDataSource.getChatRoomIds().data.chatRoomIds
        }
    }

    override suspend fun fetchChatRoomsFromRemote(): Result<Unit> {
        return suspendRunCatching {
            val pushToken = null // TODO: FCM 구현하면 fcmToken 값으로 대체
            val chatRooms = chatDataSource.getChatRooms(pushToken).data.toEntities()
            insertChatRoomIntoDB(chatRooms)
        }
    }

    private suspend fun insertChatRoomIntoDB(chatRooms: List<ChatRoomEntity>) {
        withContext(ioDispatcher) {
            chatRoomDao.safeUpsertChatRooms(chatRooms, false)
        }
    }

    override fun getChatRoomsFlow(): Flow<List<ChatRoom>> {
        return chatRoomDao.getChatRoomsFlow().mapNotNull { entities ->
            entities.map { it.toDomain() }
        }
    }
}
