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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_room_product_negotiable
import com.napzak.market.feature.chat.R.string.chat_room_product_price_won_format
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.formatToPriceString
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
            ProductTagRow(
                tradeType = product.tradeType,
                isPriceNegotiable = product.isPriceNegotiable,
            )
            Spacer(modifier = Modifier.height(5.dp))
            ProductText(
                text = product.title,
                style = NapzakMarketTheme.typography.body14sb,
            )
            ProductText(
                text = stringResource(
                    chat_room_product_price_won_format,
                    product.price.toString().formatToPriceString(),
                ),
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
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(imageSize)
            .clip(imageShape),
    )
}

@Composable
private fun ProductTagRow(
    tradeType: String,
    isPriceNegotiable: Boolean,
    modifier: Modifier = Modifier,
) {
    Row {
        Tag(
            text = TradeType.fromName(tradeType).label,
            textStyle = NapzakMarketTheme.typography.caption12sb,
            contentColor = NapzakMarketTheme.colors.white,
            containerColor = when (TradeType.fromName(tradeType)) {
                TradeType.BUY -> NapzakMarketTheme.colors.transBlack
                TradeType.SELL -> NapzakMarketTheme.colors.transP500
            },
            shape = RoundedCornerShape(4.dp),
            modifier = modifier,
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (isPriceNegotiable) {
            Tag(
                text = stringResource(chat_room_product_negotiable),
                textStyle = NapzakMarketTheme.typography.caption12sb,
                contentColor = NapzakMarketTheme.colors.purple500,
                containerColor = NapzakMarketTheme.colors.transP100,
                shape = RoundedCornerShape(4.dp),
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun Tag(
    text: String,
    textStyle: TextStyle,
    contentColor: Color,
    containerColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    val innerPadding = PaddingValues(vertical = 2.dp, horizontal = 4.dp)
    Box(
        modifier = modifier.background(
            shape = shape,
            color = containerColor,
        ),
    ) {
        Text(
            text = text,
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
private fun ChatRoomProductSellPreview() {
    NapzakMarketTheme {
        ChatRoomProductSection(
            product = ProductBrief(
                productId = 1,
                genreName = "은혼",
                title = "은혼 긴토키 히지카타 룩업",
                photo = "",
                price = 129000,
                tradeType = "SELL",
                isPriceNegotiable = false,
                isMyProduct = false,
                productOwnerId = 1
            ),
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomProductBuyPreview() {
    NapzakMarketTheme {
        ChatRoomProductSection(
            product = ProductBrief(
                productId = 1,
                genreName = "은혼",
                title = "은혼 긴토키 히지카타 룩업",
                photo = "",
                price = 129000,
                tradeType = "BUY",
                isPriceNegotiable = true,
                isMyProduct = false,
                productOwnerId = 1
            ),
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
