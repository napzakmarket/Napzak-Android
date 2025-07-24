package com.napzak.market.chat.chatroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ChatRoomViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRoomRepository,
) : ViewModel() {
    private val _chatItems = MutableStateFlow<List<ReceiveMessage<*>>>(emptyList())
    val chatItems: StateFlow<List<ReceiveMessage<*>>> = _chatItems.asStateFlow()

    private val _chatRoom = MutableStateFlow<UiState<ChatRoomUiState>>(UiState.Loading)
    val chatRoom: StateFlow<UiState<ChatRoomUiState>> = _chatRoom.asStateFlow()

    private val _chatRoomInformation = mutableStateOf<ChatRoomInformation?>(null)
    private val isProductChanged = mutableStateOf(false)

    var chat by mutableStateOf("")

    fun prepareChatRoom() = viewModelScope.launch {
        try {
            val productId = savedStateHandle.get<Long>(PRODUCT_ID_KEY)
                ?: throw NullPointerException("productId가 없습니다.")

            fetchChatRoomDetail(productId)
            val chatRoomInfo = _chatRoomInformation.value
                ?: throw NullPointerException("채팅방 정보가 없습니다.")

            // 채팅방이 없는 경우
            if (chatRoomInfo.roomId == null) {
                _chatRoom.value = UiState.Success(
                    ChatRoomUiState(
                        storeBrief = chatRoomInfo.storeBrief,
                    )
                )
                isProductChanged.value = true
                return@launch
            }

            // 채팅방이 있는 경우
            chatRoomInfo.roomId?.let { roomId ->
                val currentChatRoomProductId = getChatRoomProductId(roomId)
                fetchMessages(roomId)
                _chatRoom.update {
                    UiState.Success(
                        ChatRoomUiState(
                            roomId = chatRoomInfo.roomId,
                            storeBrief = chatRoomInfo.storeBrief,
                            productBrief = chatRoomInfo.productBrief,
                        )
                    )
                }
                // 거래 중인 상품의 정보가 기존과 다른 경우
                if (currentChatRoomProductId != productId) {
                    isProductChanged.value = true
                }
            }
        } catch (e: Exception) {
            _chatRoom.update { UiState.Failure(e.message.toString()) }
            Timber.e(e)
        }
    }

    fun sendChat(chat: String) = viewModelScope.launch {
        Timber.d("try to send chat: $chat")
        this@ChatRoomViewModel.chat = ""
    }

    private suspend fun getChatRoomProductId(roomId: Long): Long =
        chatRepository.enterChatRoom(roomId).getOrThrow()

    private suspend fun fetchChatRoomDetail(productId: Long) {
        chatRepository.getChatRoomInformation(productId)
            .onSuccess { response -> _chatRoomInformation.value = response }
            .getOrThrow()
    }

    private suspend fun fetchMessages(roomId: Long) {
        chatRepository.getChatRoomMessages(roomId)
            .onSuccess { messages ->
                _chatItems.update { messages }
            }.getOrThrow()
    }

    private suspend fun createNewRoom(productId: Long, receiverId: Long) {
        chatRepository.createChatRoom(productId, receiverId)
            .onSuccess { roomId ->
                Timber.d("roomId: $roomId")
            }
    }

    fun leaveChatRoom() = viewModelScope.launch {
        try {
            val roomId = (_chatRoom.value as UiState.Success).data.roomId ?: return@launch
            chatRepository.leaveChatRoom(roomId).getOrThrow()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    companion object {
        private const val ROOM_ID_KEY = "chatRoomId"
        private const val PRODUCT_ID_KEY = "productId"
    }
}
