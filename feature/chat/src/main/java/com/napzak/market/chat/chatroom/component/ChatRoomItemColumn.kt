package com.napzak.market.chat.chatroom.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.napzak.market.chat.chatroom.component.chatitem.ChatDate
import com.napzak.market.chat.chatroom.component.chatitem.ChatImageItem
import com.napzak.market.chat.chatroom.component.chatitem.ChatNotice
import com.napzak.market.chat.chatroom.component.chatitem.ChatProduct
import com.napzak.market.chat.chatroom.component.chatitem.ChatText
import com.napzak.market.chat.chatroom.component.chatitem.MyChatItemContainer
import com.napzak.market.chat.chatroom.component.chatitem.OpponentChatItemContainer
import com.napzak.market.chat.model.ReceiveMessage

@Composable
internal fun ChatRoomItemColumn(
    listState: LazyListState,
    chatMessages: LazyPagingItems<ReceiveMessage<*>>,
    opponentImageUrl: String?,
    onItemClick: (ReceiveMessage<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        reverseLayout = true,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        item { Spacer(Modifier.height(30.dp)) }
        items(
            count = chatMessages.itemCount,
            key = chatMessages.itemKey(),
        ) { index ->
            val chatItem: ReceiveMessage<*> = chatMessages[index] ?: return@items
            val previousChatItem =
                if (index < chatMessages.itemCount - 1) chatMessages[index + 1]
                else null
            val nextChatItem =
                if (index > 0) chatMessages[index - 1]
                else null
            val isProfileVisible = isProfileVisible(chatItem, previousChatItem)
            val timeStamp = getTimeStamp(chatItem, nextChatItem)
            val contentAlignment = getChatItemAlignment(chatItem)
            val spaceHeight = getChatItemSpace(chatItem, previousChatItem)

            ChatItemRenderer(
                opponentImageUrl = opponentImageUrl,
                chatItem = chatItem,
                isProfileVisible = isProfileVisible,
                timeStamp = timeStamp,
                contentAlignment = contentAlignment,
                onItemClick = { onItemClick(chatItem) },
            )

            Spacer(modifier = Modifier.height(spaceHeight))
        }
        item { Spacer(Modifier.height(30.dp)) }
    }
}

@Composable
private fun ChatItemRenderer(
    chatItem: ReceiveMessage<*>,
    opponentImageUrl: String?,
    isProfileVisible: Boolean,
    timeStamp: String?,
    contentAlignment: Alignment,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = contentAlignment,
    ) {
        when {
            chatItem.isNeutralMessage -> ChatItemView(chatItem = chatItem)

            chatItem.isMessageOwner -> {
                MyChatItemContainer(
                    timeStamp = timeStamp,
                    isRead = chatItem.isRead || (chatItem is ReceiveMessage.Product),
                    content = {
                        ChatItemView(
                            chatItem = chatItem,
                            onItemClick = onItemClick
                        )
                    },
                )
            }

            !chatItem.isMessageOwner -> {
                OpponentChatItemContainer(
                    imageUrl = opponentImageUrl,
                    isProfileImageVisible = isProfileVisible,
                    isProduct = chatItem is ReceiveMessage.Product,
                    timeStamp = timeStamp,
                    isRead = chatItem.isRead,
                    content = {
                        ChatItemView(
                            chatItem = chatItem,
                            onItemClick = onItemClick,
                        )
                    },
                )
            }

            else -> {}
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
                    product = product,
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

        else -> {}
    }
}

private fun isProfileVisible(
    chatItem: ReceiveMessage<*>,
    previousChatItem: ReceiveMessage<*>?,
): Boolean {
    val isPreviousItemProduct = previousChatItem is ReceiveMessage.Product
    val isChatDirectionEqualsPrevious =
        chatItem.isMessageOwner == previousChatItem?.isMessageOwner
    val isPreviousItemNeutral = previousChatItem?.isNeutralMessage == true

    return chatItem.isProfileNeeded
            || isPreviousItemProduct
            || isPreviousItemNeutral
            || (!isChatDirectionEqualsPrevious && chatItem.isMessage)
}

private fun getChatItemAlignment(
    chatItem: ReceiveMessage<*>,
): Alignment {
    return with(chatItem) {
        val isProduct = this is ReceiveMessage.Product
        when {
            isNeutralMessage -> Alignment.Center
            (isProduct || isMessage) && isMessageOwner -> Alignment.TopEnd
            (isProduct || isMessage) && !isMessageOwner -> Alignment.TopStart
            else -> Alignment.Center
        }
    }
}

private fun getTimeStamp(
    chatItem: ReceiveMessage<*>,
    nextChatItem: ReceiveMessage<*>?,
): String? {
    val isChatDirectionEqualsNext = chatItem.isMessageOwner == nextChatItem?.isMessageOwner
    val isTimeStampEqualsNext = chatItem.timeStamp == nextChatItem?.timeStamp
    return chatItem.timeStamp.takeIf {
        !isTimeStampEqualsNext || !isChatDirectionEqualsNext
    }
}

private fun getChatItemSpace(
    chatItem: ReceiveMessage<*>,
    previousChatItem: ReceiveMessage<*>?,
): Dp {
    return when {
        chatItem is ReceiveMessage.Date || previousChatItem is ReceiveMessage.Date -> 20.dp
        chatItem is ReceiveMessage.Notice || previousChatItem is ReceiveMessage.Notice -> 30.dp
        else -> 8.dp
    }
}