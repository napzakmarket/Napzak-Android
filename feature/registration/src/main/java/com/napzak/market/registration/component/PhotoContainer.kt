package com.napzak.market.registration.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_cancel_image_24
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.representative_tag
import com.napzak.market.util.android.noRippleClickable
import com.napzak.market.util.android.noRippleCombineClickable

/**
 * Photo container
 *
 * @param index: 이미지 index
 * @param imageUri: 이미지 uri
 * @param onDeleteClick: 삭제 버튼 클릭 시 등록한 이미지 삭제
 * @param onLongClick: 길게 눌러 대표 이미지 설정
 */
@Composable
internal fun PhotoContainer(
    index: Int,
    imageUri: Uri,
    onDeleteClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp, end = 6.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(NapzakMarketTheme.colors.gray100)
                .width(88.dp)
                .aspectRatio(1f)
                .noRippleCombineClickable { onLongClick(index) },
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(imageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            if (index == 0) RepresentativeImageTag()
        }

        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .noRippleClickable { onDeleteClick(index) },
            imageVector = ImageVector.vectorResource(ic_cancel_image_24),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Composable
private fun RepresentativeImageTag(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(width = 46.dp, height = 24.dp)
            .clip(RoundedCornerShape(topStart = 5.dp, bottomEnd = 5.dp))
            .background(NapzakMarketTheme.colors.transBlack),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(representative_tag),
            style = NapzakMarketTheme.typography.caption10r.copy(
                color = NapzakMarketTheme.colors.white,
            ),
        )
    }
}
