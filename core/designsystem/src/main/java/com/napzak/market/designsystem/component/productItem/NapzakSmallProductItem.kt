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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.napzak.market.designsystem.R.drawable.ic_heart_filled_14
import com.napzak.market.designsystem.R.drawable.ic_heart_unfilled_16
import com.napzak.market.designsystem.R.string.production_item_buy
import com.napzak.market.designsystem.R.string.production_item_price
import com.napzak.market.designsystem.R.string.production_item_price_suggestion
import com.napzak.market.designsystem.R.string.production_item_sell
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.throttledNoRippleClickable

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

        Text(
            text = stringResource(production_item_price, price),
            style = NapzakMarketTheme.typography.body16b,
            color = NapzakMarketTheme.colors.gray500,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(top = 2.dp),
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
                .background(
                    color = placeholderColor,
                    shape = RoundedCornerShape(3.dp),
                ),
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
                .size(width = 40.dp, height = 20.dp)
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(topEnd = 3.dp, bottomStart = 3.dp),
                ),
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
                    .size(width = 50.dp, height = 20.dp)
                    .background(
                        color = NapzakMarketTheme.colors.transP100,
                        shape = RoundedCornerShape(topEnd = 3.dp, topStart = 3.dp),
                    ),
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
    val coroutineScope = rememberCoroutineScope()

    val imageVector = if (isLiked) ic_heart_filled_14
    else ic_heart_unfilled_16

    Icon(
        imageVector = ImageVector.vectorResource(imageVector),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier
            .clearAndSetSemantics { role = Role.Button }
            .throttledNoRippleClickable(
                coroutineScope = coroutineScope,
                onClick = onLikeClick,
            ),
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