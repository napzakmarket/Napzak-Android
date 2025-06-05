package com.napzak.market.onboarding.genre.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.napzak.market.designsystem.R.drawable.ic_heart
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.ellipsis

@Composable
fun GenreItem(
    genreName: String,
    genreImageUrl: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(100.dp))
                .clickable { onClick() }
        ) {
            AsyncImage(
                model = genreImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(NapzakMarketTheme.colors.gray100),
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NapzakMarketTheme.colors.purple500.copy(alpha = 0.4f))
                )

                Icon(
                    imageVector = ImageVector.vectorResource(id = ic_heart),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = genreName.ellipsis(6),
            style = NapzakMarketTheme.typography.caption12r,
            color = if (isSelected) NapzakMarketTheme.colors.purple500 else NapzakMarketTheme.colors.gray300,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}