package com.napzak.market.chat.chatroom.component.chatitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_room_product_button
import com.napzak.market.feature.chat.R.string.chat_room_product_title_buy
import com.napzak.market.feature.chat.R.string.chat_room_product_title_sell
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun ChatProduct(
    tradeType: String,
    genre: String,
    name: String,
    price: String,
    isMessageOwner: Boolean,
    onNavigateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = NapzakMarketTheme.colors.gray50
    val shape = if (isMessageOwner) {
        RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
            bottomStart = 12.dp,
        )
    } else {
        RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
            bottomEnd = 12.dp,
        )
    }

    BoxWithConstraints(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .width(maxWidth * 0.64f)
                .clip(shape)
                .background(color = containerColor, shape = shape),
        ) {
            ChatProductHeader(
                tradeType = tradeType,
            )
            Spacer(
                modifier = Modifier.height(16.dp),
            )
            ChatProductDetailView(
                genre = genre,
                name = name,
                price = price,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            Spacer(
                modifier = Modifier.height(15.dp),
            )
            ChatProductButton(
                onClick = onNavigateClick,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            Spacer(
                modifier = Modifier.height(12.dp),
            )
        }
    }
}

@Composable
private fun ChatProductHeader(
    tradeType: String,
    modifier: Modifier = Modifier,
) {
    val stringRes = when (TradeType.fromName(tradeType)) {
        TradeType.SELL -> chat_room_product_title_sell
        TradeType.BUY -> chat_room_product_title_buy
    }
    val textStyle = NapzakMarketTheme.typography.caption12sb
    val contentColor = NapzakMarketTheme.colors.white
    val containerColor = NapzakMarketTheme.colors.purple500

    Text(
        text = stringResource(stringRes),
        style = textStyle,
        color = contentColor,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(color = containerColor)
            .padding(vertical = 11.dp),
    )
}

@Composable
private fun ChatProductDetailView(
    genre: String,
    name: String,
    price: String,
    modifier: Modifier = Modifier,
) {
    val textColor = NapzakMarketTheme.colors.black
    val commonText: @Composable (String, TextStyle) -> Unit = { text, style ->
        Text(
            text = text,
            style = style,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.semantics {
                contentDescription = text
            }
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        commonText(genre, NapzakMarketTheme.typography.caption10sb)
        commonText(name, NapzakMarketTheme.typography.caption12sb)
        commonText(price, NapzakMarketTheme.typography.body14b)
    }
}

@Composable
private fun ChatProductButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonColor = NapzakMarketTheme.colors.black
    val buttonShape = RoundedCornerShape(8.dp)
    val textStyle = NapzakMarketTheme.typography.caption10sb
    val textColor = NapzakMarketTheme.colors.white

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = buttonColor,
                shape = buttonShape,
            )
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(chat_room_product_button),
            style = textStyle,
            color = textColor,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatProductPreview() {
    NapzakMarketTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            ChatProduct(
                tradeType = "SELL",
                genre = "식품",
                name = "김치",
                price = "1000원",
                onNavigateClick = {},
                isMessageOwner = true
            )
        }
    }
}
