package com.napzak.market.chat.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.napzak.market.chat.chatlist.component.ChatRoomItem
import com.napzak.market.chat.chatlist.model.ChatRoomDetail
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_list_top_bar
import com.napzak.market.ui_util.ScreenPreview
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow

@Composable
internal fun ChatListRoute() {
    ChatListScreen(
        onChatRoomClick = {},
    )
}

@Composable
private fun ChatListScreen(
    onChatRoomClick: (ChatRoomDetail) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.white),
    ) {
        ChatListTopBar()
        ChatListColumn(
            chatRooms = ChatRoomDetail.mockList,
            onChatRoomClick = onChatRoomClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
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
    chatRooms: List<ChatRoomDetail>,
    onChatRoomClick: (ChatRoomDetail) -> Unit,
    modifier: Modifier = Modifier,
) {
    val chatDivider = @Composable {
        HorizontalDivider(thickness = 1.dp, color = NapzakMarketTheme.colors.gray50)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 25.dp),
    ) {
        items(chatRooms) { chatRoom ->
            chatDivider()
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
        item { chatDivider() }
    }
}

@ScreenPreview
@Composable
private fun ChatListScreenPreview() {
    NapzakMarketTheme {
        ChatListScreen(
            onChatRoomClick = {},
        )
    }
}
