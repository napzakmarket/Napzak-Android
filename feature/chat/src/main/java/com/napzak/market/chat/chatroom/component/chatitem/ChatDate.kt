package com.napzak.market.chat.chatroom.component.chatitem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.img_chat_date_box
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
internal fun ChatDate(
    date: String,
    modifier: Modifier = Modifier,
) {
    val color = NapzakMarketTheme.colors.gray200
    val innerPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    val textStyle = NapzakMarketTheme.typography.caption12r

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.paint(
            painter = painterResource(img_chat_date_box),
            contentScale = ContentScale.FillBounds
        ),
    ) {
        Text(
            text = date,
            style = textStyle,
            color = color,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatDatePreview() {
    NapzakMarketTheme {
        ChatDate(date = "0000년 00월 00일")
    }
}
