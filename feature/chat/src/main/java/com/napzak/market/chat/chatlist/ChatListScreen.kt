package com.napzak.market.chat.chatlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.chat.chatlist.component.ChatRoomItem
import com.napzak.market.chat.chatlist.component.NotificationPermissionModal
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.R.drawable.img_empty_chat_list
import com.napzak.market.designsystem.component.loading.NapzakLoadingOverlay
import com.napzak.market.designsystem.component.topbar.TitleTopBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_list_empty_guide_1
import com.napzak.market.feature.chat.R.string.chat_list_empty_guide_2
import com.napzak.market.feature.chat.R.string.chat_list_top_bar
import com.napzak.market.ui_util.ScreenPreview
import com.napzak.market.ui_util.openSystemNotificationSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber

@Composable
internal fun ChatListRoute(
    onChatRoomNavigate: (Long) -> Unit,
    onSettingsNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatListViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val chatRoomsState by viewModel.chatRoomsState.collectAsStateWithLifecycle()
    val notificationState by viewModel.notificationState.collectAsStateWithLifecycle()
    val systemPermission = NotificationManagerCompat.from(context).areNotificationsEnabled()

    LifecycleResumeEffect(Unit) {
        viewModel.prepareChatRooms()
        viewModel.checkAndSetNotificationModal(systemPermission)

        onPauseOrDispose {
            // No resource to close
        }
    }

    ChatListScreen(
        chatRoomsState = chatRoomsState,
        onChatRoomClick = { chatRoom -> onChatRoomNavigate(chatRoom.roomId) },
        isNotificationModalOpen = notificationState.isNotificationModalOpen,
        isSystemPermissionGranted = systemPermission,
        isAppPermissionGranted = notificationState.isAppPermissionGranted,
        onDismissRequest = viewModel::updateNotificationModelOpenState,
        onSystemSettingNavigate = context::openSystemNotificationSettings,
        onSettingsNavigate = onSettingsNavigate,
        modifier = modifier,
    )
}

@Composable
private fun ChatListScreen(
    chatRoomsState: UiState<List<ChatRoom>>,
    onChatRoomClick: (ChatRoom) -> Unit,
    isNotificationModalOpen: Boolean,
    isSystemPermissionGranted: Boolean,
    isAppPermissionGranted: Boolean,
    onDismissRequest: () -> Unit,
    onSystemSettingNavigate: () -> Unit,
    onSettingsNavigate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.white),
    ) {
        ChatListTopBar()

        when (chatRoomsState) {
            is UiState.Loading -> {
                NapzakLoadingOverlay()
            }

            is UiState.Success -> {
                val chatRooms = chatRoomsState.data

                ChatListColumn(
                    chatRooms = chatRooms.toImmutableList(),
                    onChatRoomClick = onChatRoomClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            }

            is UiState.Empty -> {
                EmptyChatListScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            }

            else -> {
                // 실패 시 빈 화면
            }
        }
    }

    if (isNotificationModalOpen) {
        NotificationPermissionModal(
            isAppPermissionGranted = isAppPermissionGranted,
            isSystemPermissionGranted = isSystemPermissionGranted,
            onDismissRequest = onDismissRequest,
            onButtonClick = {
                if (!isSystemPermissionGranted) onSystemSettingNavigate()
                else if (!isAppPermissionGranted) onSettingsNavigate()
                onDismissRequest()
            },
        )
    }
}

@Composable
internal fun ChatListTopBar(
    modifier: Modifier = Modifier,
) {
    TitleTopBar(
        title = stringResource(chat_list_top_bar),
        paddingValues = PaddingValues(start = 20.dp, top = 36.dp, bottom = 20.dp),
        modifier = modifier,
    )
}

@Composable
private fun ChatListColumn(
    chatRooms: ImmutableList<ChatRoom>,
    onChatRoomClick: (ChatRoom) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Composable
    fun ChatDivider() {
        HorizontalDivider(thickness = 1.dp, color = NapzakMarketTheme.colors.gray50)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 25.dp),
    ) {
        items(chatRooms) { chatRoom ->
            ChatDivider()
            with(chatRoom) {
                ChatRoomItem(
                    nickname = storeNickname,
                    lastMessage = lastMessage,
                    profileImageUrl = storePhoto,
                    unReadMessageCount = unreadMessageCount,
                    timeStamp = lastMessageAt,
                    onClick = { onChatRoomClick(chatRoom) },
                )
            }
        }
        item { ChatDivider() }
    }
}

@Composable
private fun EmptyChatListScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(img_empty_chat_list),
            contentDescription = null,
            modifier = Modifier.padding(end = 40.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(chat_list_empty_guide_1),
            style = NapzakMarketTheme.typography.body14sb,
            color = NapzakMarketTheme.colors.gray300,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(chat_list_empty_guide_2),
            style = NapzakMarketTheme.typography.caption12sb,
            color = NapzakMarketTheme.colors.gray200,
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@ScreenPreview
@Composable
private fun ChatListScreenPreview() {
    val chatRoomsState = UiState.Success(
        buildList {
            repeat(20) { index ->
                val randomHour = (0..12).random().toString()
                val randomMinute = (0..60).random().toString().padStart(2, '0')
                val randomCount = (0..1000).random()

                add(
                    ChatRoom(
                        roomId = index.toLong(),
                        storeNickname = "납자기$index",
                        storePhoto = "",
                        lastMessage = "${index}번째로 메세지를 보냄 ",
                        unreadMessageCount = randomCount,
                        lastMessageAt = "오후 $randomHour:$randomMinute",
                    )
                )
            }
        }
    )
    NapzakMarketTheme {
        ChatListScreen(
            chatRoomsState = chatRoomsState,
            isNotificationModalOpen = true,
            isSystemPermissionGranted = false,
            isAppPermissionGranted = false,
            onChatRoomClick = {},
            onDismissRequest = {},
            onSettingsNavigate = {},
            onSystemSettingNavigate = {},
        )
    }
}

@ScreenPreview
@Composable
private fun ChatListEmptyScreenPreview() {
    NapzakMarketTheme {
        ChatListScreen(
            chatRoomsState = UiState.Empty,
            isNotificationModalOpen = true,
            isSystemPermissionGranted = false,
            isAppPermissionGranted = false,
            onChatRoomClick = {},
            onDismissRequest = {},
            onSettingsNavigate = {},
            onSystemSettingNavigate = {},
        )
    }
}
