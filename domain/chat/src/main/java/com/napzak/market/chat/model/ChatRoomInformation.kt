package com.napzak.market.chat.model

data class ChatRoomInformation(
    val roomId: Long?,
    val productBrief: ProductBrief,
    val storeBrief: StoreBrief,
    val isOpponentOnline: Boolean = false,
) {
    val isChatBlocked
        get() = with(storeBrief) {
            isOpponentStoreBlocked || isMyStoreBlocked || isReported
        }

    companion object {
        fun mock() = ChatRoomInformation(
            roomId = 1,
            productBrief = ProductBrief.mock(),
            storeBrief = StoreBrief.mock(),
        )
    }
}
