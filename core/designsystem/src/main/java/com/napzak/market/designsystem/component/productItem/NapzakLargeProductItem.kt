package com.napzak.market.designsystem.component.productItem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.theme.NapzakMarketTheme

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
    isSuggestionAllowed: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        NapzakProductItem(
            title = title,
            genre = genre,
            price = price,
            imgUrl = imgUrl,
            isLiked = isLiked,
            isMyItem = isMyItem,
            isSellElseBuy = isSellElseBuy,
            isSuggestionAllowed = isSuggestionAllowed,
            onLikeClick = onLikeClick,
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
                imageVector = ImageVector.vectorResource(R.drawable.ic_review_11),
                text = reviewCount,
            )

            IconText(
                imageVector = ImageVector.vectorResource(R.drawable.ic_heart_11),
                text = likeCount,
                modifier = Modifier.padding(start = 2.dp),
            )
        }
    }
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
            modifier = Modifier.padding(start = 2.dp),
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
                isSellElseBuy = true,
                isSuggestionAllowed = false,
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
                onLikeClick = { isLiked2 = !isLiked2 },
                modifier = Modifier.weight(1f),
            )
        }
    }
}