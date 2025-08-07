package com.napzak.market.chat.usecase

import com.napzak.market.chat.controller.ChatController
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetChatErrorFlowUseCase @Inject constructor(
    private val chatController: ChatController,
) {
    operator fun invoke(): SharedFlow<Throwable> {
        return chatController.errorFlow
    }
}