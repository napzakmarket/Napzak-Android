package com.napzak.market.chat.chatroom

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalFocusManager
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
import com.napzak.market.chat.chatroom.preview.mockChats
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.StoreBrief
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.R.drawable.img_empty_chat_room
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_room_empty_guide_1
import com.napzak.market.feature.chat.R.string.chat_room_empty_guide_2
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
        viewModel.prepareChatRoom()
    }

    ChatRoomScreen(
        chat = viewModel.chat,
        chatItems = chatItems.toImmutableList(),
        chatRoomState = chatRoomState,
        opponentImageUrl = "",
        onChatChange = { viewModel.chat = it },
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
    chat: String,
    chatItems: ImmutableList<ReceiveMessage<*>>,
    chatRoomState: UiState<ChatRoomInformation>,
    opponentImageUrl: String,
    onChatChange: (String) -> Unit,
    onProductDetailClick: (Long) -> Unit,
    onReportClick: (Long) -> Unit,
    onExitChatRoomClick: (Long) -> Unit,
    onNavigateUp: () -> Unit,
    onSendChatClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

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
                    storeName = chatRoom.storeBrief.nickname,
                    onBackClick = onNavigateUp,
                    onMenuClick = { isBottomSheetVisible = true },
                )
                ChatRoomProductSection(
                    product = chatRoom.productBrief,
                    onClick = { onProductDetailClick(chatRoom.productBrief.productId) },
                    modifier = Modifier.fillMaxWidth(),
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
                    text = chat,
                    hint = stringResource(chat_room_input_field_hint),
                    onSendClick = onSendChatClick,
                    onTextChange = onChatChange,
                    onGalleryClick = {},
                )
            }

            if (isBottomSheetVisible) {
                ChatRoomBottomSheet(
                    onReportClick = { onReportClick(1) }, //TODO: 스토어ID로 변경
                    onExitClick = { chatRoom.roomId?.let { onExitChatRoomClick(it) } },
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
    chatItems: ImmutableList<ReceiveMessage<*>>,
    opponentImageUrl: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val opponentProfileImageRequest = remember(opponentImageUrl) {
        ImageRequest
            .Builder(context)
            .data(opponentImageUrl)
            .build()
    }

    LazyColumn(
        reverseLayout = true,
        contentPadding = PaddingValues(vertical = 30.dp),
        modifier = modifier,
    ) {
        itemsIndexed(chatItems) { index, chatItem ->
            val nextChatItem =
                if (index > 0) chatItems[index - 1] else null
            val previousChatItem =
                if (index < chatItems.lastIndex) chatItems[index + 1] else null

            ChatItemSpacer(
                currentChatItem = chatItem,
                previousChatItem = previousChatItem,
            )
            ChatItemRenderer(
                opponentImageRequest = opponentProfileImageRequest,
                chatItem = chatItem,
                nextChatItem = nextChatItem,
                previousChatItem = previousChatItem,
            )
        }
    }
}

@Composable
private fun ChatItemRenderer(
    chatItem: ReceiveMessage<*>,
    modifier: Modifier = Modifier,
    nextChatItem: ReceiveMessage<*>? = null,
    previousChatItem: ReceiveMessage<*>? = null,
    opponentImageRequest: ImageRequest,
) {
    val isPreviousItemProduct = previousChatItem is ReceiveMessage.Product
    val isChatDirectionEqualsPrevious = chatItem.isMessageOwner == previousChatItem?.isMessageOwner
    val isChatDirectionEqualsNext = chatItem.isMessageOwner == nextChatItem?.isMessageOwner
    val isTimeStampEqualsNext = chatItem.timeStamp == nextChatItem?.timeStamp
    val timeStamp = chatItem.timeStamp.takeIf {
        !isTimeStampEqualsNext || !isChatDirectionEqualsNext
    }
    val chatItemAlignment = when (chatItem.isMessageOwner) {
        true -> Alignment.TopEnd
        false -> Alignment.TopStart
        null -> Alignment.Center
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = chatItemAlignment,
    ) {
        when (chatItem.isMessageOwner) {
            true -> MyChatItemContainer(
                timeStamp = timeStamp,
                isRead = chatItem.isRead,
                content = { ChatItemView(chatItem = chatItem) },
            )

            false -> OpponentChatItemContainer(
                imageRequest = opponentImageRequest,
                isProfileImageVisible = !isChatDirectionEqualsPrevious || isPreviousItemProduct,
                isProduct = chatItem is ReceiveMessage.Product,
                timeStamp = timeStamp,
                isRead = chatItem.isRead,
                content = { ChatItemView(chatItem = chatItem) },
            )

            null -> ChatItemView(chatItem = chatItem) // ChatDate()가 표시된다.
        }
    }
}

@Composable
private fun ChatItemView(chatItem: ReceiveMessage<*>) {
    when (chatItem) {
        is ReceiveMessage.Text -> {
            ChatText(
                text = chatItem.text,
                isMessageOwner = chatItem.isMessageOwner,
            )
        }

        is ReceiveMessage.Image -> {
            ChatImageItem(
                imageUrl = chatItem.imageUrl,
                onClick = {},
            )
        }

        is ReceiveMessage.Product -> {
            with(chatItem) {
                ChatProduct(
                    tradeType = product.tradeType,
                    genre = product.genreName,
                    name = product.title,
                    price = product.price.toString(),
                    isMessageOwner = isMessageOwner,
                    onNavigateClick = {},
                )
            }
        }

        is ReceiveMessage.Date -> {
            ChatDate(
                date = chatItem.date,
            )
        }

        is ReceiveMessage.Notice -> {
            ChatNotice(
                notice = chatItem.notice,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun ChatItemSpacer(
    currentChatItem: ReceiveMessage<*>? = null,
    previousChatItem: ReceiveMessage<*>? = null,
) {
    val height: Dp = when {
        currentChatItem is ReceiveMessage.Date || previousChatItem is ReceiveMessage.Date -> 20.dp
        currentChatItem is ReceiveMessage.Notice || previousChatItem is ReceiveMessage.Notice -> 30.dp
        else -> 8.dp
    }
    Spacer(modifier = Modifier.height(height))
}

@Composable
private fun EmptyChatScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(img_empty_chat_room),
            contentDescription = null,
            modifier = Modifier.padding(end = 80.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(chat_room_empty_guide_1),
            style = NapzakMarketTheme.typography.body14sb,
            color = NapzakMarketTheme.colors.gray300,
        )
        Text(
            text = stringResource(chat_room_empty_guide_2),
            style = NapzakMarketTheme.typography.caption12sb,
            color = NapzakMarketTheme.colors.gray200,
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

private val mockChatRoom = ChatRoomInformation(
    roomId = 1,
    productBrief = ProductBrief(
        productId = 1,
        photo = "",
        tradeType = "",
        title = "",
        price = 10000,
        genreName = "",
        isPriceNegotiable = true,
    ),
    storeBrief = StoreBrief(
        storeId = 1,
        nickname = "",
        storePhoto = "",
        isWithdrawn = false
    )
)

@ScreenPreview
@Composable
private fun ChatRoomScreenPreview() {
    NapzakMarketTheme {
        ChatRoomScreen(
            chat = "",
            chatItems = mockChats.toImmutableList(),
            opponentImageUrl = "",
            onChatChange = {},
            onSendChatClick = {},
            onProductDetailClick = {},
            onReportClick = {},
            onExitChatRoomClick = {},
            onNavigateUp = {},
            chatRoomState = UiState.Success(mockChatRoom),
        )
    }
}

@ScreenPreview
@Composable
private fun ChatRoomScreenEmptyPreview() {
    NapzakMarketTheme {
        ChatRoomScreen(
            chat = "",
            chatItems = emptyList<ReceiveMessage<*>>().toImmutableList(),
            opponentImageUrl = "",
            onChatChange = {},
            onSendChatClick = {},
            onProductDetailClick = {},
            onReportClick = {},
            onExitChatRoomClick = {},
            onNavigateUp = {},
            chatRoomState = UiState.Success(mockChatRoom),
        )
    }
}
