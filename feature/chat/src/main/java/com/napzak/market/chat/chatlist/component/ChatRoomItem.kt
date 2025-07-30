package com.napzak.market.chat.chatlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_profile_60
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun ChatRoomItem(
    nickname: String,
    lastMessage: String,
    profileImageUrl: String,
    unReadMessageCount: Int,
    timeStamp: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val innerPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick)
            .padding(innerPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileImage(
            imageUrl = profileImageUrl,
        )
        Spacer(modifier = Modifier.width(12.dp))
        ChatRoomDetail(
            nickname = nickname,
            lastMessage = lastMessage,
            modifier = Modifier.weight(1f),
        )
        ChatRoomMetaData(
            unReadMessageCount = unReadMessageCount,
            timeStamp = timeStamp,
        )
    }
}

@Composable
private fun ProfileImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageShape = CircleShape
    val imageSize = 52.dp

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .placeholder(ic_profile_60)
            .error(ic_profile_60)
            .fallback(ic_profile_60)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(imageSize)
            .clip(imageShape),
    )
}

@Composable
private fun ChatRoomDetail(
    nickname: String,
    lastMessage: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = nickname,
            style = NapzakMarketTheme.typography.body16b,
            color = NapzakMarketTheme.colors.purple500,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = lastMessage,
            style = NapzakMarketTheme.typography.body14r,
            color = NapzakMarketTheme.colors.gray300,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ChatRoomMetaData(
    unReadMessageCount: Int,
    timeStamp: String,
    modifier: Modifier = Modifier,
) {
    val unReadMessageCountShape = RoundedCornerShape(16.dp)

    val (unReadMessageCountText, unReadMessageCountColor) = when {
        unReadMessageCount > 99 -> "99+" to NapzakMarketTheme.colors.red
        unReadMessageCount > 0 -> unReadMessageCount.toString() to NapzakMarketTheme.colors.red
        else -> "" to NapzakMarketTheme.colors.white
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        Box(
            modifier = Modifier
                .clip(unReadMessageCountShape)
                .background(
                    color = unReadMessageCountColor,
                )
                .padding(
                    horizontal = 6.dp,
                    vertical = 3.dp,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = unReadMessageCountText,
                style = NapzakMarketTheme.typography.caption12sb,
                color = NapzakMarketTheme.colors.white,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = timeStamp,
            style = NapzakMarketTheme.typography.caption12r,
            color = NapzakMarketTheme.colors.gray200,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomItemPreview() {
    NapzakMarketTheme {
        Column {
            ChatRoomItem(
                nickname = "정재현1",
                lastMessage = "구매?",
                profileImageUrl = "",
                unReadMessageCount = 11,
                timeStamp = "오전 11:01",
                onClick = {},
            )
            HorizontalDivider(thickness = 1.dp, color = NapzakMarketTheme.colors.gray50)
            ChatRoomItem(
                nickname = "토도로키",
                lastMessage = "사용자가 채팅방을 나갔습니다.",
                profileImageUrl = "",
                unReadMessageCount = 1000,
                timeStamp = "오전 1:38",
                onClick = {},
            )
        }
    }
}
