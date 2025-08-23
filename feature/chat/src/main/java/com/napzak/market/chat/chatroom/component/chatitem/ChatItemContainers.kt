package com.napzak.market.chat.chatroom.component.chatitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.image.NapzakProfileImage
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_room_is_not_read

@Composable
internal fun MyChatItemContainer(
    isRead: Boolean,
    timeStamp: String?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        ChatLabels(
            isRead = isRead,
            timeStamp = timeStamp,
            alignment = Alignment.End,
        )
        Spacer(modifier = Modifier.width(4.dp))
        content()
    }
}

@Composable
internal fun OpponentChatItemContainer(
    imageUrl: String?,
    isProfileImageVisible: Boolean,
    isProduct: Boolean,
    isRead: Boolean,
    timeStamp: String?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val contentPaddingValues = PaddingValues(
        top = if (isProfileImageVisible) 20.dp else 0.dp,
        start = 4.dp,
    )

    Row(
        modifier = modifier,
    ) {
        when {
            !isProduct && isProfileImageVisible -> {
                NapzakProfileImage(
                    imageUrl = imageUrl ?: "",
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

            !isProduct && !isProfileImageVisible -> Spacer(modifier = Modifier.size(40.dp))
        }

        Row(
            modifier = Modifier.padding(contentPaddingValues),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            content()
            ChatLabels(
                isRead = isRead || isProduct,
                timeStamp = timeStamp,
                alignment = Alignment.Start,
            )
        }
    }
}

@Composable
private fun ChatLabels(
    isRead: Boolean,
    timeStamp: String?,
    alignment: Alignment.Horizontal,
    modifier: Modifier = Modifier,
) {
    val color = NapzakMarketTheme.colors.gray200
    val textStyle = NapzakMarketTheme.typography.caption10r
    val textContent: @Composable (String) -> Unit = { text ->
        Text(
            text = text,
            style = textStyle,
            color = color,
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = alignment,
    ) {
        if (!isRead) textContent(stringResource(chat_room_is_not_read))
        if (!timeStamp.isNullOrBlank()) textContent(timeStamp)
    }
}