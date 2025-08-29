package com.napzak.market.chat.controller

import com.napzak.market.chat.model.ChatSocketException
import com.napzak.market.chat.model.ChatSocketException.ConnectionFailureException
import com.napzak.market.chat.model.ChatSocketException.SendFailureException
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatSocketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatController @Inject constructor(
    private val chatSocketRepository: ChatSocketRepository,
) {
    private val roomIdSet = mutableSetOf<Long>()
    private val jobMap = mutableMapOf<Int, Job>()
    private val flowMap = mutableMapOf<Int, Flow<Any>>()

    private val _messageFlow = MutableSharedFlow<ReceiveMessage<*>>()
    val messageFlow = _messageFlow.asSharedFlow()

    private val _errorFlow = MutableSharedFlow<ChatSocketException>()
    val errorFlow = _errorFlow.asSharedFlow()

    suspend fun connect(storeId: Long): Result<Unit> {
        collectMessage(storeId)
        collectNewChatRoom(storeId)

        return chatSocketRepository.connect()
            .onSuccess { subscribeCreateChatRoom(storeId) }
            .onFailure { _errorFlow.emit(ConnectionFailureException(it)) }
    }

    suspend fun disconnect(): Result<Unit> {
        jobMap.forEach { (_, job) -> job.cancel() }
        jobMap.clear()
        flowMap.clear()
        roomIdSet.clear()
        return chatSocketRepository.disconnect()
    }

    suspend fun sendMessage(message: SendMessage<*>) {
        try {
            if (awaitConnected()) {
                chatSocketRepository.sendChat(message)
            }
        } catch (e: Exception) {
            _errorFlow.emit(SendFailureException(e))
        }
    }

    suspend fun subscribeChatRoom(roomId: Long, storeId: Long): Result<Unit> {
        return if (awaitConnected() && roomIdSet.add(roomId)) {
            chatSocketRepository.subscribeChatRoom(roomId, storeId)
        } else {
            Result.success(Unit)
        }
    }

    fun unsubscribeChatRoom(roomId: Long) {
        roomIdSet.remove(roomId)
    }

    private fun collectMessage(storeId: Long) {
        if (jobMap.containsKey(ID_COLLECT_MSG) && flowMap.containsKey(ID_COLLECT_MSG)) return

        val flow = chatSocketRepository.getMessageFlow(storeId)
        val job = CoroutineScope(Dispatchers.Default).launch {
            flow.collect { message ->
                _messageFlow.emit(message)
            }
        }

        flowMap[ID_COLLECT_MSG] = flow
        jobMap[ID_COLLECT_MSG] = job
    }

    private fun collectNewChatRoom(storeId: Long) {
        if (jobMap.containsKey(ID_CREATE_ROOM) && flowMap.containsKey(ID_CREATE_ROOM)) return

        val flow = chatSocketRepository.getChatRoomCreationFlow()

        val job = CoroutineScope(Dispatchers.Default).launch {
            flow.collect { roomId ->
                chatSocketRepository.subscribeChatRoom(roomId, storeId).getOrThrow()
            }
        }

        flowMap[ID_CREATE_ROOM] = flow
        jobMap[ID_CREATE_ROOM] = job
    }

    private suspend fun subscribeCreateChatRoom(storeId: Long) {
        if (awaitConnected()) {
            chatSocketRepository.subscribeCreateChatRoom(storeId)
        }
    }

    private suspend fun awaitConnected(timeoutMs: Long = 7_000): Boolean {
        return withTimeoutOrNull(timeoutMs) {
            chatSocketRepository.getConnectState()
                .distinctUntilChanged()
                .filter { it }
                .first()
            true
        } ?: false
    }

    companion object {
        private const val ID_COLLECT_MSG = 0
        private const val ID_CREATE_ROOM = 1
    }
}
