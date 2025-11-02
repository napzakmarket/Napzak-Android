package com.napzak.market.chat.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.chat.repository.ChatRepository
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
) : ViewModel() {
    private val _chatRoomsState = MutableStateFlow(ChatListUiState.Empty)
    val chatRoomsState = _chatRoomsState.asStateFlow()
    private val _notificationState = MutableStateFlow(NotificationState.Empty)
    val notificationState = _notificationState.asStateFlow()

    fun prepareChatRooms() = viewModelScope.launch {
        fetchChatRooms()
        collectChatRoomsState()
    }

    private suspend fun fetchChatRooms() {
        updateUiState(UiState.Loading)
        chatRepository.fetchChatRoomsFromRemote()
            .onFailure { t ->
                updateUiState(UiState.Failure(t.message.toString()))
            }
    }

    private suspend fun collectChatRoomsState() {
        chatRepository.getChatRoomsFlow().collect { chatRooms ->
            if (chatRooms.isEmpty()) updateUiState(UiState.Empty)
            else updateUiState(UiState.Success(chatRooms.toImmutableList()))
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
}
