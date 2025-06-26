package com.napzak.market.chat.chatroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.chatroom.model.ChatItem
import com.napzak.market.chat.chatroom.model.ChatRoom
import com.napzak.market.chat.chatroom.preview.mockChats
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
internal class ChatRoomViewModel @Inject constructor() : ViewModel() {
    private val _chatItems = MutableStateFlow<List<ChatItem<*>>>(emptyList())
    val chatItems: StateFlow<List<ChatItem<*>>> = _chatItems.asStateFlow()

    private val _chatRoom = MutableStateFlow<UiState<ChatRoom>>(UiState.Loading)
    val chatRoom: StateFlow<UiState<ChatRoom>> = _chatRoom.asStateFlow()

    var chat by mutableStateOf("")

    suspend fun fetchChatItems() {
        // TODO: 채팅 내역을 가져오는 로직 구현
        _chatItems.update { mockChats }
    }

    suspend fun fetchChatRoomDetail() {
        _chatRoom.update {
            UiState.Success(ChatRoom.mock)
        }
    }

    fun sendChat(chat: String) = viewModelScope.launch {
        Timber.d("try to send chat: $chat")
        this@ChatRoomViewModel.chat = ""
    }

    fun exitChatRoom(chatRoomId: Long) = viewModelScope.launch {
        Timber.d("try to exit chatroom: $chatRoomId")
    }
}
