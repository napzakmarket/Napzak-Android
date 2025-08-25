package com.napzak.market.chat.chatlist

import androidx.compose.runtime.Immutable
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.common.state.UiState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ChatListUiState(
    val loadState: UiState<ImmutableList<ChatRoom>>
) {
    companion object {
        val Empty = ChatListUiState(UiState.Loading)
    }
}
