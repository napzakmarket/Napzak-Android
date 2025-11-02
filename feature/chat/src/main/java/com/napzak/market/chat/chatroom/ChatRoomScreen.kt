package com.napzak.market.chat.chatroom

import android.app.NotificationManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.napzak.market.chat.chatroom.component.ChatImageZoomScreen
import com.napzak.market.chat.chatroom.component.ChatRoomBottomSheet
import com.napzak.market.chat.chatroom.component.ChatRoomDialogSection
import com.napzak.market.chat.chatroom.component.ChatRoomInputField
import com.napzak.market.chat.chatroom.component.ChatRoomItemColumn
import com.napzak.market.chat.chatroom.component.ChatRoomProductSection
import com.napzak.market.chat.chatroom.component.ChatRoomTopBar
import com.napzak.market.chat.chatroom.state.ChatRoomPopupEvent
import com.napzak.market.chat.chatroom.state.ChatRoomPopupState
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.R.drawable.ic_no_chatting_history
import com.napzak.market.designsystem.R.drawable.ic_white_block
import com.napzak.market.designsystem.R.drawable.ic_white_unblock
import com.napzak.market.designsystem.component.loading.NapzakLoadingOverlay
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.drawable.img_user_blocked_popup
import com.napzak.market.feature.chat.R.string.chat_room_empty_guide_1
import com.napzak.market.feature.chat.R.string.chat_room_empty_guide_2
import com.napzak.market.feature.chat.R.string.chat_room_toast_block
import com.napzak.market.feature.chat.R.string.chat_room_toast_unblock
import com.napzak.market.ui_util.ScreenPreview
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
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
    val context = LocalContext.current
    val toast = LocalNapzakToast.current
    val chatListState = rememberLazyListState()

    val chatRoomState by viewModel.chatRoomState.collectAsStateWithLifecycle()
    val chatMessageFlow by viewModel.chatMessageFlow.collectAsStateWithLifecycle()
    val chatMessages = chatMessageFlow.collectAsLazyPagingItems()

    LifecycleResumeEffect(Unit) {
        viewModel.collectChatRoomInformation()
        viewModel.initializeChatRoom()

        onPauseOrDispose {
            viewModel.leaveChatRoom()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { (chatRoomState as? UiState.Success<ChatRoomInformation>)?.data?.roomId?.toInt() }
            .distinctUntilChanged()
            .filterNotNull()
            .collect { notificationId ->
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(notificationId)
            }
    }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is ChatRoomSideEffect.OnSendChatMessage -> {
                        chatListState.scrollToItem(0)
                    }

                    is ChatRoomSideEffect.OnReceiveChatMessage -> {
                        if (chatListState.firstVisibleItemIndex <= 0) {
                            chatListState.scrollToItem(0)
                        }
                    }

                    is ChatRoomSideEffect.OnWithdrawChatRoom -> onNavigateUp()

                    is ChatRoomSideEffect.OnErrorOccurred -> {
                        onNavigateUp()
                    }

                    is ChatRoomSideEffect.ShowToast -> toast.makeText(
                        toastType = ToastType.COMMON,
                        message = sideEffect.message,
                        yOffset = toast.toastOffsetWithBottomBar(),
                    )

                    is ChatRoomSideEffect.OnChangeBlockState -> {
                        val (message, icon) =
                            if (sideEffect.newState) chat_room_toast_block to ic_white_block
                            else chat_room_toast_unblock to ic_white_unblock
                        toast.makeText(
                            toastType = ToastType.COMMON,
                            message = context.getString(message),
                            icon = icon,
                            yOffset = 200,
                        )
                    }
                }
            }
    }

    ChatRoomScreen(
        chat = { viewModel.chat },
        chatMessages = chatMessages,
        chatRoomState = chatRoomState,
        chatListState = chatListState,
        onChatChange = { viewModel.chat = it },
        onProductDetailClick = onProductDetailNavigate,
        onReportClick = { productId ->
            viewModel.trackReportMarket()
            onStoreReportNavigate(productId)
        },
        onBlockClick = { viewModel.toggleStoreBlockState(true) },
        onUnblockClick = { viewModel.toggleStoreBlockState(false) },
        onWithdrawChatRoomClick = viewModel::withdrawChatRoom,
        onNavigateUp = onNavigateUp,
        onSendChatClick = viewModel::sendTextMessage,
        onPhotoSelect = viewModel::sendImageMessage,
        modifier = modifier,
    )
}

@Composable
internal fun ChatRoomScreen(
    chat: () -> String,
    chatMessages: LazyPagingItems<ReceiveMessage<*>>,
    chatRoomState: UiState<ChatRoomInformation>,
    chatListState: LazyListState,
    onChatChange: (String) -> Unit,
    onProductDetailClick: (Long) -> Unit,
    onReportClick: (Long) -> Unit,
    onBlockClick: () -> Unit,
    onUnblockClick: () -> Unit,
    onWithdrawChatRoomClick: () -> Unit,
    onNavigateUp: () -> Unit,
    onSendChatClick: (String) -> Unit,
    onPhotoSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (chatRoomState) {
        is UiState.Loading -> {
            NapzakLoadingOverlay()
        }

        is UiState.Success -> {
            val chatRoom = chatRoomState.data
            var selectedImageUrl by remember { mutableStateOf<String?>(null) }
            var popupState by remember { mutableStateOf(ChatRoomPopupState()) }
            fun updatePopupState(event: ChatRoomPopupEvent) {
                popupState = popupState.handleEvent(event)
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
            ) {
                ChatRoomTopBar(
                    storeName = chatRoom.storeBrief.nickname,
                    onBackClick = onNavigateUp,
                    onMenuClick = { updatePopupState(ChatRoomPopupEvent.ShowBottomSheet) },
                )

                chatRoom.productBrief.let { product ->
                    ChatRoomProductSection(
                        product = product,
                        onClick = {
                            val isOpponentWithdrawn = chatRoom.storeBrief.isWithdrawn
                            if (isOpponentWithdrawn.not()) {
                                onProductDetailClick(product.productId)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (chatMessages.itemCount == 0) {
                        EmptyChatScreen()
                    }

                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding(),
                    ) {
                        ChatRoomMessageSection(
                            chatMessages = chatMessages,
                            chatListState = chatListState,
                            chatRoomInformation = chatRoom,
                            onProductMessageClick = onProductDetailClick,
                            onImageMessageClick = { selectedImageUrl = it },
                            modifier = Modifier
                                .weight(1f)
                                .background(NapzakMarketTheme.colors.white),
                        )

                        ChatRoomInputField(
                            text = chat,
                            enabled = !chatRoom.isChatBlocked,
                            onSendClick = onSendChatClick,
                            onTextChange = onChatChange,
                            onPhotoSelect = {
                                selectedImageUrl = it
                                updatePopupState(ChatRoomPopupEvent.ShowPreview)
                            },
                        )
                    }
                }

            }

            selectedImageUrl?.let {
                ChatImageZoomScreen(
                    selectedImageUrl = it,
                    isPreview = popupState.isPreviewVisible,
                    onBackClick = {
                        selectedImageUrl = null
                        updatePopupState(ChatRoomPopupEvent.DismissPreview)
                    },
                    onSendClick = {
                        selectedImageUrl?.let(onPhotoSelect)
                        selectedImageUrl = null
                        updatePopupState(ChatRoomPopupEvent.DismissPreview)
                    },
                )
            }

            if (popupState.isBottomSheetVisible) {
                chatRoom.storeBrief.let { store ->
                    ChatRoomBottomSheet(
                        isStoreBlocked = store.isOpponentStoreBlocked,
                        onReportClick = { store.storeId.let(onReportClick) },
                        onBlockClick = {
                            if (store.isOpponentStoreBlocked) {
                                onUnblockClick()
                                updatePopupState(ChatRoomPopupEvent.DismissBottomSheet)
                            } else {
                                updatePopupState(ChatRoomPopupEvent.ShowBlockDialog)
                            }
                        },
                        onExitClick = {
                            updatePopupState(ChatRoomPopupEvent.ShowWithdrawDialog)
                        },
                        onDismissRequest = {
                            updatePopupState(ChatRoomPopupEvent.DismissBottomSheet)
                        },
                    )
                }
            }

            ChatRoomDialogSection(
                isWithdrawDialogVisible = popupState.isWithdrawDialogVisible,
                isBlockDialogVisible = popupState.isBlockDialogVisible,
                onWithdrawConfirm = onWithdrawChatRoomClick,
                onBlockConfirm = {
                    onBlockClick()
                    updatePopupState(ChatRoomPopupEvent.DismissOnBlockConfirmed)
                },
                onDismissClick = ::updatePopupState,
            )
        }

        else -> {
            /*TODO: 채팅방 정보를 불러오지 못하는 경우에 대한 화면 구현*/
            Timber.tag("ChatRoom").d("none ChatRoomScreen called")
        }
    }
}

@Composable
private fun ChatRoomMessageSection(
    chatMessages: LazyPagingItems<ReceiveMessage<*>>,
    chatListState: LazyListState,
    chatRoomInformation: ChatRoomInformation,
    onProductMessageClick: (Long) -> Unit,
    onImageMessageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (chatMessages.itemCount > 0) {
        Box(
            modifier = modifier,
        ) {
            ChatRoomItemColumn(
                listState = chatListState,
                chatMessages = chatMessages,
                opponentImageUrl = chatRoomInformation.storeBrief.storePhoto,
                onItemClick = { message ->
                    when (message) {
                        is ReceiveMessage.Product -> {
                            onProductMessageClick(message.product.productId)
                        }

                        is ReceiveMessage.Image -> {
                            onImageMessageClick(message.imageUrl)
                        }

                        else -> {} // no events
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )

            if (chatRoomInformation.isChatBlocked) {
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
}

@Composable
private fun EmptyChatScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(ic_no_chatting_history),
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

@ScreenPreview
@Composable
private fun ChatRoomScreenPreview() {
    NapzakMarketTheme {
        val chatMessages = flowOf<PagingData<ReceiveMessage<*>>>().collectAsLazyPagingItems()
        val chatRoomState = UiState.Success(ChatRoomInformation.mock())
        val chatListState = rememberLazyListState()
        ChatRoomScreen(
            chat = { "" },
            chatMessages = chatMessages,
            chatRoomState = chatRoomState,
            chatListState = chatListState,
            onChatChange = {},
            onSendChatClick = {},
            onProductDetailClick = {},
            onReportClick = {},
            onBlockClick = {},
            onUnblockClick = {},
            onWithdrawChatRoomClick = {},
            onNavigateUp = {},
            onPhotoSelect = {},
        )
    }
}
