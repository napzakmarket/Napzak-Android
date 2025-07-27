package com.napzak.market.registration.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_import_24
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.photo_count
import com.napzak.market.registration.model.Photo
import com.napzak.market.ui_util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

private const val MAX_ITEMS = 10
private const val MIN_ITEMS = 2
private const val ZERO = 0

/**
 * Registration photo picker
 *
 * @param imageUris: 현재 이미지 URL 리스트
 * @param onLongClick: 길게 누를 시 대표 이미지 변경을 위한 함수
 * @param onDeleteClick: 클릭 시 이미지 삭제 처리를 위한 함수
 */
@Composable
internal fun PhotoPicker(
    imageUris: PersistentList<Photo>,
    onImagesSelect: (ImmutableList<Photo>) -> Unit,
    onLongClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val remainingImageSize = (MAX_ITEMS - imageUris.size).coerceAtLeast(MIN_ITEMS)

    val photoPickerLauncher = when (imageUris.size) {
        MAX_ITEMS - 1 -> rememberLauncherForActivityResult(
            PickVisualMedia()
        ) { uri ->
            uri?.let { onImagesSelect(listOf(Photo(it)).toPersistentList()) }
        }

        else -> rememberLauncherForActivityResult(PickMultipleVisualMedia(remainingImageSize)) { uris ->
            onImagesSelect(uris.map { Photo(it) }.toPersistentList())
        }
    }

    LazyRow(
        modifier = modifier,
    ) {
        item {
            PhotoRegisterButton(
                modifier = Modifier.padding(start = 20.dp),
                imageCount = imageUris.size,
                onPhotoClick = {
                    val remainImageSize = MAX_ITEMS - imageUris.size

                    when {
                        /* TODO: 최대 개수 초과 시 스낵바 처리 */
                        remainImageSize <= ZERO -> {}

                        else -> photoPickerLauncher.launch(
                            PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.width(14.dp))
        }

        itemsIndexed(
            items = imageUris,
            key = { _, photo -> photo.uuid },
        ) { index, photo ->
            val padEnd = if (index == imageUris.lastIndex) 20.dp else 8.dp

            PhotoContainer(
                index = index,
                imageUri = photo.compressedUri,
                status = photo.status,
                onDeleteClick = onDeleteClick,
                onLongClick = { onLongClick(index) },
                modifier = Modifier
                    .padding(end = padEnd),
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
            .noRippleClickable { if (imageCount < MAX_ITEMS) onPhotoClick() },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
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

@Preview
@Composable
private fun RegistrationPhotoPickerPreview() {
    NapzakMarketTheme {
        Column {
            PhotoPicker(
                imageUris = persistentListOf(),
                onImagesSelect = { },
                onLongClick = { },
                onDeleteClick = { },
            )
        }
    }
}
