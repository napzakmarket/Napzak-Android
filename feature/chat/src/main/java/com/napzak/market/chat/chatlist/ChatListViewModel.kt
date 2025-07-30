package com.napzak.market.chat.chatlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.chat.repository.ChatSocketRepository
import com.napzak.market.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    private val _chatRoomsState = MutableStateFlow<UiState<List<ChatRoom>>>(UiState.Loading)
    val chatRoomsState = _chatRoomsState.asStateFlow()

    private var _chatRoomPair by mutableStateOf(mapOf<Long, ChatRoom>() to listOf<ChatRoom>())

    private var _messageFlow: Flow<ReceiveMessage<*>>? = null

    fun fetchChatRooms() = viewModelScope.launch {
        chatRepository.getChatRooms()
            .onSuccess { chatRooms ->
                if (chatRooms.isEmpty()) {
                    _chatRoomsState.update { UiState.Empty }
                    return@launch
                }

                val map = mutableMapOf<Long, ChatRoom>()
                val list = mutableListOf<ChatRoom>()

                chatRooms.forEach { chatRoom ->
                    chatSocketRepository.subscribeChatRoom(chatRoom.roomId).onSuccess {
                        map[chatRoom.roomId] = chatRoom
                        list.add(chatRoom)
                    }
                }

                _chatRoomPair = map to list
                _chatRoomsState.update { UiState.Success(list) }
            }
            .onFailure { error ->
                _chatRoomsState.update { UiState.Failure(error.message.toString()) }
            }
    }

    fun collectChatMessages() {
        viewModelScope.launch {
            try {
                if (_messageFlow == null) {
                    _messageFlow = chatSocketRepository.messageFlow
                    _messageFlow?.collect { message -> updateChatRoom(message) }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun updateChatRoom(message: ReceiveMessage<*>) {
        try {
            val roomId = message.roomId
                ?: throw IllegalStateException("메시지에 roomId가 없습니다")

            val chatRoomMap = _chatRoomPair.first
            val chatRoomList = _chatRoomPair.second

            if (chatRoomMap[roomId] == null) {
                fetchChatRooms()
                return
            }

            chatRoomMap[roomId]?.let { chatRoom ->
                val list = chatRoomList.toMutableList()
                val map = chatRoomMap.toMutableMap()
                val room = chatRoom.copy(
                    lastMessage = getLastMessage(message),
                    unreadMessageCount = chatRoom.unreadMessageCount + 1,
                )

                list.removeIf { it.roomId == roomId }
                list.add(0, room)
                map[roomId] = room

                _chatRoomPair = map to list
                _chatRoomsState.value = UiState.Success(list)
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
    }

    private fun getLastMessage(message: ReceiveMessage<*>): String {
        return when (message) {
            is ReceiveMessage.Image -> "사진"
            is ReceiveMessage.Text -> message.text
            is ReceiveMessage.Notice -> message.notice
            else -> ""
        }
    }

    companion object {
        private const val TAG = "ChatList"
    }
}
