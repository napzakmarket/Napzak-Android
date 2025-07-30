package com.napzak.market.chat.chatroom

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.request.ImageRequest
import com.napzak.market.chat.chatroom.component.ChatRoomBottomSheet
import com.napzak.market.chat.chatroom.component.ChatRoomInputField
import com.napzak.market.chat.chatroom.component.ChatRoomProductSection
import com.napzak.market.chat.chatroom.component.ChatRoomTopBar
import com.napzak.market.chat.chatroom.component.NapzakWithdrawDialog
import com.napzak.market.chat.chatroom.component.chatitem.ChatDate
import com.napzak.market.chat.chatroom.component.chatitem.ChatImageItem
import com.napzak.market.chat.chatroom.component.chatitem.ChatNotice
import com.napzak.market.chat.chatroom.component.chatitem.ChatProduct
import com.napzak.market.chat.chatroom.component.chatitem.ChatText
import com.napzak.market.chat.chatroom.component.chatitem.MyChatItemContainer
import com.napzak.market.chat.chatroom.component.chatitem.OpponentChatItemContainer
import com.napzak.market.chat.chatroom.preview.mockChatRoom
import com.napzak.market.chat.chatroom.preview.mockChats
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.R.drawable.img_empty_chat_room
import com.napzak.market.designsystem.component.image.ZoomableImageScreen
import com.napzak.market.designsystem.component.loading.NapzakLoadingOverlay
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.drawable.img_user_blocked_popup
import com.napzak.market.feature.chat.R.string.chat_room_empty_guide_1
import com.napzak.market.feature.chat.R.string.chat_room_empty_guide_2
import com.napzak.market.ui_util.ScreenPreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber


@Composable
internal fun ChatRoomRoute(
    onProductDetailNavigate: (Long) -> Unit,
    onStoreReportNavigate: (Long) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatRoomViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val chatListState = rememberLazyListState()

    val chatRoomState by viewModel.chatRoomState.collectAsStateWithLifecycle()
    val chatItems by viewModel.chatItems.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.prepareChatRoom()

        onPauseOrDispose {
            viewModel.leaveChatRoom()
        }
    }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is ChatRoomSideEffect.OnSendChatMessage -> chatListState.scrollToItem(0)
                    is ChatRoomSideEffect.OnWithdrawChatRoom -> onNavigateUp()
                }
            }
    }

    ChatRoomScreen(
        chat = viewModel.chat,
        chatItems = chatItems.toImmutableList(),
        chatRoomState = chatRoomState,
        chatListState = chatListState,
        opponentImageUrl = "",
        onChatChange = { viewModel.chat = it },
        onProductDetailClick = onProductDetailNavigate,
        onReportClick = onStoreReportNavigate,
        onExitChatRoomClick = viewModel::withdrawChatRoom,
        onNavigateUp = onNavigateUp,
        onSendChatClick = viewModel::sendTextMessage,
        onPhotoSelect = viewModel::sendImageMessage,
        modifier = modifier,
    )
}

@Composable
internal fun ChatRoomScreen(
    chat: String,
    chatItems: ImmutableList<ReceiveMessage<*>>,
    chatRoomState: ChatRoomUiState,
    chatListState: LazyListState,
    opponentImageUrl: String,
    onChatChange: (String) -> Unit,
    onProductDetailClick: (Long) -> Unit,
    onReportClick: (Long) -> Unit,
    onExitChatRoomClick: () -> Unit,
    onNavigateUp: () -> Unit,
    onSendChatClick: (String) -> Unit,
    onPhotoSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (chatRoomState.chatRoomState) {
        is UiState.Loading -> {
            NapzakLoadingOverlay()
        }

        is UiState.Success -> {
            val chatRoom = chatRoomState.chatRoomState.data
            var isBottomSheetVisible by remember { mutableStateOf(false) }
            var isWithdrawDialogVisible by remember { mutableStateOf(false) }
            var selectedImageUrl: String? by remember { mutableStateOf(null) }

            ChatImageZoomScreen(
                selectedImageUrl = selectedImageUrl,
                onBackClick = { selectedImageUrl = null },
                modifier = modifier
            )

            if (isWithdrawDialogVisible) {
                NapzakWithdrawDialog(
                    onConfirmClick = { onExitChatRoomClick() },
                    onDismissClick = { isWithdrawDialogVisible = false },
                )
            }

            Column(
                modifier = modifier
                    .systemBarsPadding()
                    .fillMaxSize()
                    .background(NapzakMarketTheme.colors.white)
                    .imePadding(),
            ) {
                ChatRoomTopBar(
                    storeName = chatRoom.storeBrief?.nickname ?: "",
                    onBackClick = onNavigateUp,
                    onMenuClick = { isBottomSheetVisible = true },
                )

                chatRoom.productBrief?.let { product ->
                    ChatRoomProductSection(
                        product = product,
                        onClick = { onProductDetailClick(product.productId) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                ChatRoomRecordView(
                    listState = chatListState,
                    chatItems = chatItems,
                    opponentImageUrl = opponentImageUrl,
                    isOpponentWithdrawn = chatRoomState.isOpponentWithdrawn,
                    onItemClick = { message ->
                        when (message) {
                            is ReceiveMessage.Product -> onProductDetailClick(message.product.productId)
                            is ReceiveMessage.Image -> selectedImageUrl = message.imageUrl
                            else -> {}
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                ChatRoomInputField(
                    text = chat,
                    enabled = chatRoomState.isChatDisabled,
                    onSendClick = onSendChatClick,
                    onTextChange = onChatChange,
                    onPhotoSelect = onPhotoSelect,
                )
            }

            if (isBottomSheetVisible) {
                ChatRoomBottomSheet(
                    onReportClick = { onReportClick(1) }, //TODO: 스토어ID로 변경
                    onExitClick = { isWithdrawDialogVisible = true },
                    onDismissRequest = { isBottomSheetVisible = false },
                )
            }
        }

        else -> {
            /*TODO: 채팅방 정보를 불러오지 못하는 경우에 대한 화면 구현*/
            Timber.tag("ChatRoom").d("none ChatRoomScreen called")
        }
    }
}

@Composable
private fun ChatRoomRecordView(
    listState: LazyListState,
    chatItems: ImmutableList<ReceiveMessage<*>>,
    opponentImageUrl: String,
    isOpponentWithdrawn: Boolean,
    onItemClick: (ReceiveMessage<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val opponentProfileImageRequest = remember(opponentImageUrl) {
        ImageRequest
            .Builder(context)
            .data(opponentImageUrl)
            .build()
    }

    Box(modifier) {
        if (chatItems.isEmpty()) {
            EmptyChatScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
            )
        } else {
            LazyColumn(
                state = listState,
                reverseLayout = true,
                contentPadding = PaddingValues(vertical = 30.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
            ) {
                itemsIndexed(chatItems) { index, chatItem ->
                    val nextChatItem =
                        if (index > 0) chatItems[index - 1] else null
                    val previousChatItem =
                        if (index < chatItems.lastIndex) chatItems[index + 1] else null


                    ChatItemRenderer(
                        opponentImageRequest = opponentProfileImageRequest,
                        chatItem = chatItem,
                        nextChatItem = nextChatItem,
                        previousChatItem = previousChatItem,
                        onItemClick = { onItemClick(chatItem) },
                    )

                    ChatItemSpacer(
                        currentChatItem = chatItem,
                        previousChatItem = previousChatItem,
                    )
                }
            }
        }

        if (isOpponentWithdrawn) {
            Image(
                painter = painterResource(img_user_blocked_popup),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 23.dp),
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
    onItemClick: () -> Unit,
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
                content = {
                    ChatItemView(
                        chatItem = chatItem,
                        onItemClick = onItemClick
                    )
                },
            )

            false -> OpponentChatItemContainer(
                imageRequest = opponentImageRequest,
                isProfileImageVisible = !isChatDirectionEqualsPrevious || isPreviousItemProduct,
                isProduct = chatItem is ReceiveMessage.Product,
                timeStamp = timeStamp,
                isRead = chatItem.isRead,
                content = {
                    ChatItemView(
                        chatItem = chatItem,
                        onItemClick = onItemClick
                    )
                },
            )

            null -> ChatItemView(chatItem = chatItem) // ChatDate()가 표시된다.
        }
    }
}

@Composable
private fun ChatItemView(
    chatItem: ReceiveMessage<*>,
    onItemClick: () -> Unit = {},
) {
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
                onClick = onItemClick,
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
                    onNavigateClick = onItemClick,
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

@Composable
private fun ChatImageZoomScreen(
    selectedImageUrl: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(selectedImageUrl != null) {
        onBackClick()
    }

    selectedImageUrl?.let {
        ZoomableImageScreen(
            imageUrls = listOf(it).toImmutableList(),
            initialPage = 0,
            contentDescription = null,
            onBackClick = onBackClick,
            modifier = modifier
                .systemBarsPadding()
                .fillMaxSize(),
        )
    }
}

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
            onPhotoSelect = {},
            chatRoomState = ChatRoomUiState(UiState.Success(mockChatRoom)),
            chatListState = rememberLazyListState(),
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
            onPhotoSelect = {},
            chatRoomState = ChatRoomUiState(UiState.Success(mockChatRoom)),
            chatListState = rememberLazyListState(),
        )
    }
}
