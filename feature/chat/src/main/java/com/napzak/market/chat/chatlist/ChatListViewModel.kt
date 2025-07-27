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

    fun checkAndSetNotificationModal(context: Context) = viewModelScope.launch {
        val hasShown = notificationRepository.getNotificationModalShown()
        if (hasShown == true) return@launch

        val isSystemPermissionGranted =
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        val pushToken = notificationRepository.getPushToken()
        val isAppPermissionGranted = pushToken?.let {
            getNotificationSettingsUseCase(it).getOrNull()?.allowMessage != false
        } != false

        if (!isSystemPermissionGranted || !isAppPermissionGranted) {
            _isNotificationModalOpen.value = true
        }

        notificationRepository.setNotificationModalShow()
    }

    fun updateNotificationModelOpenState() {
        _isNotificationModalOpen.value = false
    }

    fun fetchChatRooms() {
        _chatRoomsState.update { UiState.Success(ChatRoomDetail.mockList) }
    }
}
