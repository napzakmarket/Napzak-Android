package com.napzak.market.registration.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.napzak.market.designsystem.R.drawable.ic_cancel_image_24
import com.napzak.market.designsystem.R.drawable.ic_import_24
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.photo_count
import com.napzak.market.feature.registration.R.string.representative_tag
import com.napzak.market.util.android.noRippleClickable
import com.napzak.market.util.android.noRippleCombineClickable

private const val MAX_PHOTOS = 10

/**
 * Registration photo picker
 *
 * @param imageUrlList: 현재 이미지 URL 리스트
 * @param onPhotoClick: 클릭 시 이미지 추가 함수 호출
 * @param onLongClick: 길게 누를 시 대표 이미지 변경을 위한 함수
 * @param onDeleteClick: 클릭 시 이미지 삭제 처리를 위한 함수
 */
@Composable
fun PhotoPicker(
    imageUrlList: List<String>,
    onPhotoClick: () -> Unit,
    onLongClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier, // TODO: Overscroll 넣을래말래넣을래말래넣을래말래넣을래말래넣을래말래넣을래말래넣을래말래넣을래말래
    ) {
        item {
            PhotoRegisterButton(
                modifier = Modifier.padding(start = 20.dp),
                imageCount = imageUrlList.size,
                onPhotoClick = onPhotoClick,
            )
            Spacer(modifier = Modifier.width(14.dp))
        }
        itemsIndexed(
            items = imageUrlList,
            key = { index, _ -> index },
        ) { index, url ->
            PhotoContainer(
                index = index,
                imageUrl = url,
                onDeleteClick = onDeleteClick,
                onLongClick = { onLongClick(index) },
                modifier = Modifier
                    .padding(
                        end = if (index == imageUrlList.lastIndex) 20.dp else 8.dp),
            )
        }
    }
}

/**
 * Photo register button
 *
 * @param imageCount: 현재 등록된 이미지 수
 * @param onPhotoClick: 클릭 시 이미지 photoPicker 호출
 */
@Composable
private fun PhotoRegisterButton(
    imageCount: Int,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(top = 8.dp)
            .width(88.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(5.dp))
            .background(NapzakMarketTheme.colors.purple100)
            .padding(top = 10.dp)
            .noRippleClickable { if (imageCount < MAX_PHOTOS) onPhotoClick() },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_import_24),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Text(
                text = stringResource(photo_count, imageCount),
                style = NapzakMarketTheme.typography.caption10r.copy(
                    color = NapzakMarketTheme.colors.purple500,
                ),
            )
        }
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

/**
 * Photo container
 *
 * @param index: 이미지 index
 * @param imageUrl: 이미지 url
 * @param onDeleteClick: 삭제 버튼 클릭 시 등록한 이미지 삭제
 * @param onLongClick: 길게 눌러 대표 이미지 설정
 */
@Composable
private fun PhotoContainer(
    index: Int,
    imageUrl: String,
    onDeleteClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                model = imageUrl,
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

@Preview
@Composable
private fun RegistrationPhotoPickerPreview() {
    NapzakMarketTheme {
        Column {
            PhotoPicker(
                imageUrlList = listOf("1", "2", "3", "4", "1", "1", "4"),
                onPhotoClick = { },
                onLongClick = { },
                onDeleteClick = { },
                modifier = Modifier
            )
        }
    }
}
