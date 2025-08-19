package com.napzak.market.designsystem.component.image

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_profile

/**
 * 납작마켓의 프로필 이미지의 `placeholder`, `error`, `fallback`과 `contentScale`을 일괄적으로 관리하기 위한
 * 이미지 컴포넌트입니다.
 */
@Composable
fun NapzakProfileImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest
            .Builder(context)
            .data(imageUrl)
            .placeholder(ic_profile)
            .error(ic_profile)
            .fallback(ic_profile)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(shape),
    )
}

@Preview
@Composable
private fun NapzakProfileImagePreview() {
    NapzakProfileImage(
        imageUrl = "",
        contentDescription = null,
    )
}