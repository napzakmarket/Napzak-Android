package com.napzak.market.chat.repositoryimpl

import com.napzak.market.chat.datasource.ChatDataSource
import com.napzak.market.chat.mapper.toDomain
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.util.android.suspendRunCatching
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource,
) : ChatRepository {
    override suspend fun getChatRoomIds(): Result<List<Long>> {
        return suspendRunCatching {
            chatDataSource.getChatRoomIds().data.chatRoomIds
        }
    }

    override suspend fun getChatRooms(): Result<Pair<Long, List<ChatRoom>>> {
        return suspendRunCatching {
            val pushToken = null // TODO: FCM 구현하면 fcmToken 값으로 대체
            val response = chatDataSource.getChatRooms(pushToken).data
            response.myStoreId to response.chatRooms.map { it.toDomain() }
        }
    }
}
