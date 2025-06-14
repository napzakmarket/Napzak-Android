package com.napzak.market.chat.chatroom.component.chatitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.chat.chatroom.model.ChatDirection
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
internal fun ChatText(
    text: String,
    chatDirection: ChatDirection,
    modifier: Modifier = Modifier,
) {
    val maxWidthDp = (LocalConfiguration.current.screenWidthDp * (262 / 360f)).dp
    val (textColor, backgroundColor, shape) = getChatTextDesign(chatDirection)
    val innerPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    val textStyle = NapzakMarketTheme.typography.body14r

    Box(
        modifier = modifier
            .widthIn(max = maxWidthDp)
            .clip(shape = shape)
            .background(color = backgroundColor)
            .padding(innerPadding)
    ) {
        Text(
            text = text,
            style = textStyle,
            color = textColor,
        )
    }
}

@Composable
private fun getChatTextDesign(senderType: ChatDirection) = when (senderType) {
    ChatDirection.SENT ->
        Triple(
            NapzakMarketTheme.colors.white,
            NapzakMarketTheme.colors.purple500,
            RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 2.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp,
            )
        )

    ChatDirection.RECEIVED ->
        Triple(
            NapzakMarketTheme.colors.black,
            NapzakMarketTheme.colors.gray50,
            RoundedCornerShape(
                topStart = 2.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp,
            )
        )
}

@Preview
@Composable
private fun ChatTextPreview() {
    NapzakMarketTheme {
        Column {
            ChatText(
                text = "안녕하세요!",
                chatDirection = ChatDirection.SENT,
            )
            Spacer(modifier = Modifier.height(16.dp))
            ChatText(
                text = "네 안녕해요.",
                chatDirection = ChatDirection.RECEIVED,
            )
        }
    }
}
