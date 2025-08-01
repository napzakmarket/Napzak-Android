package com.napzak.market.chat.chatroom.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.napzak.market.chat.chatroom.component.chatitem.ChatDate
import com.napzak.market.chat.chatroom.component.chatitem.ChatImageItem
import com.napzak.market.chat.chatroom.component.chatitem.ChatNotice
import com.napzak.market.chat.chatroom.component.chatitem.ChatProduct
import com.napzak.market.chat.chatroom.component.chatitem.ChatText
import com.napzak.market.chat.chatroom.component.chatitem.MyChatItemContainer
import com.napzak.market.chat.chatroom.component.chatitem.OpponentChatItemContainer
import com.napzak.market.chat.chatroom.preview.mockChats
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ChatRoomItemColumn(
    listState: LazyListState,
    chatItems: ImmutableList<ReceiveMessage<*>>,
    opponentImageUrl: String?,
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

    LazyColumn(
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(vertical = 30.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        itemsIndexed(chatItems.reversed(), key = { _, item -> item.messageId }) { index, chatItem ->
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

@Preview(showBackground = true)
@Composable
private fun ChatRoomItemColumnPreview() {
    NapzakMarketTheme {
        ChatRoomItemColumn(
            listState = remember { LazyListState() },
            chatItems = mockChats.toImmutableList(),
            opponentImageUrl = null,
            onItemClick = {},
        )
    }
}