package com.napzak.market.chat.chatlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.chat.repository.ChatSocketRepository
import com.napzak.market.common.state.UiState
import com.napzak.market.notification.repository.NotificationRepository
import com.napzak.market.notification.usecase.GetNotificationSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val chatSocketRepository: ChatSocketRepository,
    private val notificationRepository: NotificationRepository,
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
) : ViewModel() {
    private val _chatRoomsState = MutableStateFlow<UiState<List<ChatRoom>>>(UiState.Loading)
    val chatRoomsState = _chatRoomsState.asStateFlow()
    private val _notificationState = MutableStateFlow<NotificationState>(NotificationState())
    val notificationState = _notificationState.asStateFlow()

    private var _chatRoomPair by mutableStateOf(mapOf<Long, ChatRoom>() to listOf<ChatRoom>())
    private var _myStoreId: Long? = null

    private var _messageFlow: Flow<ReceiveMessage<*>>? = null

    fun fetchChatRooms() = viewModelScope.launch {
        chatRepository.getChatRooms()
            .onSuccess { (myStoreId, chatRooms) ->
                _myStoreId = myStoreId

                if (chatRooms.isEmpty()) {
                    _chatRoomsState.update { UiState.Empty }
                    return@launch
                }

                val map = mutableMapOf<Long, ChatRoom>()
                val list = mutableListOf<ChatRoom>()

                chatRooms.forEach { chatRoom ->
                    chatSocketRepository.subscribeChatRoom(chatRoom.roomId).onSuccess {
                        map[chatRoom.roomId] = chatRoom
                        list.add(chatRoom)
                    }
                }

                _chatRoomPair = map to list
                _chatRoomsState.update { UiState.Success(list) }
            }
            .onFailure { error ->
                _chatRoomsState.update { UiState.Failure(error.message.toString()) }
            }
    }

    fun collectChatMessages() {
        viewModelScope.launch {
            try {
                if (_messageFlow == null) {
                    val storeId = requireNotNull(_myStoreId) { "myStoreId가 null입니다" }
                    _messageFlow = chatSocketRepository.getMessageFlow(storeId)
                    _messageFlow?.collect { message -> updateChatRoom(message) }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun updateChatRoom(message: ReceiveMessage<*>) {
        try {
            val roomId = message.roomId
                ?: throw IllegalStateException("메시지에 roomId가 없습니다")

            val chatRoomMap = _chatRoomPair.first
            val chatRoomList = _chatRoomPair.second

            if (chatRoomMap[roomId] == null) {
                fetchChatRooms()
                return
            }

            chatRoomMap[roomId]?.let { chatRoom ->
                val list = chatRoomList.toMutableList()
                val map = chatRoomMap.toMutableMap()
                val unReadMessageCount =
                    if (message.isMessageOwner == false) chatRoom.unreadMessageCount.inc()
                    else chatRoom.unreadMessageCount

                val room = chatRoom.copy(
                    lastMessage = getLastMessage(message),
                    unreadMessageCount = unReadMessageCount,
                )

                list.removeIf { it.roomId == roomId }
                list.add(0, room)
                map[roomId] = room

                _chatRoomPair = map to list
                _chatRoomsState.value = UiState.Success(list)
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
    }

    private fun getLastMessage(message: ReceiveMessage<*>): String {
        return when (message) {
            is ReceiveMessage.Image -> "사진"
            is ReceiveMessage.Text -> message.text
            is ReceiveMessage.Notice -> message.notice
            else -> ""
        }
    }

    private suspend fun checkAppPermission() {
        val pushToken = notificationRepository.getPushToken()

        if (pushToken != null) getNotificationSettingsUseCase(pushToken)
            .onSuccess { result ->
                _notificationState.update { it.copy(isAppPermissionGranted = result.allowMessage) }
            }
            .onFailure { Timber.e(it) }
        else Timber.tag("FCM_TOKEN")
            .d("ChatList-checkAndSetNotificationModal() : pushToken == null")
    }

    fun checkAndSetNotificationModal(isSystemPermissionGranted: Boolean) = viewModelScope.launch {
        val hasShown = notificationRepository.getNotificationModalShown()
        if (hasShown == true) return@launch

        checkAppPermission()
        if (!isSystemPermissionGranted || !_notificationState.value.isAppPermissionGranted) {
            _notificationState.update { it.copy(isNotificationModalOpen = true) }
        }

        notificationRepository.updateNotificationModalShown()
    }

    fun updateNotificationModelOpenState() {
        _notificationState.update { it.copy(isNotificationModalOpen = false) }
    }

    companion object {
        private const val TAG = "ChatList"
    }
}
