package com.napzak.market.chat.chatroom

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.StoreBrief
import com.napzak.market.common.state.UiState

@Stable
internal data class ChatRoomUiState(
    val chatRoomState: UiState<ChatRoomState> = UiState.Loading,
    val storeId: Long? = null,
    val isRoomWithdrawn: Boolean = false,
    val isOpponentOnline: Boolean = false,
    val isUserExitChatRoom: Boolean = false,
) {
    val isOpponentWithdrawn
        get() = if (chatRoomState is UiState.Success) {
            chatRoomState.data.storeBrief?.isWithdrawn == true
        } else false

    val isOpponentReported
        get() = if (chatRoomState is UiState.Success) {
            chatRoomState.data.storeBrief?.isReported == true
        } else false

    val isChatDisabled get() = isRoomWithdrawn || isOpponentWithdrawn || isOpponentReported
}

@Immutable
internal data class ChatRoomState(
    val roomId: Long? = null,
    val storeBrief: StoreBrief? = null,
    val productBrief: ProductBrief? = null,
)
