package com.napzak.market.designsystem.component.productItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
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
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.R.drawable.ic_red_heart_large
import com.napzak.market.designsystem.R.drawable.ic_transparent_heart_large
import com.napzak.market.designsystem.R.drawable.img_thumbnail_complete_buy
import com.napzak.market.designsystem.R.drawable.img_thumbnail_complete_sell
import com.napzak.market.designsystem.R.drawable.img_thumbnail_reservation
import com.napzak.market.designsystem.R.string.production_item_buy
import com.napzak.market.designsystem.R.string.production_item_price
import com.napzak.market.designsystem.R.string.production_item_price_suggestion
import com.napzak.market.designsystem.R.string.production_item_sell
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.formatToPriceString
import com.napzak.market.ui_util.noRippleClickable

/**
 * 상품 아이템 (Thumbnail-Big)
 *
 * @param title 상품 제목 문자열
 * @param genre 장르 문자열
 * @param price 가격 문자열
 * @param imgUrl 이미지 URL
 * @param createdDate 생성일 문자열
 * @param reviewCount 리뷰 수 문자열
 * @param likeCount 좋아요 수 문자열
 * @param isLiked 좋아요 여부
 * @param isMyItem 내 상품 여부
 * @param isSellElseBuy 팔아요 여부 (true: 팔아요, false: 구해요)
 * @param isSuggestionAllowed 가격제시 여부
 * @param tradeStatus 거래 상태 (판매중, 판매/구매 완료, 예약중)
 * @param onLikeClick 좋아요 클릭 이벤트
 */

@Composable
fun NapzakLargeProductItem(
    title: String,
    genre: String,
    price: String,
    imgUrl: String,
    createdDate: String,
    reviewCount: String,
    likeCount: String,
    isLiked: Boolean,
    isMyItem: Boolean,
    isSellElseBuy: Boolean,
    tradeStatus: TradeStatusType,
    isSuggestionAllowed: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.semantics(
            mergeDescendants = true,
            properties = {
                contentDescription = title
            }
        ),
    ) {
        ItemImageGroup(
            imgUrl = imgUrl,
            isLiked = isLiked,
            isMyItem = isMyItem,
            isSellElseBuy = isSellElseBuy,
            isSuggestionAllowed = isSuggestionAllowed,
            tradeStatus = tradeStatus,
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
            modifier = Modifier.padding(top = 6.dp),
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
            text = stringResource(production_item_price, price.formatToPriceString()),
            style = NapzakMarketTheme.typography.body16b,
            color = NapzakMarketTheme.colors.gray500,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(top = 6.dp),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 13.dp),
        ) {
            Text(
                text = createdDate,
                style = NapzakMarketTheme.typography.caption10r,
                color = NapzakMarketTheme.colors.gray200,
                overflow = TextOverflow.Clip,
                maxLines = 1,
            )

            Spacer(modifier = Modifier.weight(1f))

            IconText(
                imageVector = ImageVector.vectorResource(R.drawable.ic_gray_review),
                text = reviewCount,
            )

            IconText(
                imageVector = ImageVector.vectorResource(R.drawable.ic_gray_heart),
                text = likeCount,
                modifier = Modifier.padding(start = 2.dp),
            )
        }
    }
}

@Composable
private fun ItemImageGroup(
    imgUrl: String,
    isLiked: Boolean,
    isMyItem: Boolean,
    isSellElseBuy: Boolean,
    isSuggestionAllowed: Boolean,
    tradeStatus: TradeStatusType,
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

        TradeStatusImage(
            tradeStatus = tradeStatus,
            shape = imageShape,
            modifier = Modifier
                .matchParentSize()
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
                    .padding(9.dp)
                    .align(Alignment.BottomEnd),
            )
        }
    }
}

@Composable
private fun TradeStatusImage(
    tradeStatus: TradeStatusType,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
) {
    val imageRes = when (tradeStatus) {
        TradeStatusType.RESERVED -> img_thumbnail_reservation
        TradeStatusType.COMPLETED_SELL -> img_thumbnail_complete_sell
        TradeStatusType.COMPLETED_BUY -> img_thumbnail_complete_buy
        else -> null
    }

    if (imageRes != null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(NapzakMarketTheme.colors.transBlack),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = tradeStatus.label,
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
    val imageVector = if (isLiked) ic_red_heart_large
    else ic_transparent_heart_large

    Icon(
        imageVector = ImageVector.vectorResource(imageVector),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier
            .clearAndSetSemantics { role = Role.Button }
            .noRippleClickable(onClick = onLikeClick)
    )
}

@Composable
private fun IconText(
    imageVector: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Text(
            text = text,
            style = NapzakMarketTheme.typography.caption10r,
            color = NapzakMarketTheme.colors.gray200,
            overflow = TextOverflow.Clip,
            maxLines = 1,
            modifier = Modifier.padding(start = 3.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LargeProductItemPreview() {
    NapzakMarketTheme {
        var isLiked1 by remember { mutableStateOf(false) }
        var isLiked2 by remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 27.dp),
            ) {
                NapzakLargeProductItem(
                    genre = "은혼",
                    title = "은혼 긴토키 히지카타 룩업",
                    imgUrl = "",
                    price = "125000",
                    createdDate = "1일 전",
                    reviewCount = "999+",
                    likeCount = "999+",
                    isLiked = isLiked1,
                    isMyItem = false,
                    isSellElseBuy = true,
                    isSuggestionAllowed = false,
                    tradeStatus = TradeStatusType.BEFORE_TRADE_SELL,
                    onLikeClick = { isLiked1 = !isLiked1 },
                    modifier = Modifier.weight(1f),
                )

                NapzakLargeProductItem(
                    genre = "은혼",
                    title = "은혼 긴토키 히지카타 룩업",
                    imgUrl = "",
                    price = "125,000",
                    createdDate = "1일 전",
                    reviewCount = "999+",
                    likeCount = "999+",
                    isLiked = isLiked2,
                    isMyItem = false,
                    isSellElseBuy = false,
                    isSuggestionAllowed = true,
                    tradeStatus = TradeStatusType.COMPLETED_BUY,
                    onLikeClick = { isLiked2 = !isLiked2 },
                    modifier = Modifier.weight(1f),
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 27.dp),
            ) {
                NapzakLargeProductItem(
                    genre = "은혼",
                    title = "은혼 긴토키 히지카타 룩업",
                    imgUrl = "",
                    price = "125,000",
                    createdDate = "1일 전",
                    reviewCount = "999+",
                    likeCount = "999+",
                    isLiked = isLiked1,
                    isMyItem = false,
                    isSellElseBuy = false,
                    isSuggestionAllowed = false,
                    tradeStatus = TradeStatusType.COMPLETED_SELL,
                    onLikeClick = { isLiked1 = !isLiked1 },
                    modifier = Modifier.weight(1f),
                )

                NapzakLargeProductItem(
                    genre = "은혼",
                    title = "은혼 긴토키 히지카타 룩업",
                    imgUrl = "",
                    price = "125,000",
                    createdDate = "1일 전",
                    reviewCount = "999+",
                    likeCount = "999+",
                    isLiked = isLiked2,
                    isMyItem = false,
                    isSellElseBuy = false,
                    isSuggestionAllowed = true,
                    tradeStatus = TradeStatusType.RESERVED,
                    onLikeClick = { isLiked2 = !isLiked2 },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
