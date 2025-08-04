package com.napzak.market.chat.controller

import com.napzak.market.chat.model.ChatSocketException
import com.napzak.market.chat.model.ChatSocketException.ConnectionFailureException
import com.napzak.market.chat.model.ChatSocketException.SendFailureException
import com.napzak.market.chat.model.ChatSocketException.SubscriptionFailureException
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatSocketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatController @Inject constructor(
    private val chatSocketRepository: ChatSocketRepository,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val roomIdSet = mutableSetOf<Long>()
    private val jobMap = mutableMapOf<Long, Job>()

    private val _messageFlow = MutableSharedFlow<ReceiveMessage<*>>()
    val messageFlow = _messageFlow.asSharedFlow()

    private val _errorFlow = MutableSharedFlow<ChatSocketException>()
    val errorFlow = _errorFlow.asSharedFlow()

    suspend fun connect(storeId: Long): Result<Unit> {
        return chatSocketRepository.connect()
            .onSuccess { createChatRoom(storeId) }
            .onFailure { _errorFlow.emit(ConnectionFailureException(it)) }
    }

    suspend fun disconnect(): Result<Unit> {
        jobMap.forEach { (_, job) -> job.cancel() }
        jobMap.clear()
        roomIdSet.clear()
        return chatSocketRepository.disconnect()
    }

    suspend fun subscribeChatRoom(roomId: Long, storeId: Long): Result<Unit> {
        return if (roomIdSet.add(roomId)) {
            chatSocketRepository.subscribeChatRoom(roomId, storeId).mapCatching { flow ->
                val job = coroutineScope.launch {
                    flow
                        .catch { _errorFlow.emit(SubscriptionFailureException(it)) }
                        .collect { message ->
                            _messageFlow.emit(message)
                        }
                }
                jobMap[roomId] = job
            }
        } else {
            Result.success(Unit)
        }
    }

    fun unsubscribeChatRoom(roomId: Long) {
        jobMap[roomId]?.cancel()
        jobMap.remove(roomId)
        roomIdSet.remove(roomId)
    }

    private suspend fun createChatRoom(storeId: Long): Result<Unit> {
        return chatSocketRepository.subscribeCreateChatRoom(storeId).mapCatching { flow ->
            val job = coroutineScope.launch {
                flow
                    .catch { _errorFlow.emit(SubscriptionFailureException(it)) }
                    .collect { roomId ->
                        subscribeChatRoom(roomId, storeId)
                    }
            }
            jobMap[CREATE_JOB_ID] = job
        }
    }

    suspend fun sendMessage(message: SendMessage<*>) {
        try {
            chatSocketRepository.sendChat(message)
        } catch (e: Exception) {
            _errorFlow.emit(SendFailureException(e))
        }
    }

    companion object {
        private const val CREATE_JOB_ID = 0L
    }
}
