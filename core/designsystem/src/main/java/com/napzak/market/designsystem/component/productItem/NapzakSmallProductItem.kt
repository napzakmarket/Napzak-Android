package com.napzak.market.designsystem.component.productItem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_red_heart
import com.napzak.market.designsystem.R.drawable.ic_transparent_heart
import com.napzak.market.designsystem.R.string.production_item_buy
import com.napzak.market.designsystem.R.string.production_item_buy_price
import com.napzak.market.designsystem.R.string.production_item_price_suggestion
import com.napzak.market.designsystem.R.string.production_item_sell
import com.napzak.market.designsystem.R.string.production_item_sell_price
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.formatToPriceString
import com.napzak.market.ui_util.noRippleClickable

/**
 * 상품 아이템 (Thumbnail-Small), 가로 스크롤 목록에 사용됩니다.
 *
 * @param title 상품 제목 문자열
 * @param genre 장르 문자열
 * @param price 가격 문자열
 * @param imgUrl 이미지 URL
 * @param isLiked 좋아요 여부
 * @param isMyItem 내 상품 여부
 * @param isSellElseBuy 팔아요 여부 (true: 팔아요, false: 구해요)
 * @param isSuggestionAllowed 가격제시 여부
 * @param onLikeClick 좋아요 클릭 이벤트
 */
@Composable
fun NapzakSmallProductItem(
    title: String,
    genre: String,
    price: String,
    imgUrl: String,
    isLiked: Boolean,
    isMyItem: Boolean,
    isSellElseBuy: Boolean,
    isSuggestionAllowed: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.semantics(
            mergeDescendants = true,
            properties = {
                contentDescription = title
            },
        ),
    ) {
        ItemImageGroup(
            imgUrl = imgUrl,
            isLiked = isLiked,
            isMyItem = isMyItem,
            isSellElseBuy = isSellElseBuy,
            isSuggestionAllowed = isSuggestionAllowed,
            onLikeClick = onLikeClick,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        )

        Text(
            text = genre,
            style = NapzakMarketTheme.typography.caption12r,
            color = NapzakMarketTheme.colors.gray500,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(top = 5.dp),
        )

        Text(
            text = title,
            style = NapzakMarketTheme.typography.body14sb,
            color = NapzakMarketTheme.colors.gray500,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(top = 2.dp),
        )

        PriceText(
            price = price,
            isSellElseBuy = isSellElseBuy,
        )
    }
}

@Composable
private fun ItemImageGroup(
    imgUrl: String,
    isLiked: Boolean,
    isMyItem: Boolean,
    isSellElseBuy: Boolean,
    isSuggestionAllowed: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholderColor: Color = NapzakMarketTheme.colors.gray200,
) {
    val context = LocalContext.current
    val imageShape = RoundedCornerShape(3.dp)

    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data(imgUrl)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .clip(imageShape)
                .background(color = placeholderColor),
        )

        TypeTag(
            isSuggestionAllowed = isSuggestionAllowed,
            isSellElseBuy = isSellElseBuy,
            modifier = Modifier
                .align(Alignment.BottomStart),
        )


        if (!isMyItem) {
            LikeButton(
                isLiked = isLiked,
                onLikeClick = onLikeClick,
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.BottomEnd),
            )
        }
    }
}

@Composable
private fun TypeTag(
    isSellElseBuy: Boolean,
    isSuggestionAllowed: Boolean,
    modifier: Modifier = Modifier,
) {
    val textRes = if (isSellElseBuy) production_item_sell else production_item_buy
    val contentColor = NapzakMarketTheme.colors.white
    val containerColor = if (isSellElseBuy) NapzakMarketTheme.colors.transP500
    else NapzakMarketTheme.colors.transBlack

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(topEnd = 3.dp, bottomStart = 3.dp),
                )
                .padding(vertical = 4.dp, horizontal = 7.dp),
        ) {
            Text(
                text = stringResource(textRes),
                style = NapzakMarketTheme.typography.caption10sb,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier,
            )
        }

        if (isSuggestionAllowed) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = NapzakMarketTheme.colors.transP100,
                        shape = RoundedCornerShape(topEnd = 3.dp, topStart = 3.dp),
                    )
                    .padding(vertical = 4.dp, horizontal = 7.dp),
            ) {
                Text(
                    text = stringResource(production_item_price_suggestion),
                    style = NapzakMarketTheme.typography.caption10sb,
                    color = NapzakMarketTheme.colors.purple500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
private fun LikeButton(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val imageVector = if (isLiked) ic_red_heart
    else ic_transparent_heart

    Icon(
        imageVector = ImageVector.vectorResource(imageVector),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier
            .clearAndSetSemantics { role = Role.Button }
            .size(14.dp)
            .noRippleClickable(onClick = onLikeClick),
    )
}

@Composable
private fun PriceText(
    price: String,
    isSellElseBuy: Boolean,
    modifier: Modifier = Modifier,
) {
    val priceText = price.formatToPriceString()
    val textRes =
        if (isSellElseBuy) production_item_sell_price
        else production_item_buy_price

    Text(
        text = stringResource(textRes, priceText),
        style = NapzakMarketTheme.typography.body16b,
        color = NapzakMarketTheme.colors.gray500,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = modifier.padding(top = 2.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun SmallProductItemPreview() {
    NapzakMarketTheme {
        var isLiked by remember { mutableStateOf(false) }

        NapzakSmallProductItem(
            genre = "은혼",
            title = "은혼 긴토키 히지카타 룩업",
            imgUrl = "",
            price = "125,000",
            isLiked = isLiked,
            isSellElseBuy = false,
            isSuggestionAllowed = true,
            isMyItem = false,
            onLikeClick = { isLiked = !isLiked },
            modifier = Modifier.size(
                width = 116.dp,
                height = 182.dp,
            ),
        )
    }
}