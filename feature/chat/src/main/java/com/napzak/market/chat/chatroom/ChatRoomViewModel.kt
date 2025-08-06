package com.napzak.market.chat.chatroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.chatroom.type.ChatCondition
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.usecase.GetChatFlowUseCase
import com.napzak.market.chat.usecase.SendMessageUseCase
import com.napzak.market.chat.usecase.SubscribeChatRoomUseCase
import com.napzak.market.common.state.UiState
import com.napzak.market.presigned_url.model.UploadImage
import com.napzak.market.presigned_url.usecase.UploadImagesUseCase
import com.napzak.market.store.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ChatRoomViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRoomRepository,
    private val getChatFlowUseCase: GetChatFlowUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val subscribeChatRoomUseCase: SubscribeChatRoomUseCase,

    private val storeRepository: StoreRepository,
    private val uploadImagesUseCase: UploadImagesUseCase
) : ViewModel() {
    private val chatMessageIdSet = mutableSetOf<Long>()
    private val chatMessageList = mutableListOf<ReceiveMessage<*>>()

    private val _chatItems = MutableStateFlow<List<ReceiveMessage<*>>>(emptyList())
    val chatItems: StateFlow<List<ReceiveMessage<*>>> = _chatItems.asStateFlow()

    private val _chatRoomState = MutableStateFlow(ChatRoomUiState())
    val chatRoomState = _chatRoomState.asStateFlow()

    private val _sideEffect = Channel<ChatRoomSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val chatCondition = mutableStateOf(ChatCondition.PRODUCT_NOT_CHANGED)

    var chat by mutableStateOf("")

    private val _roomId
        get() = requireNotNull(savedStateHandle.get<Long>(ROOM_ID_KEY))

    private val _productId
        get() = requireNotNull(savedStateHandle.get<Long>(PRODUCT_ID_KEY))

    private val _chatRoomStateAsSuccess
        get() = (_chatRoomState.value.chatRoomState as UiState.Success).data

    /**
     * [SavedStateHandle]에 저장된 값들을 활용하여 채팅방 정보들을 준비합니다.
     */
    fun prepareChatRoom() = viewModelScope.launch {
        fetchStoreId()
        runCatching { prepareChatRoomWithRoomId(_roomId) }
            .recoverCatching { prepareChatRoomWithProductId(_productId) }
            .onFailure { e ->
                _chatRoomState.update {
                    it.copy(chatRoomState = UiState.Failure(e.message.toString()))
                }
                Timber.e(e)
            }
    }

    /**
     * [StoreRepository]를 통해 내 상점 ID를 불러옵니다.
     *
     * 불러온 상점 ID는 메시지의 방향을 설정하기 위해 사용합니다.
     */
    private fun fetchStoreId() = viewModelScope.launch {
        _chatRoomState.update {
            it.copy(storeId = storeRepository.fetchStoreInfo().getOrNull()?.storeId)
        }
    }

    /**
     * [_roomId] 깂을 활용하여 상대방과 사용자의 관계에 알맞게 채팅방을 구성합니다.
     */
    private suspend fun prepareChatRoomWithRoomId(roomId: Long) {
        val productId = enterChatRoom(roomId)
        fetchMessages(roomId)
        fetchChatRoomDetail(productId, roomId)
        collectMessages(roomId)
    }

    /**
     * [_productId] 깂을 활용하여 상대방과 사용자의 관계에 알맞게 채팅방을 구성합니다.
     *
     * 각 상황마다 채팅 조건 [chatCondition]을 수정합니다. 이 값은 채팅방 입장 후 처음 메시지를 보낼 때 조건을 따지기 위해 사용됩니다.
     *
     * - 채팅을 처음하는 경우, 채팅 조건만 수정합니다.
     * - 채팅 기록이 있다면, 채팅방의 상태를 수정하고 채팅 기록을 불러옵니다.
     * - 물품도 변경되었다면, 채팅 조건도 수정합니다.
     */
    private suspend fun prepareChatRoomWithProductId(productId: Long) {
        fetchChatRoomDetail(productId)

        // 채팅방이 없는 경우
        if (_chatRoomStateAsSuccess.roomId == null) {
            chatCondition.value = ChatCondition.NEW_CHAT_ROOM
            return
        }
        // 채팅방이 있는 경우
        _chatRoomStateAsSuccess.roomId?.let { roomId ->
            val currentChatRoomProductId = enterChatRoom(roomId)
            fetchMessages(roomId)
            collectMessages(roomId)

            if (currentChatRoomProductId != productId) {
                chatCondition.value = ChatCondition.PRODUCT_CHANGED
            }
        }

    }

    /**
     * 채팅방의 정보를 불러옵니다.
     * 불러온 정보로 [_chatRoomState]의 상태를 초기화합니다.
     */
    private suspend fun fetchChatRoomDetail(productId: Long, roomId: Long? = null) {
        chatRepository.getChatRoomInformation(productId, roomId)
            .onSuccess { response ->
                _chatRoomState.update {
                    it.copy(
                        chatRoomState = UiState.Success(
                            ChatRoomState(
                                roomId = response.roomId,
                                storeBrief = response.storeBrief,
                                productBrief = response.productBrief,
                            )
                        )
                    )
                }
            }.getOrElse {
                Timber.tag(TAG).e(it)
                throw it
            }
    }

    /**
     * TODO: 페이징 처리
     *
     * 과거 채팅 기록들을 불러옵니다.
     */
    private suspend fun fetchMessages(roomId: Long) {
        chatRepository.getChatRoomMessages(roomId)
            .onSuccess { messages ->
                val reversedMessage = messages.asReversed()
                if (reversedMessage.any { it is ReceiveMessage.Notice }) {
                    _chatRoomState.update { it.copy(isRoomWithdrawn = true) }
                }
                addMessages(reversedMessage)
            }
            .getOrElse {
                Timber.tag(TAG).e(it)
                throw it
            }
    }

    /**
     * 구독 중인 소켓 채널로부터 메시지를 수신합니다. 이 메서드가 호출되어야 수신이 시작됩니다.
     */
    private fun collectMessages(roomId: Long) = viewModelScope.launch {
        try {
            getChatFlowUseCase(roomId).collect { message ->
                if (message.roomId == roomId) {
                    _sideEffect.send(ChatRoomSideEffect.OnReceiveChatMessage)
                    addMessages(listOf(message))
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
    }

    /**
     * 수신한 메시지를 화면에 출력할 메시지 리스트에 넣습니다. 다음과 같은 전처리 과정을 거칩니다.
     *
     * - 넣고자하는 메시지의 id가 이미 존재한다면 메시지 리스트에 추가하지 않습니다.
     * - 메시지가 이미지인 경우, 이미지 url의 쿼리를 제거합니다.
     * - 메시지 리스트가 변경되었다면 메시지 플로우를 갱신합니다.
     */
    private fun addMessages(newMessages: List<ReceiveMessage<*>>) {
        var added = false
        newMessages.forEach { message ->
            when (message) {
                is ReceiveMessage.Join -> {
                    chatMessageList.forEachIndexed { index, record ->
                        if (with(record) { isMessage && isMessageOwner == true && isRead }) {
                            return@forEachIndexed
                        }
                        chatMessageList[index] =
                            if (with(record) { isMessage && isMessageOwner == true && !isRead }) {
                                runCatching {
                                    (record as ReceiveMessage.Text).copy(isRead = true)
                                }.recoverCatching {
                                    (record as ReceiveMessage.Image).copy(isRead = true)
                                }.recoverCatching {
                                    (record as ReceiveMessage.Product).copy(isRead = true)
                                }.getOrNull() ?: record
                            } else record

                    }

                    _chatRoomState.update { it.copy(isOpponentOnline = true) }
                    _chatItems.update { chatMessageList.toList() }
                }

                is ReceiveMessage.Leave -> {
                    _chatRoomState.update { it.copy(isOpponentOnline = false) }
                }

                else -> {
                    if (message is ReceiveMessage.Notice) {
                        _chatRoomState.update { it.copy(isRoomWithdrawn = true) }
                    }

                    if (chatMessageIdSet.add(message.messageId)) {
                        chatMessageList.add(preprocessMessage(message))
                        added = true
                    }
                }
            }
        }

        if (added) {
            chatMessageList.sortBy { it.messageId }
            _chatItems.update { chatMessageList.toList() }
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
                val roomId = _chatRoomStateAsSuccess.roomId ?: return@launch
                sendMessage(SendMessage.Text(roomId, text))
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
    fun sendImageMessage(uri: String) {
        viewModelScope.launch {
            try {
                onProductChanged()
                /*TODO: PresignedURL 로직 삽입*/
                uploadImagesUseCase(
                    listOf(
                        UploadImage(
                            imageType = UploadImage.ImageType.CHAT,
                            uri = uri,
                        )
                    )
                ).onSuccess { response ->
                    val imageUrls = response.map {
                        it.value.toUri().toString().substringBefore("?")
                    }
                    val roomId = requireNotNull(_chatRoomStateAsSuccess.roomId)
                    sendMessage(SendMessage.Image(roomId, null, imageUrls))
                }.getOrThrow()
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
                val productBrief = requireNotNull(_chatRoomStateAsSuccess.productBrief)
                val roomId = requireNotNull(_chatRoomStateAsSuccess.roomId)

                sendMessage(SendMessage.Product(roomId, null, productBrief))
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
            }
        }
    }

    /**
     * 채팅 소켓 채널을 통해 메시지를 전송합니다.
     * 성공적으로 전송하면 [ChatRoomSideEffect.OnSendChatMessage]를 발생시킵니다.
     */
    private suspend fun sendMessage(message: SendMessage<*>) {
        sendMessageUseCase(message)
        _sideEffect.trySend(ChatRoomSideEffect.OnSendChatMessage)
    }

    /**
     * 대화 대상인 물품이 변경되었을 때, 채팅방 상태에 따라 올바른 로직을 매핑합니다.
     */
    private suspend fun onProductChanged() {
        when (chatCondition.value) {
            ChatCondition.NEW_CHAT_ROOM -> createNewRoom(_productId)

            ChatCondition.PRODUCT_CHANGED -> {
                _chatRoomStateAsSuccess.roomId?.let { patchProduct(it, _productId) }
            }

            ChatCondition.PRODUCT_NOT_CHANGED -> {} //no work to do
        }
        chatCondition.value = ChatCondition.PRODUCT_NOT_CHANGED
    }

    /**
     * 인식할 수 없는 이미지 링크에 대해서 전처리를 수행합니다.
     * 1. 쿼리 파라미터를 없앱니다.
     * 2. 메시지 읽음 처리를 수행합니다. 다음 상태일 때 메시지를 읽음 처리합니다:
     *    - 불러온 메시지의 기록의 isRead가 true
     *    - 수신한 메시지의 송신자가 '본인'일 경우
     *    - 상대방이 채팅방에 접속해있는 경우
     */
    private fun preprocessMessage(message: ReceiveMessage<*>): ReceiveMessage<*> {
        val isRead =
            message.isRead || message.isMessageOwner == false || _chatRoomState.value.isOpponentOnline

        return when (message) {
            is ReceiveMessage.Text -> message.copy(isRead = isRead)
            is ReceiveMessage.Product -> message.copy(isRead = isRead)
            is ReceiveMessage.Image -> {
                message.copy(
                    imageUrl = message.imageUrl.toUri().toString().substringBefore("?"),
                    isRead = isRead
                )
            }

            else -> message
        }
    }

    /**
     * 상대방과 처음 채팅을 시도할 때, 채팅방의 상태를 초기화하기 위해 일련의 과정을 수행합니다..
     *
     * 각 과정에서 에러가 발생하면 예외를 던집니다.
     * 1. 채팅방을 생성합니다. 이 과정에서 채팅방의 ID를 반환받습니다.
     * 2. 생성된 채팅방에 입장합니다.
     * 3. 채팅방 상태를 업데이트합니다.
     * 3. 채팅방에 대한 메시지 채널을 구독합니다.
     * 4. 채팅방 채널을 통해 제품 정보를 전송합니다.
     */
    private suspend fun createNewRoom(productId: Long) {
        val storeId = requireNotNull(_chatRoomStateAsSuccess.storeBrief?.storeId)
        val roomId = chatRepository.createChatRoom(productId, storeId).getOrThrow()

        try {
            enterChatRoom(roomId)
            collectMessages(roomId)
            fetchChatRoomDetail(productId, roomId)
            subscribeChatRoomUseCase(roomId, storeId)
            sendProductMessage()
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "채팅방 생성에 실패했습니다.")
            throw e
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
        chatRepository.enterChatRoom(roomId).mapCatching { (productId, isOnline) ->
            Timber.tag(TAG).d("채팅방 입장 및 구독 시작함")
            _chatRoomState.update { it.copy(isOpponentOnline = isOnline) }
            productId
        }.getOrElse {
            Timber.tag(TAG).e(it)
            throw it
        }


    /**
     * 현재 채팅방에 접속 중이지 않음을 서버에 알리기 위해 호춣합니다.
     */
    fun leaveChatRoom() {
        viewModelScope.launch {
            try {
                val roomId = requireNotNull(_chatRoomStateAsSuccess.roomId)
                chatRepository.leaveChatRoom(roomId).getOrThrow()
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
            }
        }
    }

    /**
     * 채팅방에서 나가기 위해 호출합니다.
     */
    fun withdrawChatRoom() {
        viewModelScope.launch {
            try {
                val roomId = requireNotNull(_chatRoomStateAsSuccess.roomId)
                chatRepository.withdrawChatRoom(roomId).onSuccess {
                    _sideEffect.trySend(ChatRoomSideEffect.OnWithdrawChatRoom)
                }
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
