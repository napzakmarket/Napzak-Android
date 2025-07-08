package com.napzak.market.chat.chatlist

import androidx.lifecycle.ViewModel
import com.napzak.market.chat.chatlist.model.ChatRoomDetail
import com.napzak.market.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor() : ViewModel() {
    private val _chatRoomsState = MutableStateFlow<UiState<List<ChatRoomDetail>>>(UiState.Loading)
    val chatRoomsState = _chatRoomsState.asStateFlow()

    fun fetchChatRooms() {
        _chatRoomsState.update { UiState.Success(ChatRoomDetail.mockList) }
    }
}
