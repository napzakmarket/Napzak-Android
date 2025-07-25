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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.chat.chatlist.component.ChatRoomItem
import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.R.drawable.img_empty_chat_list
import com.napzak.market.designsystem.component.NapzakLoadingOverlay
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_list_empty_guide_1
import com.napzak.market.feature.chat.R.string.chat_list_empty_guide_2
import com.napzak.market.feature.chat.R.string.chat_list_top_bar
import com.napzak.market.ui_util.ScreenPreview
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ChatListRoute(
    onChatRoomNavigate: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatListViewModel = hiltViewModel(),
) {
    val chatRoomsState by viewModel.chatRoomsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchChatRooms()
        viewModel.collectChatMessages()
    }

    ChatListScreen(
        chatRoomsState = chatRoomsState,
        onChatRoomClick = { chatRoom -> onChatRoomNavigate(chatRoom.roomId) },
        modifier = modifier,
    )
}

@Composable
private fun ChatListScreen(
    chatRoomsState: UiState<Map<Long, ChatRoom>>,
    onChatRoomClick: (ChatRoom) -> Unit,
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
                val chatRooms = chatRoomsState.data.values.sortedByDescending { it.lastMessageAt }

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
        buildMap {
            repeat(20) { index ->
                val randomHour = (0..12).random().toString()
                val randomMinute = (0..60).random().toString().padStart(2, '0')
                val randomCount = (0..1000).random()

                put(
                    index.toLong(), ChatRoom(
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
            onChatRoomClick = {},
        )
    }
}

@ScreenPreview
@Composable
private fun ChatListEmptyScreenPreview() {
    NapzakMarketTheme {
        ChatListScreen(
            chatRoomsState = UiState.Empty,
            onChatRoomClick = {},
        )
    }
}
