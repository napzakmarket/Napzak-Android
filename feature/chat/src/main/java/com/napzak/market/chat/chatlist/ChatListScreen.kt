package com.napzak.market.chat.chatlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.chat.chatlist.component.ChatRoomItem
import com.napzak.market.chat.chatlist.component.NotificationPermissionModal
import com.napzak.market.chat.chatlist.model.ChatRoomDetail
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.R.drawable.img_empty_chat_list
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_list_empty_guide_1
import com.napzak.market.feature.chat.R.string.chat_list_empty_guide_2
import com.napzak.market.feature.chat.R.string.chat_list_top_bar
import com.napzak.market.feature.chat.R.string.permission_modal_app_setting_off_title
import com.napzak.market.feature.chat.R.string.permission_modal_both_setting_off_content
import com.napzak.market.feature.chat.R.string.permission_modal_setting_off_content
import com.napzak.market.feature.chat.R.string.permission_modal_system_app_setting_off_title
import com.napzak.market.feature.chat.R.string.permission_modal_system_setting_off_title
import com.napzak.market.ui_util.ScreenPreview
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.openSystemNotificationSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ChatListRoute(
    onChatRoomNavigate: (Long) -> Unit,
    onSettingsNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatListViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val chatRoomsState by viewModel.chatRoomsState.collectAsStateWithLifecycle()
    val isNotificationModalOpen by viewModel.isNotificationModalOpen.collectAsStateWithLifecycle()
    val isSystemPermissionGranted by viewModel.isSystemPermissionGranted.collectAsStateWithLifecycle()
    val isAppPermissionGranted by viewModel.isAppPermissionGranted.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.fetchChatRooms()
        viewModel.checkAndSetNotificationModal(context)
        onPauseOrDispose {
            // no resource to be cleared
        }
    }

    ChatListScreen(
        chatRoomsState = chatRoomsState,
        isNotificationModalOpen = isNotificationModalOpen,
        isSystemPermissionGranted = isSystemPermissionGranted,
        isAppPermissionGranted = isAppPermissionGranted,
        onChatRoomClick = { chatRoom -> onChatRoomNavigate(chatRoom.chatRoomId) },
        onDismissRequest = viewModel::updateNotificationModelOpenState,
        onSystemSettingNavigate = context::openSystemNotificationSettings,
        onSettingsNavigate = onSettingsNavigate,
        modifier = modifier,
    )
}

@Composable
private fun ChatListScreen(
    chatRoomsState: UiState<List<ChatRoomDetail>>,
    isNotificationModalOpen: Boolean,
    isSystemPermissionGranted: Boolean,
    isAppPermissionGranted: Boolean,
    onChatRoomClick: (ChatRoomDetail) -> Unit,
    onDismissRequest: () -> Unit,
    onSystemSettingNavigate: () -> Unit,
    onSettingsNavigate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (titleRes, contentRes) = when {
        !isAppPermissionGranted && isSystemPermissionGranted ->
            permission_modal_app_setting_off_title to permission_modal_setting_off_content

        isAppPermissionGranted && !isSystemPermissionGranted ->
            permission_modal_system_setting_off_title to permission_modal_setting_off_content

        !isAppPermissionGranted && !isSystemPermissionGranted ->
            permission_modal_system_app_setting_off_title to permission_modal_both_setting_off_content

        else -> null to null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.white),
    ) {
        ChatListTopBar()

        when (chatRoomsState) {
            is UiState.Loading -> {
                /*TODO: 로딩화면 구현*/
            }

            is UiState.Success -> {
                ChatListColumn(
                    chatRooms = chatRoomsState.data.toImmutableList(),
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
        if (titleRes != null && contentRes != null) {
            NotificationPermissionModal(
                title = stringResource(titleRes),
                content = stringResource(contentRes),
                onDismissRequest = onDismissRequest,
                onButtonClick = {
                    if (!isSystemPermissionGranted) onSystemSettingNavigate()
                    else if (!isAppPermissionGranted) onSettingsNavigate()
                    onDismissRequest()
                },
            )
        }
    }
}

@Composable
internal fun ChatListTopBar(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .napzakGradientShadow(
                height = 4.dp,
                startColor = NapzakMarketTheme.colors.shadowBlack,
                endColor = NapzakMarketTheme.colors.transWhite,
                direction = ShadowDirection.Bottom,
            )
            .padding(top = 34.dp, bottom = 20.dp, start = 20.dp),
    ) {
        Text(
            text = stringResource(chat_list_top_bar),
            style = NapzakMarketTheme.typography.body16b,
            color = NapzakMarketTheme.colors.gray400,
        )
    }
}

@Composable
private fun ChatListColumn(
    chatRooms: ImmutableList<ChatRoomDetail>,
    onChatRoomClick: (ChatRoomDetail) -> Unit,
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
                    nickname = storeName,
                    lastMessage = lastMessage,
                    profileImageUrl = storeImage,
                    unReadMessageCount = unReadMessageCount,
                    timeStamp = timeStamp,
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
    val chatRoomsState = UiState.Success(ChatRoomDetail.mockList)
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
