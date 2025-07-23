package com.napzak.market.chat.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.chat.repository.ChatSocketRepository
import com.napzak.market.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val chatSocketRepository: ChatSocketRepository,
) : ViewModel() {
    private val _chatRoomsState = MutableStateFlow<UiState<Map<Long, ChatRoom>>>(UiState.Loading)
    val chatRoomsState = _chatRoomsState.asStateFlow()

    fun fetchChatRooms() = viewModelScope.launch {
        chatRepository.getChatRooms()
            .onSuccess { chatRooms ->
                _chatRoomsState.update {
                    UiState.Success(
                        buildMap {
                            chatRooms.forEach { chatRoom -> put(chatRoom.roomId, chatRoom) }
                        }
                    )
                }
            }
            .onFailure { error ->
                _chatRoomsState.update { UiState.Failure(error.message.toString()) }
            }
    }

    fun collectChatMessages() {
        viewModelScope.launch {
            try {
                chatSocketRepository.messageFlow.collect { message -> updateChatRoom(message) }
            } catch (e: IllegalStateException) {
                Timber.e(e)
            }
        }
    }

    private fun updateChatRoom(message: ReceiveMessage<*>) {
        _chatRoomsState.update { currentState ->
            try {
                if (currentState is UiState.Success) {
                    val chatRooms = currentState.data.toMutableMap()
                    val roomId = message.roomId ?: throw Exception()
                    val targetRoom = currentState.data[roomId] ?: throw Exception()
                    chatRooms[roomId] = targetRoom.copy(
                        lastMessage = message.message.toString(),
                        lastMessageAt = message.timeStamp,
                        unreadMessageCount = targetRoom.unreadMessageCount + 1
                    )
                    UiState.Success(chatRooms)
                } else {
                    currentState
                }
            } catch (e: Exception) {
                Timber.e(e)
                currentState
            }
        }
    }
}
