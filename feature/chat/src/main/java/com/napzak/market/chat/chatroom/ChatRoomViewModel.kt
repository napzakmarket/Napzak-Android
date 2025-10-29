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
import com.napzak.market.chat.usecase.CreateChatRoomUseCase
import com.napzak.market.chat.usecase.GetChatRoomInformationUseCase
import com.napzak.market.chat.usecase.PatchChatRoomUseCase
import com.napzak.market.chat.usecase.SendChatMessageUseCase
import com.napzak.market.common.state.UiState
import com.napzak.market.mixpanel.MixpanelConstants.OPENED_REPORT_MARKET
import com.napzak.market.presigned_url.model.UploadImage
import com.napzak.market.presigned_url.model.UploadImage.ImageType.CHAT
import com.napzak.market.presigned_url.usecase.UploadImagesUseCase
import com.napzak.market.store.usecase.SetStoreBlockStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
    private val getChatRoomInformationUseCase: GetChatRoomInformationUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val createChatRoomUseCase: CreateChatRoomUseCase,
    private val patchChatRoomUseCase: PatchChatRoomUseCase,
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val setStoreBlockStateUseCase: SetStoreBlockStateUseCase,
) : ViewModel() {
    private val _roomId = MutableStateFlow<Long?>(savedStateHandle[ROOM_ID_KEY])
    private val _productId = MutableStateFlow<Long?>(savedStateHandle[PRODUCT_ID_KEY])

    private val _sideEffect = Channel<ChatRoomSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val _chatRoomState = MutableStateFlow<UiState<ChatRoomInformation>>(UiState.Loading)
    val chatRoomState = _chatRoomState.asStateFlow()

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

    fun initializeChatRoom() = viewModelScope.launch {
        getChatRoomInformationUseCase(_roomId.value, _productId.value)
            .onSuccess { (info, condition) ->
                _roomId.update { info.roomId }
                _productId.update { info.productBrief.productId }
                _chatRoomState.update { UiState.Success(info) }
                chatCondition = condition

                if (info.storeBrief.isWithdrawn) {
                    _sideEffect.trySend(ChatRoomSideEffect.OnWithdrawChatRoom)
                }
            }
            .onFailure { t ->
                Timber.e(t)
                with(_sideEffect) {
                    send(ChatRoomSideEffect.ShowToast(CHAT_ERROR_MSG))
                    send(ChatRoomSideEffect.OnErrorOccurred)
                }
            }
    }

    fun collectChatRoomInformation() = viewModelScope.launch {
        _roomId.flatMapLatest { roomId ->
            if (roomId == null) flowOf(UiState.Empty)
            else chatRoomRepository.getChatRoomInformationAsFlow(roomId).map { UiState.Success(it) }
        }.collect { chatRoomState ->
            (chatRoomState as? UiState.Success)?.let { state ->
                val roomId = state.data.roomId ?: return@collect
                val isOpponentOnline = state.data.isOpponentOnline
                if (isOpponentOnline) {
                    chatRoomRepository.markMessagesAsRead(roomId, true)
                }
            }
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
            prepareChatRoomContext(chatCondition)
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
            prepareChatRoomContext(chatCondition)
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

    private suspend fun prepareChatRoomContext(chatCondition: ChatCondition) {
        when (chatCondition) {
            ChatCondition.NEW_CHAT_ROOM -> {
                createNewRoom()
                delay(100L)
            }

            ChatCondition.PRODUCT_CHANGED -> {
                patchChatProduct()
                delay(100L)
            }

            ChatCondition.PRODUCT_NOT_CHANGED -> Unit
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
        val chatRoomState =
            requireNotNull(chatRoomState.value as? UiState.Success<ChatRoomInformation>).data
        val receiverId = chatRoomState.storeBrief.storeId
        _roomId.update {
            createChatRoomUseCase(productId, receiverId).getOrThrow()
        }
    }

    private suspend fun patchChatProduct() {
        val roomId = requireNotNull(_roomId.value)
        val productBrief =
            requireNotNull(chatRoomState.value as? UiState.Success<ChatRoomInformation>).data.productBrief
        patchChatRoomUseCase(roomId, productBrief)
    }

    fun toggleStoreBlockState(targetState: Boolean) = viewModelScope.launch {
        val storeId =
            (chatRoomState.value as? UiState.Success)?.data?.storeBrief?.storeId ?: return@launch
        setStoreBlockStateUseCase(storeId, targetState)
            .onSuccess {
                _productId.value?.let { productId ->
                    chatRoomRepository.getChatRoomInformation(productId, null)
                    _sideEffect.send(
                        ChatRoomSideEffect.OnChangeBlockState(targetState)
                    )
                }
            }.onFailure(Timber::e)
    }

    internal fun trackReportMarket() = mixpanel?.track(OPENED_REPORT_MARKET)

    companion object Companion {
        private const val ROOM_ID_KEY = "chatRoomId"
        private const val PRODUCT_ID_KEY = "productId"
        private const val CHAT_ERROR_MSG = "오류가 발생했습니다. 다시 시도해주세요."
    }
}
