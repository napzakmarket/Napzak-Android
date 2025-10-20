package com.napzak.market.chat.repositoryimpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.napzak.market.chat.datasource.ChatRoomDataSource
import com.napzak.market.chat.datasource.ChatRoomMessageMediator
import com.napzak.market.chat.dto.ChatRoomCreateRequest
import com.napzak.market.chat.dto.ChatRoomPatchProductRequest
import com.napzak.market.chat.mapper.toDomain
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.local.room.NapzakDatabase
import com.napzak.market.local.room.dao.ChatMessageDao
import com.napzak.market.local.room.dao.ChatProductDao
import com.napzak.market.local.room.dao.ChatRemoteKeyDao
import com.napzak.market.local.room.entity.ChatMessageEntity
import com.napzak.market.local.room.type.ChatMessageType
import com.napzak.market.util.android.suspendRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRoomRepositoryImpl @Inject constructor(
    private val chatRoomDataSource: ChatRoomDataSource,
    private val napzakDatabase: NapzakDatabase,
    private val chatMessageDao: ChatMessageDao,
    private val chatProductDao: ChatProductDao,
    private val chatRemoteKeyDao: ChatRemoteKeyDao,
) : ChatRoomRepository {
    override suspend fun getChatRoomInformation(
        productId: Long,
        roomId: Long?,
    ): Result<ChatRoomInformation> {
        return suspendRunCatching {
            chatRoomDataSource.getChatRoomInformation(productId, roomId).data.toDomain()
        }
    }

    override suspend fun createChatRoom(
        productId: Long,
        receiverId: Long,
    ): Result<Long> {
        return suspendRunCatching {
            val request = ChatRoomCreateRequest(productId, receiverId)
            val response = chatRoomDataSource.createChatRoom(request)
            response.data.roomId
        }
    }

    override suspend fun getChatRoomMessages(
        roomId: Long,
    ): Result<List<ReceiveMessage<*>>> {
        return suspendRunCatching {
            val response = chatRoomDataSource.getChatRoomMessages(roomId)
            response.data.messages.map { it.toDomain(roomId) }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedChatRoomMessages(roomId: Long): Flow<PagingData<ReceiveMessage<*>>> {
        return Pager(
            config = PagingConfig(pageSize = ChatRoomMessageMediator.PAGE_SIZE),
            remoteMediator = ChatRoomMessageMediator(
                roomId = roomId,
                chatRoomDataSource = chatRoomDataSource,
                database = napzakDatabase,
                chatMessageDao = chatMessageDao,
                chatProductDao = chatProductDao,
                remoteKeyDao = chatRemoteKeyDao,
            ),
            pagingSourceFactory = { chatMessageDao.getChatMessages(roomId) }
        ).flow.map { pagingData -> pagingData.mapToDomain(roomId) }
    }

    private fun PagingData<ChatMessageEntity>.mapToDomain(
        roomId: Long,
    ): PagingData<ReceiveMessage<*>> = map { chatMessageEntity ->
        if (chatMessageEntity.messageType == ChatMessageType.PRODUCT) {
            val product = chatMessageEntity.message?.toLongOrNull()?.let { productId ->
                chatProductDao.getProduct(productId)?.toDomain()
            }
            chatMessageEntity.toDomain(roomId, product)
        } else {
            chatMessageEntity.toDomain(roomId)
        }
    }

    override suspend fun enterChatRoom(
        roomId: Long,
    ): Result<Pair<Long, Boolean>> {
        return suspendRunCatching {
            val response = chatRoomDataSource.enterChatRoom(roomId)
            response.data.productId to response.data.onlineStoreIds.isNotEmpty()
        }
    }

    override suspend fun leaveChatRoom(
        roomId: Long,
    ): Result<Unit> {
        return suspendRunCatching {
            chatRoomDataSource.leaveChatRoom(roomId)
        }
    }

    override suspend fun withdrawChatRoom(
        roomId: Long,
    ): Result<Unit> {
        return suspendRunCatching {
            chatRoomDataSource.withdrawChatRoom(roomId)
        }
    }

    override suspend fun patchChatRoomProduct(
        roomId: Long,
        productId: Long,
    ): Result<Long> {
        return suspendRunCatching {
            val request = ChatRoomPatchProductRequest(productId)
            val response = chatRoomDataSource.patchChatRoomProduct(roomId, request)
            response.data.updatedProductId
        }
    }
}
