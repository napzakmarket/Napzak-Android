package com.napzak.market.chat.repositoryimpl

import com.napzak.market.chat.datasource.ChatRoomDataSource
import com.napzak.market.chat.mapper.toDomain
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.util.android.suspendRunCatching
import javax.inject.Inject

class ChatRoomRepositoryImpl @Inject constructor(
    private val chatRoomDataSource: ChatRoomDataSource,
) : ChatRoomRepository {
    override suspend fun getChatRoomInformation(roomId: Long) = suspendRunCatching {
        val response = chatRoomDataSource.getChatRoomInformation(roomId)
        with(response.data) {
            val productBrief = productInfo.toDomain()
            val storeBrief = storeInfo.toDomain()
            Pair(roomId, Pair(productBrief, storeBrief))
        }
    }
}