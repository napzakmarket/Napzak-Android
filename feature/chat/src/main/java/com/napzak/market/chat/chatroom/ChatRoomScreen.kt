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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.chat.chatroom.component.ChatImageZoomScreen
import com.napzak.market.chat.chatroom.component.ChatRoomBottomSheet
import com.napzak.market.chat.chatroom.component.ChatRoomInputField
import com.napzak.market.chat.chatroom.component.ChatRoomItemColumn
import com.napzak.market.chat.chatroom.component.ChatRoomProductSection
import com.napzak.market.chat.chatroom.component.ChatRoomTopBar
import com.napzak.market.chat.chatroom.component.NapzakWithdrawDialog
import com.napzak.market.chat.chatroom.preview.mockChatRoom
import com.napzak.market.chat.chatroom.preview.mockChats
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.R.drawable.img_empty_chat_room
import com.napzak.market.designsystem.component.loading.NapzakLoadingOverlay
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.ToastFontType
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.drawable.img_user_blocked_popup
import com.napzak.market.feature.chat.R.string.chat_room_empty_guide_1
import com.napzak.market.feature.chat.R.string.chat_room_empty_guide_2
import com.napzak.market.ui_util.ScreenPreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
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
    val chatItems by viewModel.chatItems.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.prepareChatRoom()

        onPauseOrDispose {
            viewModel.leaveChatRoom()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { (chatRoomState.chatRoomState as? UiState.Success)?.data?.roomId?.toInt() }
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
                        fontType = ToastFontType.SMALL,
                        yOffset = toast.toastOffsetWithBottomBar(),
                    )
                }
            }
    }

    ChatRoomScreen(
        chat = { viewModel.chat },
        chatItems = chatItems.toImmutableList(),
        chatRoomState = chatRoomState,
        chatListState = chatListState,
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
    chat: () -> String,
    chatItems: ImmutableList<ReceiveMessage<*>>,
    chatRoomState: ChatRoomUiState,
    chatListState: LazyListState,
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
            val focusManager = LocalFocusManager.current
            val chatRoom = chatRoomState.chatRoomState.data
            var isBottomSheetVisible by remember { mutableStateOf(false) }
            var isWithdrawDialogVisible by remember { mutableStateOf(false) }
            var isPreviewVisible by remember { mutableStateOf(false) }
            var selectedImageUrl: String? by remember { mutableStateOf(null) }

            selectedImageUrl?.let {
                ChatImageZoomScreen(
                    selectedImageUrl = it,
                    isPreview = isPreviewVisible,
                    onBackClick = {
                        selectedImageUrl = null
                        isPreviewVisible = false
                    },
                    onSendClick = {
                        selectedImageUrl?.let(onPhotoSelect)
                        selectedImageUrl = null
                        isPreviewVisible = false
                    },
                )
            }

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
                        onClick = {
                            if (chatRoomState.isOpponentWithdrawn.not()) {
                                onProductDetailClick(product.productId)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (chatItems.isEmpty()) {
                        EmptyChatScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                        )
                    } else {
                        ChatRoomItemColumn(
                            listState = chatListState,
                            chatItems = chatItems,
                            opponentImageUrl = chatRoom.storeBrief?.storePhoto,
                            onItemClick = { message ->
                                when (message) {
                                    is ReceiveMessage.Product -> onProductDetailClick(message.product.productId)
                                    is ReceiveMessage.Image -> {
                                        selectedImageUrl = message.imageUrl
                                        focusManager.clearFocus()
                                    }

                                    else -> {}
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        if (chatRoomState.isOpponentWithdrawn) {
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

                ChatRoomInputField(
                    text = chat,
                    enabled = !chatRoomState.isChatDisabled,
                    onSendClick = onSendChatClick,
                    onTextChange = onChatChange,
                    onPhotoSelect = {
                        selectedImageUrl = it
                        isPreviewVisible = true
                    },
                )
            }

            if (isBottomSheetVisible) {
                ChatRoomBottomSheet(
                    onReportClick = { chatRoom.storeBrief?.storeId?.let(onReportClick) },
                    onExitClick = { isWithdrawDialogVisible = true },
                    onDismissRequest = { isBottomSheetVisible = false },
                )
            }
        }

        else -> {
            /*TODO: 채팅방 정보를 불러오지 못하는 경우에 대한 화면 구현*/
            Timber.tag("ChatRoom").d("none ChatRoomScreen called")
            LaunchedEffect(chatRoomState.isUserExitChatRoom) {
                if (chatRoomState.isUserExitChatRoom) {
                    onNavigateUp()
                }
            }
        }
    }
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

@ScreenPreview
@Composable
private fun ChatRoomScreenPreview() {
    NapzakMarketTheme {
        ChatRoomScreen(
            chat = { "" },
            chatItems = mockChats.toImmutableList(),
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
            chat = { "" },
            chatItems = emptyList<ReceiveMessage<*>>().toImmutableList(),
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
