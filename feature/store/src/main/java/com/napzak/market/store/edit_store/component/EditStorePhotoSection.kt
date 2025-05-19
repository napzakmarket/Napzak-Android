package com.napzak.market.store.edit_store.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.drawable.ic_profile_basic
import com.napzak.market.feature.store.R.drawable.ic_profile_edit
import com.napzak.market.presigned_url.type.PhotoType
import com.napzak.market.util.android.noRippleClickable

@Composable
internal fun EditStorePhotoSection(
    storeCover: String,
    storePhoto: String,
    onPhotoChange: (PhotoType, Uri?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val storePhotoShape = CircleShape

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
        modifier = modifier,
    ) {
        // Cover Image
        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data(storeCover)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2.25f)
                .background(NapzakMarketTheme.colors.gray200)
                .noRippleClickable { onPhotoClick(PhotoType.COVER) },
        )

        // Profile Image
        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data(storePhoto)
                .placeholder(ic_profile_basic)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .size(110.dp)
                .clip(storePhotoShape)
                .noRippleClickable { onPhotoClick(PhotoType.PROFILE) }
                .border(
                    width = 5.dp,
                    color = NapzakMarketTheme.colors.white,
                    shape = storePhotoShape,
                ),
        )

        Icon(
            imageVector = ImageVector.vectorResource(ic_profile_edit),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 96.dp, bottom = 16.dp),
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
