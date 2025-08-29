package com.napzak.market.chat.chatlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRepository
import com.napzak.market.chat.usecase.GetAllChatFlowsUseCase
import com.napzak.market.common.state.UiState
import com.napzak.market.notification.repository.NotificationRepository
import com.napzak.market.notification.usecase.GetNotificationSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val notificationRepository: NotificationRepository,
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val getAllChatFlowsUseCase: GetAllChatFlowsUseCase,
) : ViewModel() {
    private val _chatRoomsState = MutableStateFlow(ChatListUiState.Empty)
    val chatRoomsState = _chatRoomsState.asStateFlow()
    private val _notificationState = MutableStateFlow(NotificationState.Empty)
    val notificationState = _notificationState.asStateFlow()

    private var _chatRoomPair by mutableStateOf(mapOf<Long, ChatRoom>() to listOf<ChatRoom>())
    private val isCollectingMessage = MutableStateFlow(false)

    fun prepareChatRooms() {
        fetchChatRooms()
        collectChatMessages()
    }

    private fun fetchChatRooms() = viewModelScope.launch {
        chatRepository.getChatRooms()
            .onSuccess { (_, chatRooms) ->
                if (chatRooms.isEmpty()) {
                    updateUiState(UiState.Empty)
                    return@launch
                }

                val map = mutableMapOf<Long, ChatRoom>()
                val list = mutableListOf<ChatRoom>()

                chatRooms.forEach { chatRoom ->
                    map[chatRoom.roomId] = chatRoom
                    list.add(chatRoom)
                }

                _chatRoomPair = map to list
                updateUiState(UiState.Success(list.toImmutableList()))
            }
            .onFailure { e ->
                Timber.tag(TAG).e(e)
                updateUiState(UiState.Failure(e.message.toString()))
            }
    }

    private fun collectChatMessages() {
        try {
            if (!isCollectingMessage.value) {
                viewModelScope.launch {
                    isCollectingMessage.update { true }
                    getAllChatFlowsUseCase().collect { message ->
                        if (message.isMessage) {
                            updateChatRoom(message)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
            updateUiState(UiState.Failure(e.message.toString()))
            isCollectingMessage.update { false }
        }
    }

    private fun updateChatRoom(message: ReceiveMessage<*>) {
        try {
            val roomId = requireNotNull(message.roomId) { "메시지에 roomId가 없습니다" }
            val (chatRoomMap, chatRoomList) = _chatRoomPair

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
                    lastMessage = message.toLastMessage(),
                    unreadMessageCount = unReadMessageCount,
                )

                list.removeIf { it.roomId == roomId }
                list.add(0, room)
                map[roomId] = room

                _chatRoomPair = map to list

                updateUiState(UiState.Success(list.toImmutableList()))
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
    }

    private fun ReceiveMessage<*>.toLastMessage(): String {
        return when (this) {
            is ReceiveMessage.Image -> "사진"
            is ReceiveMessage.Text -> text
            is ReceiveMessage.Notice -> notice
            else -> ""
        }
    }

    private fun updateUiState(loadState: UiState<ImmutableList<ChatRoom>>) {
        _chatRoomsState.update { currentState ->
            currentState.copy(
                loadState = loadState,
            )
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

    fun checkAndSetNotificationModal(systemPermission: Boolean) = viewModelScope.launch {
        isSystemPermissionEqual(systemPermission)
        val hasShown = notificationRepository.getNotificationModalShown()
        if (hasShown == true) return@launch

        checkAppPermission()
        if (!systemPermission || !_notificationState.value.isAppPermissionGranted) {
            _notificationState.update { it.copy(isNotificationModalOpen = true) }
        }

        notificationRepository.updateNotificationModalShown()
    }

    fun updateNotificationModelOpenState() {
        _notificationState.update { it.copy(isNotificationModalOpen = false) }
    }

    private suspend fun isSystemPermissionEqual(newPermission: Boolean) {
        val lastedSystemPermission = notificationRepository.getSystemNotificationPermission()
        if (lastedSystemPermission != newPermission) {
            notificationRepository.setSystemNotificationPermission(newPermission)
            if (!newPermission) notificationRepository.updateNotificationModalShown(false)
        }
    }

    companion object {
        private const val TAG = "ChatList"
    }
}
