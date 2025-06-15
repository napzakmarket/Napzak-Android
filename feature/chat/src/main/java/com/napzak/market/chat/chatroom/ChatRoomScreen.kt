package com.napzak.market.chat.chatroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.ImageRequest
import com.napzak.market.chat.chatroom.component.ChatRoomBottomSheet
import com.napzak.market.chat.chatroom.component.ChatRoomInputField
import com.napzak.market.chat.chatroom.component.ChatRoomProductSection
import com.napzak.market.chat.chatroom.component.ChatRoomTopBar
import com.napzak.market.chat.chatroom.component.chatitem.ChatDate
import com.napzak.market.chat.chatroom.component.chatitem.ChatImageItem
import com.napzak.market.chat.chatroom.component.chatitem.ChatNotice
import com.napzak.market.chat.chatroom.component.chatitem.ChatProduct
import com.napzak.market.chat.chatroom.component.chatitem.ChatText
import com.napzak.market.chat.chatroom.component.chatitem.MyChatItemContainer
import com.napzak.market.chat.chatroom.component.chatitem.OpponentChatItemContainer
import com.napzak.market.chat.chatroom.model.ChatDirection
import com.napzak.market.chat.chatroom.model.ChatItem
import com.napzak.market.chat.chatroom.model.ChatRoom
import com.napzak.market.chat.chatroom.preview.mockChats
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_room_input_field_hint
import com.napzak.market.ui_util.ScreenPreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ChatRoomRoute(
    onProductDetailNavigate: (Long) -> Unit,
    onStoreReportNavigate: (Long) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatRoomViewModel = hiltViewModel(),
) {
    val chatRoomState by viewModel.chatRoom.collectAsStateWithLifecycle()
    val chatItems by viewModel.chatItems.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        with(viewModel) {
            fetchChatItems()
            fetchChatRoomDetail()
        }
    }

    ChatRoomScreen(
        chatItems = chatItems.toImmutableList(),
        chatRoomState = chatRoomState,
        opponentImageUrl = "",
        onProductDetailClick = onProductDetailNavigate,
        onReportClick = onStoreReportNavigate,
        onExitChatRoomClick = viewModel::exitChatRoom,
        onNavigateUp = onNavigateUp,
        onSendChatClick = viewModel::sendChat,
        modifier = modifier,
    )
}

@Composable
internal fun ChatRoomScreen(
    chatItems: ImmutableList<ChatItem<*>>,
    chatRoomState: UiState<ChatRoom>,
    opponentImageUrl: String,
    onProductDetailClick: (Long) -> Unit,
    onReportClick: (Long) -> Unit,
    onExitChatRoomClick: (Long) -> Unit,
    onNavigateUp: () -> Unit,
    onSendChatClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (chatRoomState) {
        is UiState.Loading -> {
            /*TODO: 로딩화면 구현*/
        }

        is UiState.Success -> {
            val chatRoom = chatRoomState.data
            var isBottomSheetVisible by remember { mutableStateOf(false) }

            Column(
                modifier = modifier
                    .systemBarsPadding()
                    .fillMaxSize()
                    .background(NapzakMarketTheme.colors.white)
                    .imePadding(),
            ) {
                ChatRoomTopBar(
                    storeName = chatRoom.storeName,
                    onBackClick = onNavigateUp,
                    onMenuClick = { isBottomSheetVisible = true },
                )
                ChatRoomProductSection(
                    product = chatRoom.product,
                    onClick = { onProductDetailClick(chatRoom.product.productId) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (chatItems.isEmpty()) {
                    EmptyChatScreen(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                    )
                } else {
                    ChatRoomRecordView(
                        chatItems = chatItems,
                        opponentImageUrl = opponentImageUrl,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                    )
                }

                ChatRoomInputField(
                    hint = stringResource(chat_room_input_field_hint),
                    onSendClick = onSendChatClick,
                    onGalleryClick = {},
                )
            }

            if (isBottomSheetVisible) {
                ChatRoomBottomSheet(
                    onReportClick = { onReportClick(1) }, //TODO: 스토어ID로 변경
                    onExitClick = { onExitChatRoomClick(chatRoom.chatRoomId) },
                    onDismissRequest = { isBottomSheetVisible = false },
                )
            }
        }

        else -> {
            /*TODO: 채팅방 정보를 불러오지 못하는 경우에 대한 화면 구현*/
        }
    }
}

@Composable
private fun ChatRoomRecordView(
    chatItems: ImmutableList<ChatItem<*>>,
    opponentImageUrl: String,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val opponentProfileImageRequest = ImageRequest
        .Builder(context)
        .data(opponentImageUrl)
        .build()

    Column(
        modifier = modifier
            .verticalScroll(
                state = scrollState,
                reverseScrolling = true,
            ),
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // TODO: 채팅의 순서에 따라 chatMessages.reversed() 사용 여부 결정
        chatItems.reversed().let { reversedChatItems ->
            reversedChatItems.forEachIndexed { index, chatItem ->
                val previousChatItem =
                    if (index > 0) reversedChatItems[index - 1] else null
                val nextChatItem =
                    if (index < reversedChatItems.lastIndex) reversedChatItems[index + 1] else null

                ChatItemSpacer(
                    currentChatItem = chatItem,
                    previousChatItem = previousChatItem
                )
                ChatItemRenderer(
                    opponentImageRequest = opponentProfileImageRequest,
                    chatItem = chatItem,
                    nextChatItem = nextChatItem,
                    previousChatItem = previousChatItem,
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun ColumnScope.ChatItemRenderer(
    chatItem: ChatItem<*>,
    modifier: Modifier = Modifier,
    nextChatItem: ChatItem<*>? = null,
    previousChatItem: ChatItem<*>? = null,
    opponentImageRequest: ImageRequest,
) {
    val isChatDirectionEqualsPrevious = chatItem.direction == previousChatItem?.direction
    val isChatDirectionEqualsNext = chatItem.direction == nextChatItem?.direction
    val isTimeStampEqualsNext = chatItem.timeStamp == nextChatItem?.timeStamp
    val timeStamp = chatItem.timeStamp.takeIf {
        !isTimeStampEqualsNext || !isChatDirectionEqualsNext
    }

    val content: @Composable () -> Unit = {
        when (chatItem) {
            is ChatItem.Text -> {
                ChatText(text = chatItem.text, chatDirection = chatItem.direction)
            }

            is ChatItem.Image -> {
                ChatImageItem(
                    imageUrl = chatItem.imageUrl,
                    onClick = {},
                    modifier = modifier
                )
            }

            is ChatItem.Product -> {
                with(chatItem) {
                    ChatProduct(
                        direction = direction,
                        tradeType = product.tradeType,
                        genre = product.genreName,
                        name = product.productName,
                        price = product.price.toString(),
                        onNavigateClick = {},
                    )
                }
            }

            is ChatItem.Date -> {
                ChatDate(
                    date = chatItem.date,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            is ChatItem.Notice -> {
                ChatNotice(
                    notice = chatItem.notice,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }

    when (chatItem.direction) {
        ChatDirection.SENT -> MyChatItemContainer(
            timeStamp = timeStamp,
            content = content,
            modifier = modifier.align(Alignment.End),
            isRead = chatItem.isRead,
        )

        ChatDirection.RECEIVED -> OpponentChatItemContainer(
            imageRequest = opponentImageRequest,
            isProfileImageVisible = !isChatDirectionEqualsPrevious,
            timeStamp = timeStamp,
            content = content,
            modifier = modifier.align(Alignment.Start),
            isRead = chatItem.isRead,
        )

        null -> content() // ChatDate()가 표시된다.
    }
}

@Composable
private fun ChatItemSpacer(
    currentChatItem: ChatItem<*>? = null,
    previousChatItem: ChatItem<*>? = null,
) {
    val height: Dp = when {
        currentChatItem is ChatItem.Date || previousChatItem is ChatItem.Date -> 20.dp
        currentChatItem is ChatItem.Notice || previousChatItem is ChatItem.Notice -> 30.dp
        else -> 8.dp
    }
    Spacer(modifier = Modifier.height(height))
}

@Composable
private fun EmptyChatScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(com.napzak.market.designsystem.R.drawable.img_empty_chat_room),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "채팅을 시작해보세요!",
            style = NapzakMarketTheme.typography.body14sb,
            color = NapzakMarketTheme.colors.gray300,
        )
        Text(
            text = "안전한 거래를 위해 먼저 이야기를 나눠보세요",
            style = NapzakMarketTheme.typography.caption12sb,
            color = NapzakMarketTheme.colors.gray200,
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@ScreenPreview
@Composable
private fun ChatRoomScreenPreview() {
    NapzakMarketTheme {
        ChatRoomScreen(
            chatItems = mockChats.toImmutableList(),
            opponentImageUrl = "",
            onSendChatClick = {},
            onProductDetailClick = {},
            onReportClick = {},
            onExitChatRoomClick = {},
            onNavigateUp = {},
            chatRoomState = UiState.Success(ChatRoom.mock)
        )
    }
}

@ScreenPreview
@Composable
private fun ChatRoomScreenEmptyPreview() {
    NapzakMarketTheme {
        ChatRoomScreen(
            chatItems = emptyList<ChatItem<*>>().toImmutableList(),
            opponentImageUrl = "",
            onSendChatClick = {},
            onProductDetailClick = {},
            onReportClick = {},
            onExitChatRoomClick = {},
            onNavigateUp = {},
            chatRoomState = UiState.Success(ChatRoom.mock),
        )
    }
}
