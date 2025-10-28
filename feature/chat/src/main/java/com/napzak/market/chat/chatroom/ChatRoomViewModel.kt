package com.napzak.market.chat.chatroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.SendMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.type.ChatCondition
import com.napzak.market.chat.usecase.SendChatMessageUseCase
import com.napzak.market.common.state.UiState
import com.napzak.market.mixpanel.MixpanelConstants.OPENED_REPORT_MARKET
import com.napzak.market.presigned_url.model.UploadImage
import com.napzak.market.presigned_url.model.UploadImage.ImageType.CHAT
import com.napzak.market.presigned_url.usecase.UploadImagesUseCase
import com.napzak.market.store.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mixpanel: MixpanelAPI?,
    private val chatRoomRepository: ChatRoomRepository,
    private val storeRepository: StoreRepository,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val uploadImagesUseCase: UploadImagesUseCase,
) : ViewModel() {
    private val _roomId = MutableStateFlow<Long?>(savedStateHandle[ROOM_ID_KEY])
    private val _productId = MutableStateFlow<Long?>(savedStateHandle[PRODUCT_ID_KEY])
    private val _storeId = MutableStateFlow<Long?>(null)

    private val _sideEffect = Channel<ChatRoomSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val _chatRoomState = MutableStateFlow<UiState<ChatRoomInformation>>(UiState.Loading)
    val chatRoomState = _chatRoomState.asStateFlow()
    private val _chatRoomStateAsSuccess get() = chatRoomState.value as? UiState.Success<ChatRoomInformation>

    val chatMessageFlow: StateFlow<Flow<PagingData<ReceiveMessage<*>>>> =
        _roomId.filterNotNull().mapNotNull { roomId ->
            chatRoomRepository.getPagedChatRoomMessages(roomId)
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = flowOf(PagingData.empty())
        )

    var chat by mutableStateOf("")
    private var chatCondition by mutableStateOf(ChatCondition.PRODUCT_NOT_CHANGED)

    init {
        fetchChatRoomData()
    }

    fun fetchChatRoomData() = viewModelScope.launch {
        runCatching {
            _roomId.value?.let { fetchChatRoomInformationByRoomId(it) }
                ?: _productId.value?.let { fetchChatRoomInformationByProductId(it) }
                ?: throw Throwable(message = "채팅방 정보를 불러올 수 없습니다.")
        }.onFailure { t ->
            Timber.e(t)
            with(_sideEffect) {
                send(ChatRoomSideEffect.ShowToast(CHAT_ERROR_MSG))
                send(ChatRoomSideEffect.OnErrorOccurred)
            }
        }
    }

    private suspend fun fetchChatRoomInformationByRoomId(roomId: Long) {
        chatRoomRepository.enterChatRoom(roomId)
            .onSuccess { (productId, _) ->
                _productId.update { productId }
                getChatRoomInformation(productId, roomId)
            }.getOrElse { t -> throw Throwable(cause = t, message = "roomId 정보 조회 실패") }
    }

    private suspend fun fetchChatRoomInformationByProductId(productId: Long) {
        val chatRoom = getChatRoomInformation(productId, null)
        val roomId = chatRoom.roomId
        if (roomId == null) {
            chatCondition = ChatCondition.NEW_CHAT_ROOM
            // TODO: 여기에서 전달받은 데이터를 인메모리로 저장해야 하는데...
            _chatRoomState.update { UiState.Success(chatRoom) }

        } else {
            chatRoomRepository.enterChatRoom(roomId)
                .onSuccess { (remoteProductId, _) ->
                    _roomId.update { roomId }
                    // 서버에 저장된 채팅방의 상품과 현재 상품이 다른 경우
                    if (remoteProductId != productId) {
                        chatCondition = ChatCondition.PRODUCT_CHANGED
                    }
                }.getOrElse { t -> throw Throwable(cause = t, message = "productId 정보 조회 실패") }
        }
    }

    private suspend fun getChatRoomInformation(
        productId: Long,
        roomId: Long?
    ): ChatRoomInformation {
        return chatRoomRepository.getChatRoomInformation(productId, roomId)
            .getOrElse { t -> throw Throwable(cause = t, message = "채팅방 정보 조회 실패") }
    }

    fun collectChatRoomInformation() = viewModelScope.launch {
        _roomId.flatMapLatest { roomId ->
            if (roomId == null) flowOf(UiState.Empty)
            else chatRoomRepository.getChatRoomInformationAsFlow(roomId).map { UiState.Success(it) }
        }.collect { state ->
            _chatRoomState.update { state }
        }
    }

    fun leaveChatRoom() = viewModelScope.launch {
        val roomId = _roomId.value
        if (roomId == null) {
            Timber.w("채팅방이 생성되지 않았습니다.")
        } else {
            chatRoomRepository.leaveChatRoom(roomId)
                .onFailure(Timber::e)
        }
    }

    fun withdrawChatRoom() = viewModelScope.launch {
        val roomId = _roomId.value
        if (roomId == null) {
            Timber.w("채팅방이 생성되지 않았습니다.")
        } else {
            chatRoomRepository.withdrawChatRoom(roomId)
                .onFailure(Timber::e)
        }
        _sideEffect.trySend(ChatRoomSideEffect.OnWithdrawChatRoom)
    }

    fun sendTextMessage(text: String) = viewModelScope.launch {
        runCatching {
            when (chatCondition) {
                ChatCondition.NEW_CHAT_ROOM -> createNewRoom()
                ChatCondition.PRODUCT_CHANGED -> patchChatProduct()
                ChatCondition.PRODUCT_NOT_CHANGED -> Unit
            }
            val roomId = requireNotNull(_roomId.value) { "roomId가 없습니다." }
            sendMessage(SendMessage.Text(roomId, text))
        }.onSuccess {
            chatCondition = ChatCondition.PRODUCT_NOT_CHANGED
        }.onFailure {
            Timber.e(it)
            _sideEffect.trySend(ChatRoomSideEffect.ShowToast(CHAT_ERROR_MSG))
        }
    }

    fun sendImageMessage(uri: String) = viewModelScope.launch {
        runCatching {
            when (chatCondition) {
                ChatCondition.NEW_CHAT_ROOM -> createNewRoom()
                ChatCondition.PRODUCT_CHANGED -> patchChatProduct()
                ChatCondition.PRODUCT_NOT_CHANGED -> Unit
            }
            val imageUrls = getUploadImageUrls(uri)
            val roomId = requireNotNull(_roomId.value) { "roomId가 없습니다." }
            sendMessage(SendMessage.Image(roomId, null, imageUrls))
        }.onSuccess {
            chatCondition = ChatCondition.PRODUCT_NOT_CHANGED
            _sideEffect.trySend(ChatRoomSideEffect.OnSendChatMessage)
        }.onFailure {
            Timber.e(it)
            _sideEffect.trySend(ChatRoomSideEffect.ShowToast(CHAT_ERROR_MSG))
        }
    }

    private suspend fun sendMessage(message: SendMessage<*>) {
        sendChatMessageUseCase(message).onSuccess {
            chat = ""
            _sideEffect.trySend(ChatRoomSideEffect.OnSendChatMessage)
        }
    }

    private suspend fun getUploadImageUrls(uri: String): List<String> {
        val response = uploadImagesUseCase(
            listOf(UploadImage(CHAT, uri))
        ).getOrElse { cause ->
            throw Throwable("이미지 변환 실패", cause)
        }
        return response.values.map { uri -> uri.substringBefore("?") }
    }

    private suspend fun createNewRoom() {
        val productId = requireNotNull(_productId.value)
        val chatRoomState = requireNotNull(_chatRoomStateAsSuccess?.data)
        val receiverId = chatRoomState.storeBrief.storeId
        val productBrief = chatRoomState.productBrief

        val roomId = chatRoomRepository.createChatRoom(productId, receiverId).getOrThrow()
        chatRoomRepository.enterChatRoom(roomId)
        fetchChatRoomInformationByRoomId(roomId)
        sendChatMessageUseCase(SendMessage.Product(roomId, null, productBrief))
    }

    private suspend fun patchChatProduct() {
        val roomId = requireNotNull(_roomId.value)
        val productBrief = requireNotNull(_chatRoomStateAsSuccess?.data?.productBrief)
        chatRoomRepository.patchChatRoomProduct(roomId, productBrief.productId)
        sendChatMessageUseCase(SendMessage.Product(roomId, null, productBrief))
    }

    fun toggleStoreBlockState(targetState: Boolean) = viewModelScope.launch {
        fetchStoreId()
        runCatching {
            _storeId.value?.let { storeId ->
                when (targetState) {
                    true -> storeRepository.blockStore(storeId).getOrThrow()
                    false -> storeRepository.unblockStore(storeId).getOrThrow()
                }
            }
        }.onSuccess {
            _productId.value?.let { productId ->
                getChatRoomInformation(productId, null)
                _sideEffect.send(
                    ChatRoomSideEffect.OnChangeBlockState(targetState)
                )
            }
        }.onFailure(Timber::e)
    }

    private fun fetchStoreId() = viewModelScope.launch {
        storeRepository.fetchStoreInfo()
            .onSuccess { storeInfo ->
                _storeId.update { storeInfo.storeId }
            }
    }

    internal fun trackReportMarket() = mixpanel?.track(OPENED_REPORT_MARKET)

    companion object Companion {
        private const val ROOM_ID_KEY = "chatRoomId"
        private const val PRODUCT_ID_KEY = "productId"
        private const val CHAT_ERROR_MSG = "오류가 발생했습니다. 다시 시도해주세요."
    }
}
