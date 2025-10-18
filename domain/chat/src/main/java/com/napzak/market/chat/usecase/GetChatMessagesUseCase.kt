package com.napzak.market.chat.usecase

import androidx.paging.PagingData
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Room에 저장된 채팅 메시지를 페이지 단위 Flow로 불러옵니다.
 */
class GetChatMessagesUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
) {
    operator fun invoke(roomId: Long): Flow<PagingData<ReceiveMessage<*>>> {
        return chatRoomRepository.getPagedChatRoomMessages(roomId)
    }
}