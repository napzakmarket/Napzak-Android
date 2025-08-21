package com.napzak.market.home.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay

private const val SCROLL_DELAY = 5000L
private const val SCROLL_TWEEN = 1000

/**
 * 가로 스크롤 및 자동 스크롤을 지원하는 이미지 목록 컴포넌트입니다.
 *
 * @param images 이미지 URL 목록입니다. 이미지 URL 문자열만 받아서 처리합니다.
 * @param onImageClick 이미지를 클릭했을 때 호출되는 콜백입니다. 클릭한 이미지의 인덱스를 인자로 전달합니다.
 * @param modifier 컴포넌트의 수정자입니다.
 */
@Composable
internal fun HorizontalAutoScrolledImages(
    images: ImmutableList<String>,
    onImageClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context by rememberUpdatedState(LocalContext.current)
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { Int.MAX_VALUE },
    )

    val currentPage = rememberSaveable(pagerState) {
        pagerState.currentPage % images.size
    }

    LaunchedEffect(Unit) {
        pagerState.scrollToPage(page = 0, pageOffsetFraction = 0f)
        while (true) {
            delay(SCROLL_DELAY)
            pagerState.animateScrollToPage(
                page = pagerState.currentPage + 1,
                animationSpec = tween(SCROLL_TWEEN),
            )
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .background(color = NapzakMarketTheme.colors.gray100)
            .noRippleClickable { onImageClick(currentPage) }
    ) {
        HorizontalPager(
            state = pagerState,
        ) {
            val currentImage = images[currentPage]
            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(currentImage)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        PageIndicator(
            imageCount = images.size,
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
        )
    }
}

@Composable
private fun PageIndicator(
    imageCount: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val selectedColor = NapzakMarketTheme.colors.purple500
    val unselectedColor = NapzakMarketTheme.colors.gray100

    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        repeat(imageCount) { iteration ->
            val color = if (pagerState.currentPage % imageCount == iteration) selectedColor
            else unselectedColor

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HorizontalImagesScrollPreview() {
    NapzakMarketTheme {
        val mockImageUrls = listOf(
            "", "", ""
        )

        HorizontalAutoScrolledImages(
            images = mockImageUrls.toImmutableList(),
            onImageClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(360 / 216f),
        )
    }
}