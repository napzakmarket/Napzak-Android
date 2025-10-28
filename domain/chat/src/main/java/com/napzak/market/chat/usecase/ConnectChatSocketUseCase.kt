package com.napzak.market.chat.usecase

import com.napzak.market.chat.repository.ChatSocketRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ConnectChatSocketUseCase @Inject constructor(
    private val chatSocketRepository: ChatSocketRepository,
) {
    suspend operator fun invoke(storeId: Long, coroutineScope: CoroutineScope): Result<Unit> {
        return with(chatSocketRepository) {
            connect()
                .onSuccess {
                    collectMessagesFromSocket(storeId, coroutineScope)
                    collectNewChatRequestFromSocket(coroutineScope)
                }
        }
    }
}