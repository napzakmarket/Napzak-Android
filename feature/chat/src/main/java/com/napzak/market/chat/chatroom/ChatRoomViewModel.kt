package com.napzak.market.chat.chatroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.chatroom.type.ChatCondition
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.repository.ChatSocketRepository
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
    private val chatSocketRepository: ChatSocketRepository,
) : ViewModel() {
    private val _chatItems = MutableStateFlow<List<ReceiveMessage<*>>>(emptyList())
    val chatItems: StateFlow<List<ReceiveMessage<*>>> = _chatItems.asStateFlow()

    private val _chatRoomState = MutableStateFlow<UiState<ChatRoomUiState>>(UiState.Loading)
    val chatRoomState: StateFlow<UiState<ChatRoomUiState>> = _chatRoomState.asStateFlow()

    private val chatCondition = mutableStateOf(ChatCondition.PRODUCT_NOT_CHANGED)

    var chat by mutableStateOf("")

    // TODO: FCM 관리 방식에 따라 유지/삭제 결정
    /*private val roomId
        get() = requireNotNull(savedStateHandle.get<Long>(ROOM_ID_KEY))*/

    private val productId
        get() = requireNotNull(savedStateHandle.get<Long>(PRODUCT_ID_KEY))

    private val chatRoomStateAsSuccess
        get() = (_chatRoomState.value as UiState.Success).data

    /**
     * 채팅방 진입했을 때, 상대방과 사용자의 관계에 알맞게 채팅방을 구성합니다.
     *
     * 각 상황마다 채팅 조건 [chatCondition]을 수정합니다. 이 값은 채팅방 입장 후 처음 메시지를 보낼 때 조건을 따지기 위해 사용됩니다.
     *
     * - 채팅을 처음하는 경우, 채팅 조건만 수정합니다.
     * - 채팅 기록이 있다면, 채팅방의 상태를 수정하고 채팅 기록을 불러옵니다.
     *    - 물품도 변경되었다면, 채팅 조건도 수정합니다.
     */
    fun prepareChatRoom() = viewModelScope.launch {
        try {
            fetchChatRoomDetail(productId)

            // 채팅방이 없는 경우
            if (chatRoomStateAsSuccess.roomId == null) {
                chatCondition.value = ChatCondition.NEW_CHAT_ROOM
                return@launch
            }

            // 채팅방이 있는 경우
            chatRoomStateAsSuccess.roomId?.let { roomId ->
                val currentChatRoomProductId = enterChatRoom(roomId)
                fetchMessages(roomId)

                if (currentChatRoomProductId != productId) {
                    chatCondition.value = ChatCondition.PRODUCT_CHANGED
                }
            }
        } catch (e: Exception) {
            _chatRoomState.update { UiState.Failure(e.message.toString()) }
            Timber.e(e)
        }
    }

    /**
     * 채팅방의 정보를 불러옵니다.
     * 불러온 정보로 [_chatRoomState]의 상태를 초기화합니다.
     */
    private suspend fun fetchChatRoomDetail(productId: Long) {
        chatRepository.getChatRoomInformation(productId)
            .onSuccess { response ->
                _chatRoomState.update {
                    UiState.Success(
                        ChatRoomUiState(
                            roomId = response.roomId,
                            storeBrief = response.storeBrief,
                            productBrief = response.productBrief,
                        )
                    )
                }
            }.getOrThrow()
    }

    /**
     * TODO: 페이징 처리 + 채팅 기록 저장 방식 수정 (Collection 교체 혹은 저장 방향 수정)
     *
     * 과거 채팅 기록들을 불러옵니다.
     */
    private suspend fun fetchMessages(roomId: Long) {
        chatRepository.getChatRoomMessages(roomId)
            .onSuccess { messages ->
                _chatItems.update {
                    it.toMutableList().apply { addAll(messages) }
                }
            }
            .getOrThrow()
    }

    /**
     * 구독 중인 소켓 채널로부터 메시지를 수신합니다. 이 메서드가 호출되어야 수신이 시작됩니다.
     */
    private fun collectMessages(roomId: Long) = viewModelScope.launch {
        try {
            val storeId = requireNotNull(chatRoomStateAsSuccess.storeBrief?.storeId)

            chatSocketRepository.getMessageFlow(storeId).collect { message ->
                if (message.roomId == roomId) {
                    Timber.tag("ChatRoom").d("구독 중인 메시지: $message")

                    // TODO: 채팅 기록 저장 방식 수정 (Collection 교체 혹은 저장 방향 수정)
                    val newList = _chatItems.value.toMutableList()
                        .apply { this.add(0, message) }
                        .toList()
                    _chatItems.update { newList }
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
    }

    /**
     * 채팅 소켓 채널을 통해 텍스트를 전송합니다.
     *
     * 텍스트를 전송할 떄마다 첫 채팅인지 혹은 물품 정보가 변경되었는지 확인합니다.
     */
    fun sendTextMessage(text: String) {
        viewModelScope.launch {
            try {
                onProductChanged()
                val chatRoomState = (_chatRoomState.value as UiState.Success).data
                val roomId = chatRoomState.roomId ?: return@launch
                chatSocketRepository.sendChat(SendMessage.Text(roomId, text))
                chat = ""
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
            }
        }
    }

    /**
     * 채팅 소켓 채널을 통해 이미지 URL을 전송합니다.
     * 이미지를 전송할 떄마다 첫 채팅인지 혹은 물품 정보가 변경되었는지 확인합니다.
     */
    fun sendImageMessage(imageUrls: List<String>) {
        viewModelScope.launch {
            try {
                onProductChanged()
                /*TODO: PresignedURL 로직 삽입*/

                val chatRoomState = (_chatRoomState.value as UiState.Success).data
                val roomId = chatRoomState.roomId ?: return@launch
                chatSocketRepository.sendChat(SendMessage.Image(roomId, null, imageUrls))

            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
            }
        }
    }

    /**
     * 채팅 소켓 채널을 통해 새로운 물품 정보를 전송합니다.
     */
    fun sendProductMessage() {
        viewModelScope.launch {
            try {
                val productBrief = requireNotNull(chatRoomStateAsSuccess.productBrief)
                val roomId = requireNotNull(chatRoomStateAsSuccess.roomId)

                chatSocketRepository.sendChat(
                    SendMessage.Product(roomId, null, productBrief)
                )
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
            }
        }
    }

    /**
     * 대화 대상인 물품이 변경되었을 때, 채팅방 상태에 따라 올바른 로직을 매핑합니다.
     */
    private suspend fun onProductChanged() {
        when (chatCondition.value) {
            ChatCondition.NEW_CHAT_ROOM -> createNewRoom(productId)

            ChatCondition.PRODUCT_CHANGED -> {
                chatRoomStateAsSuccess.roomId?.let { patchProduct(it, productId) }
            }

            ChatCondition.PRODUCT_NOT_CHANGED -> {} //no work to do
        }
        chatCondition.value = ChatCondition.PRODUCT_NOT_CHANGED
    }

    /**
     * 상대방과 처음 채팅을 시도할 때, 채팅방의 상태를 초기화하기 위해 일련의 과정을 수행합니다..
     *
     * 각 과정에서 에러가 발생하면 예외를 던집니다.
     * 1. 채팅방을 생성합니다. 이 과정에서 채팅방의 ID를 반환받습니다.
     * 2. 생성된 채팅방에 입장합니다.
     * 3. 채팅방 상태의 `roomId`를 업데이트합니다.
     * 3. 채팅방에 대한 메시지 채널을 구독합니다.
     * 4. 채팅방 채널을 통해 제품 정보를 전송합니다.
     */
    private suspend fun createNewRoom(productId: Long) {
        val storeId = requireNotNull(chatRoomStateAsSuccess.storeBrief?.storeId)
        val roomId = chatRepository.createChatRoom(productId, storeId).getOrThrow()
        enterChatRoom(roomId)
        _chatRoomState.update { currentState ->
            UiState.Success((currentState as UiState.Success).data.copy(roomId = roomId))
        }
        with(chatSocketRepository) {
            subscribeChatRoom(roomId)
            sendProductMessage()
        }
    }

    /**
     * 기존에 대화 중이던 방에서 물품이 변경되었을 때 새로운 정보를 등록하기 위해 일련의 과정을 수행합니다.
     *
     * 실패하면 에외를 던집니다.
     * 1. 채팅방 상단 고정 상품을 수정합니다.
     * 2. 채팅방 채널을 통해 제품 정보를 전송합니다.
     */
    private suspend fun patchProduct(roomId: Long, productId: Long) {
        chatRepository.patchChatRoomProduct(roomId, productId)
            .onSuccess { sendProductMessage() }
            .getOrThrow()
    }

    /**
     * 채팅방에 입장하여 서버에게 채팅방에 접속 중임을 알립니다.
     * @return 채팅방 ID
     */
    private suspend fun enterChatRoom(roomId: Long): Long =
        chatRepository.enterChatRoom(roomId).onSuccess {
            collectMessages(roomId)
        }.getOrThrow()

    /**
     * 현재 채팅방에 접속 중이지 않음을 서버에 알리기 위해 호춣합니다.
     */
    fun leaveChatRoom() {
        viewModelScope.launch {
            try {
                val roomId = chatRoomStateAsSuccess.roomId
                    ?: throw NullPointerException("채팅방 ID가 없습니다.")
                chatRepository.leaveChatRoom(roomId).getOrThrow()
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
            }
        }
    }

    companion object {
        private const val TAG = "ChatRoom"
        private const val ROOM_ID_KEY = "chatRoomId"
        private const val PRODUCT_ID_KEY = "productId"
    }
}
