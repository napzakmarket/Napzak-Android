package com.napzak.market.chat.chatroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ChatRoomViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRoomRepository,
) : ViewModel() {
    private val _chatItems = MutableStateFlow<List<ReceiveMessage<*>>>(emptyList())
    val chatItems: StateFlow<List<ReceiveMessage<*>>> = _chatItems.asStateFlow()

    private val _chatRoom = MutableStateFlow<UiState<ChatRoomInformation>>(UiState.Loading)
    val chatRoom: StateFlow<UiState<ChatRoomInformation>> = _chatRoom.asStateFlow()

    var chat by mutableStateOf("")

    fun prepareChatRoom() = viewModelScope.launch {
        val productId = savedStateHandle.get<Long>(PRODUCT_ID_KEY)
        if (productId == null) {
            _chatRoom.update { UiState.Failure("productId가 없습니다.") }
            return@launch
        }
        fetchChatRoomDetail(productId)
    }

    private suspend fun fetchChatRoomDetail(productId: Long) {
        chatRepository.getChatRoomInformation(productId)
            .onSuccess { response ->
                _chatRoom.update { UiState.Success(response) }
            }
    }

    private suspend fun fetchChatItems(roomId: Long) {
        chatRepository.getChatRoomMessages(roomId)
            .onSuccess { messages ->
                _chatItems.update { messages }
            }
    }

    private suspend fun createNewRoom(productId: Long, receiverId: Long) {
        chatRepository.createChatRoom(productId, receiverId)
            .onSuccess { roomId ->
                Timber.d("roomId: $roomId")
            }
    }

    fun sendChat(chat: String) = viewModelScope.launch {
        Timber.d("try to send chat: $chat")
        this@ChatRoomViewModel.chat = ""
    }

    fun exitChatRoom(chatRoomId: Long) = viewModelScope.launch {
        Timber.d("try to exit chatroom: $chatRoomId")
    }

    companion object {
        private const val ROOM_ID_KEY = "chatRoomId"
        private const val PRODUCT_ID_KEY = "productId"
    }
}
