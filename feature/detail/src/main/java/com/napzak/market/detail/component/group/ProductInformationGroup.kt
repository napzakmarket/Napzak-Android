package com.napzak.market.detail.component.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_heart_11
import com.napzak.market.designsystem.R.drawable.ic_review_11
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.detail.R.string.detail_product_tag_is_price_negotiable

@Composable
internal fun ProductInformationGroup(
    tradeType: TradeType,
    isPriceNegotiable: Boolean,
    commentCount: Int,
    likeCount: Int,
    genre: String,
    title: String,
    price: String,
    updatedDate: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(NapzakMarketTheme.colors.white),
    ) {
        Surface(
            color = NapzakMarketTheme.colors.white,
            shadowElevation = 1.dp,
        ) {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 22.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TradeTypeTagGroup(
                        tradeType = tradeType,
                        isPriceNegotiable = isPriceNegotiable,
                    )
                    CommentLikeGroup(
                        commentCount = commentCount.toString(),
                        likeCount = likeCount.toString(),
                    )
                }

                Spacer(Modifier.height(20.dp))
                Text(
                    text = genre,
                    style = NapzakMarketTheme.typography.body14b.copy(
                        color = NapzakMarketTheme.colors.black,
                    ),
                )

                Spacer(Modifier.height(10.dp))
                Text(
                    text = title,
                    style = NapzakMarketTheme.typography.title18m.copy(
                        color = NapzakMarketTheme.colors.black,
                    ),
                )

                Spacer(Modifier.height(10.dp))
                Text(
                    text = price,
                    style = NapzakMarketTheme.typography.title18b.copy(
                        color = NapzakMarketTheme.colors.black,
                    ),
                )

                Spacer(Modifier.height(20.dp))
                Text(
                    text = updatedDate,
                    style = NapzakMarketTheme.typography.caption12r.copy(
                        color = NapzakMarketTheme.colors.gray300,
                    ),
                )
            }
        }

        Text(
            text = description,
            style = NapzakMarketTheme.typography.caption12r.copy(
                color = NapzakMarketTheme.colors.black,
            ),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        )
    }
}

@Composable
private fun TradeTypeTagGroup(
    tradeType: TradeType,
    isPriceNegotiable: Boolean,
    modifier: Modifier = Modifier,
) {
    val tradeTypeTagContainerColor =
        if (tradeType == TradeType.SELL) NapzakMarketTheme.colors.purple500
        else NapzakMarketTheme.colors.transBlack

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        TradeTypeTag(
            text = tradeType.label,
            contentColor = NapzakMarketTheme.colors.white,
            containerColor = tradeTypeTagContainerColor,
        )

        if (isPriceNegotiable) {
            TradeTypeTag(
                text = stringResource(detail_product_tag_is_price_negotiable),
                contentColor = NapzakMarketTheme.colors.purple500,
                containerColor = NapzakMarketTheme.colors.transP100,
            )
        }
    }
}

@Composable
private fun TradeTypeTag(
    text: String,
    contentColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = NapzakMarketTheme.typography.caption10r.copy(
            color = contentColor,
        ),
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(containerColor)
            .padding(horizontal = 6.dp, vertical = 3.dp),
    )
}

@Composable
private fun CommentLikeGroup(
    commentCount: String,
    likeCount: String,
    modifier: Modifier = Modifier,
    color: Color = NapzakMarketTheme.colors.gray100,
) {
    val views = listOf(
        commentCount to ic_review_11,
        likeCount to ic_heart_11,
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier,
    ) {
        views.forEach { (count, iconRes) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = iconRes),
                    contentDescription = null,
                    tint = color,
                )
                Text(
                    text = count,
                    style = NapzakMarketTheme.typography.caption10r,
                    color = color,
                    modifier = Modifier.padding(start = 3.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailGroupPreview() {
    NapzakMarketTheme {
        ProductInformationGroup(
            tradeType = TradeType.BUY,
            isPriceNegotiable = true,
            commentCount = 999,
            likeCount = 999,
            genre = "은혼",
            title = "은혼 긴토키 히지카타 룩업은혼 긴토키 히지카",
            price = "125,000원",
            updatedDate = "1일전",
            description = "은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키" +
                    " 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 " +
                    "히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업아아아은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 " +
                    "히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타" +
                    " 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업아아아카타",
            modifier = Modifier,
        )
    }

}