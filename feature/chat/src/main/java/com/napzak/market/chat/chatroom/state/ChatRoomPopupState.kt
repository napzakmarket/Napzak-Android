package com.napzak.market.chat.chatroom.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ChatRoomPopupState(
    val isBottomSheetVisible: Boolean = false,
    val isWithdrawDialogVisible: Boolean = false,
    val isBlockDialogVisible: Boolean = false,
    val isPreviewVisible: Boolean = false,
) {
    fun handleEvent(event: ChatRoomPopupEvent): ChatRoomPopupState = when (event) {
        is ChatRoomPopupEvent.ShowBottomSheet -> copy(isBottomSheetVisible = true)
        is ChatRoomPopupEvent.DismissBottomSheet -> copy(isBottomSheetVisible = false)
        is ChatRoomPopupEvent.ShowWithdrawDialog -> copy(isWithdrawDialogVisible = true)
        is ChatRoomPopupEvent.DismissWithdrawDialog -> copy(isWithdrawDialogVisible = false)
        is ChatRoomPopupEvent.ShowBlockDialog -> copy(isBlockDialogVisible = true)
        is ChatRoomPopupEvent.DismissBlockDialog -> copy(isBlockDialogVisible = false)
        is ChatRoomPopupEvent.DismissOnBlockConfirmed -> copy(
            isBlockDialogVisible = false,
            isBottomSheetVisible = false,
        )

        is ChatRoomPopupEvent.ShowPreview -> copy(isPreviewVisible = true)
        is ChatRoomPopupEvent.DismissPreview -> copy(isPreviewVisible = false)
    }
}

internal sealed class ChatRoomPopupEvent() {
    data object ShowBottomSheet : ChatRoomPopupEvent()
    data object DismissBottomSheet : ChatRoomPopupEvent()
    data object ShowWithdrawDialog : ChatRoomPopupEvent()
    data object DismissWithdrawDialog : ChatRoomPopupEvent()
    data object ShowBlockDialog : ChatRoomPopupEvent()
    data object DismissBlockDialog : ChatRoomPopupEvent()
    data object DismissOnBlockConfirmed : ChatRoomPopupEvent()
    data object ShowPreview : ChatRoomPopupEvent()
    data object DismissPreview : ChatRoomPopupEvent()
}