package com.napzak.market.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_profile_60
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.mypage_buy_count
import com.napzak.market.feature.mypage.R.string.mypage_buy_label
import com.napzak.market.feature.mypage.R.string.mypage_profile_image_description
import com.napzak.market.feature.mypage.R.string.mypage_sell_count
import com.napzak.market.feature.mypage.R.string.mypage_sell_label

@Composable
fun MyPageProfileSection(
    nickname: String,
    profileImageUrl: String,
    salesCount: Int,
    purchaseCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(NapzakMarketTheme.colors.gray10)
            .padding(horizontal = 22.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profileImageUrl.takeIf { it.isNotBlank() })
                .placeholder(ic_profile_60)
                .error(ic_profile_60)
                .build(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(NapzakMarketTheme.colors.purple100),
            contentDescription = stringResource(mypage_profile_image_description),
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = nickname,
                color = NapzakMarketTheme.colors.purple500,
                style = NapzakMarketTheme.typography.body14b,
            )
            Spacer(modifier = Modifier.height(7.dp))
            Row {
                Text(
                    stringResource(mypage_sell_label),
                    color = NapzakMarketTheme.colors.gray500,
                    style = NapzakMarketTheme.typography.caption12m,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    stringResource(mypage_sell_count, salesCount),
                    color = NapzakMarketTheme.colors.gray500,
                    style = NapzakMarketTheme.typography.caption12sb,
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    stringResource(mypage_buy_label),
                    color = NapzakMarketTheme.colors.gray500,
                    style = NapzakMarketTheme.typography.caption12m,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    stringResource(mypage_buy_count, purchaseCount),
                    color = NapzakMarketTheme.colors.gray500,
                    style = NapzakMarketTheme.typography.caption12sb,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPageProfileSectionPreview() {
    NapzakMarketTheme {
        MyPageProfileSection(
            nickname = "납작이",
            profileImageUrl = "",
            salesCount = 12,
            purchaseCount = 7,
        )
    }
}