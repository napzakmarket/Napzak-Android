package com.napzak.market.chat.repositoryimpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.napzak.market.chat.datasource.ChatRoomDataSource
import com.napzak.market.chat.datasource.ChatRoomMessageMediator
import com.napzak.market.chat.dto.ChatRoomCreateRequest
import com.napzak.market.chat.dto.ChatRoomPatchProductRequest
import com.napzak.market.chat.mapper.chatroom.toDomain
import com.napzak.market.chat.mapper.chatroom.toProductEntity
import com.napzak.market.chat.mapper.chatroom.toRoomEntity
import com.napzak.market.chat.mapper.toDomain
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.common.dispatcher.IoDispatcher
import com.napzak.market.local.room.NapzakDatabase
import com.napzak.market.local.room.dao.ChatMessageDao
import com.napzak.market.local.room.dao.ChatProductDao
import com.napzak.market.local.room.dao.ChatRemoteKeyDao
import com.napzak.market.local.room.dao.ChatRoomDao
import com.napzak.market.local.room.relation.ChatMessageWithProduct
import com.napzak.market.util.android.suspendRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRoomRepositoryImpl @Inject constructor(
    private val chatRoomDataSource: ChatRoomDataSource,
    private val napzakDatabase: NapzakDatabase,
    private val chatMessageDao: ChatMessageDao,
    private val chatProductDao: ChatProductDao,
    private val chatRoomDao: ChatRoomDao,
    private val chatRemoteKeyDao: ChatRemoteKeyDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ChatRoomRepository {
    override suspend fun getChatRoomInformation(
        productId: Long,
        roomId: Long?,
    ): Result<ChatRoomInformation> {
        return suspendRunCatching {
            val response = chatRoomDataSource.getChatRoomInformation(productId, roomId).data
            if (response.roomId != null) {
                napzakDatabase.withTransaction {
                    val entity = listOf(response.toRoomEntity(response.roomId))
                    val product = response.toProductEntity()
                    runOnChatRoomDao { safeUpsertChatRooms(entity, true) }
                    runOnChatProductDao { upsertProduct(product) }
                }
            }
            response.toDomain()
        }
    }

    override fun getChatRoomInformationAsFlow(roomId: Long): Flow<ChatRoomInformation> {
        return chatRoomDao.getChatRoomFlow(roomId).mapNotNull { it?.toDomain() }
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
            pagingSourceFactory = { chatMessageDao.getChatMessagesWithProducts(roomId) }
        ).flow.map { pagingData -> pagingData.mapToDomain(roomId) }
    }

    private fun PagingData<ChatMessageWithProduct>.mapToDomain(
        roomId: Long,
    ): PagingData<ReceiveMessage<*>> = map { joinedResult ->
        val product = joinedResult.product?.toDomain()
        joinedResult.message.toDomain(roomId, product)
    }

    override suspend fun enterChatRoom(
        roomId: Long,
    ): Result<Long> {
        return suspendRunCatching {
            val (product, isOpponentOnline) = with(chatRoomDataSource.enterChatRoom(roomId).data) {
                productId to onlineStoreIds.isNotEmpty()
            }
            runOnChatRoomDao {
                updateMyOnlineStatus(roomId, true)
                updateOpponentOnlineStatus(roomId, isOpponentOnline)
            }
            product
        }
    }

    override suspend fun leaveChatRoom(
        roomId: Long,
    ): Result<Unit> {
        return suspendRunCatching {
            runOnChatRoomDao { updateMyOnlineStatus(roomId, false) }
            chatRoomDataSource.leaveChatRoom(roomId)
        }
    }

    override suspend fun withdrawChatRoom(
        roomId: Long,
    ): Result<Unit> {
        return suspendRunCatching {
            chatRoomDataSource.withdrawChatRoom(roomId)
            runOnChatRoomDao { deleteChatRoom(roomId) }
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

    override suspend fun markMessagesAsRead(
        roomId: Long,
        isMessageOwner: Boolean
    ): Result<Unit> {
        return suspendRunCatching {
            runOnChatMessageDao {
                markMessagesAsRead(roomId, isMessageOwner)
            }
        }
    }

    private suspend fun runOnChatRoomDao(block: suspend ChatRoomDao.() -> Unit) {
        withContext(ioDispatcher) { chatRoomDao.block() }
    }

    private suspend fun runOnChatProductDao(block: suspend ChatProductDao.() -> Unit) {
        withContext(ioDispatcher) { chatProductDao.block() }
    }

    private suspend fun runOnChatMessageDao(block: suspend ChatMessageDao.() -> Unit) {
        withContext(ioDispatcher) { chatMessageDao.block() }
    }
}
