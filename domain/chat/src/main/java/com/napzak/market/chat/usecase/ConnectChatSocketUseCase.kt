package com.napzak.market.chat.usecase

import com.napzak.market.chat.controller.ChatController
import javax.inject.Inject

class ConnectChatSocketUseCase @Inject constructor(
    private val chatController: ChatController
) {
    suspend operator fun invoke(storeId: Long): Result<Unit> {
        return chatController.connect(storeId)
    }
}