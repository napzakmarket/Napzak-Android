package com.napzak.market.chat.chatlist

import androidx.compose.runtime.Immutable
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.common.state.UiState

@Immutable
data class ChatListUiState(
    val loadState: UiState<List<ChatRoom>>
) {
    companion object {
        val Empty = ChatListUiState(UiState.Loading)
    }
}
