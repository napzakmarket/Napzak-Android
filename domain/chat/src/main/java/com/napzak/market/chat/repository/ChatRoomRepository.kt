package com.napzak.market.chat.repository

import androidx.paging.PagingData
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ReceiveMessage
import kotlinx.coroutines.flow.Flow

interface ChatRoomRepository {
    suspend fun getChatRoomInformation(
        productId: Long,
        roomId: Long? = null
    ): Result<ChatRoomInformation>

    fun getChatRoomInformationAsFlow(
        roomId: Long
    ): Flow<ChatRoomInformation>

    suspend fun createChatRoom(productId: Long, receiverId: Long): Result<Long>
    suspend fun getChatRoomMessages(roomId: Long): Result<List<ReceiveMessage<*>>>
    fun getPagedChatRoomMessages(roomId: Long): Flow<PagingData<ReceiveMessage<*>>>
    suspend fun enterChatRoom(roomId: Long): Result<Pair<Long, Boolean>>
    suspend fun leaveChatRoom(roomId: Long): Result<Unit>
    suspend fun withdrawChatRoom(roomId: Long): Result<Unit>
    suspend fun patchChatRoomProduct(roomId: Long, productId: Long): Result<Long>
}
