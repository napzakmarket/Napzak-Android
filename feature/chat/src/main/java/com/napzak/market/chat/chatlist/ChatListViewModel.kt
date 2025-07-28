package com.napzak.market.chat.chatlist

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.chat.chatlist.model.ChatRoomDetail
import com.napzak.market.common.state.UiState
import com.napzak.market.notification.repository.NotificationRepository
import com.napzak.market.notification.usecase.GetNotificationSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
) : ViewModel() {
    private val _chatRoomsState = MutableStateFlow<UiState<List<ChatRoomDetail>>>(UiState.Loading)
    val chatRoomsState = _chatRoomsState.asStateFlow()

    private val _isNotificationModalOpen = MutableStateFlow(false)
    val isNotificationModalOpen: StateFlow<Boolean> = _isNotificationModalOpen.asStateFlow()
    private val _isAppPermissionGranted = MutableStateFlow(false)
    val isAppPermissionGranted: StateFlow<Boolean> = _isAppPermissionGranted.asStateFlow()
    private val _isSystemPermissionGranted = MutableStateFlow(false)
    val isSystemPermissionGranted: StateFlow<Boolean> = _isSystemPermissionGranted.asStateFlow()

    private fun checkSystemPermission(context: Context) {
        val systemPermission = NotificationManagerCompat.from(context).areNotificationsEnabled()
        _isSystemPermissionGranted.value = systemPermission
    }

    private suspend fun checkAppPermission() {
        val pushToken = notificationRepository.getPushToken()

        if (pushToken != null) getNotificationSettingsUseCase(pushToken)
            .onSuccess { _isAppPermissionGranted.value = it.allowMessage }
            .onFailure { Timber.e(it) }
        else Timber.tag("FCM_TOKEN")
            .d("ChatList-checkAndSetNotificationModal() : pushToken == null")
    }

    fun checkAndSetNotificationModal(context: Context) = viewModelScope.launch {
        val hasShown = notificationRepository.getNotificationModalShown()
        if (hasShown == true) return@launch

        checkSystemPermission(context)
        checkAppPermission()
        if (!_isSystemPermissionGranted.value || !_isAppPermissionGranted.value) {
            _isNotificationModalOpen.value = true
        }

        notificationRepository.updateNotificationModalShown()
    }

    fun updateNotificationModelOpenState() {
        _isNotificationModalOpen.value = false
    }

    fun fetchChatRooms() {
        _chatRoomsState.update { UiState.Success(ChatRoomDetail.mockList) }
    }
}
