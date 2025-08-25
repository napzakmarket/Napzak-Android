package com.napzak.market.store.edit_store.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_image_edit
import com.napzak.market.designsystem.component.image.NapzakProfileImage
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.presigned_url.type.PhotoType
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun EditStorePhotoSection(
    storeCover: String,
    storePhoto: String,
    onPhotoChange: (PhotoType, Uri?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val photoType = remember { mutableStateOf(PhotoType.COVER) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> onPhotoChange(photoType.value, uri) }

    val onPhotoClick: (PhotoType) -> Unit = { editedPhotoType ->
        photoType.value = editedPhotoType
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(NapzakMarketTheme.colors.white),
        contentAlignment = Alignment.TopCenter
    ) {
        CoverImageSection(
            imageUrl = storeCover,
            modifier = Modifier
                .fillMaxWidth()
                .height(134.dp)
        ) {
            onPhotoClick(PhotoType.COVER)
        }

        ProfileImageSection(
            imageUrl = storePhoto,
        ) {
            onPhotoClick(PhotoType.PROFILE)
        }
    }
}

@Composable
private fun CoverImageSection(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest
            .Builder(context)
            .data(imageUrl)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .noRippleClickable(onClick = onClick),
    )
}

@Composable
private fun ProfileImageSection(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(top = 48.dp)
            .noRippleClickable(onClick = onClick),
    ) {
        NapzakProfileImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .border(
                    width = 4.dp,
                    color = NapzakMarketTheme.colors.white,
                    shape = CircleShape
                )
                .size(110.dp)
        )

        Icon(
            imageVector = ImageVector.vectorResource(ic_image_edit),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditStorePhotoSectionPreview() {
    NapzakMarketTheme {
        EditStorePhotoSection(
            storeCover = "",
            storePhoto = "",
            onPhotoChange = { _, _ -> },
        )
    }
}
