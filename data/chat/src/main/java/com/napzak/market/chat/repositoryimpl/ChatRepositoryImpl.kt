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
    override suspend fun getChatRooms(): Result<List<ChatRoom>> {
        return suspendRunCatching {
            val deviceToken = null // TODO: FCM 구현하면 tokenProvider.getToken()으로 대체
            chatDataSource.getChatRooms(deviceToken).data.chatRooms.map { it.toDomain() }
        }
    }
}
