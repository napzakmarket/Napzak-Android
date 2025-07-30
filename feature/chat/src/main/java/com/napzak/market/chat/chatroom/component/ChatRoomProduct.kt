package com.napzak.market.chat.chatroom.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.chat.chatroom.preview.mockProductBrief
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun ChatRoomProductSection(
    product: ProductBrief,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val innerPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)

    Row(
        modifier = modifier
            .napzakGradientShadow(
                height = 4.dp,
                startColor = NapzakMarketTheme.colors.shadowBlack,
                endColor = NapzakMarketTheme.colors.transWhite,
                direction = ShadowDirection.Bottom,
            )
            .noRippleClickable(onClick)
            .padding(innerPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProductImage(imageUrl = product.photo)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            ProductTradeTypeChip(tradeType = product.tradeType)
            Spacer(modifier = Modifier.height(5.dp))
            ProductText(
                text = product.title,
                style = NapzakMarketTheme.typography.body14sb,
            )
            ProductText(
                text = product.price.toString(),
                style = NapzakMarketTheme.typography.body16b,
            )
        }
    }
}

@Composable
private fun ProductImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageShape = RoundedCornerShape(8.dp)
    val imageSize = 70.dp

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = modifier
            .size(imageSize)
            .clip(imageShape),
    )
}

@Composable
private fun ProductTradeTypeChip(
    tradeType: String,
    modifier: Modifier = Modifier,
) {
    val chipShape = RoundedCornerShape(4.dp)
    val containerColor = NapzakMarketTheme.colors.transP500
    val contentColor = NapzakMarketTheme.colors.white
    val textStyle = NapzakMarketTheme.typography.caption12sb
    val innerPadding = PaddingValues(vertical = 2.dp, horizontal = 4.dp)

    Box(
        modifier = modifier.background(
            shape = chipShape,
            color = containerColor,
        ),
    ) {
        Text(
            text = TradeType.fromName(tradeType).label,
            style = textStyle,
            color = contentColor,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun ProductText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = style,
        color = NapzakMarketTheme.colors.black,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomProductPreview() {
    NapzakMarketTheme {
        ChatRoomProductSection(
            product = mockProductBrief,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
