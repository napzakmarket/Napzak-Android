package com.napzak.market.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_cancel_genre_10
import com.napzak.market.designsystem.R.drawable.ic_reset_genre_22
import com.napzak.market.designsystem.theme.NapzakMarketTheme

private const val MAX_TEXT_LENGTH = 5
private const val CHIP_ANIMATION_DURATION = 200
private const val ANIMATION_LABEL = "genre_chip_group"
private const val ELLIPSIS = "..."

/**
 * 장르칩 목록을 관리하는 그룹 컴포넌트입니다. 장르를 클릭하여 삭제하거나 초기화 버튼을 누르는 기능을 제공합니다.
 *
 * @param genreNames 장르 이름 목록
 * @param onResetClick 초기화 버튼 클릭 콜백
 * @param onGenreClick 장르 클릭 콜백
 * @param modifier 수정자
 * @param backgroundColor 배경색
 * @param contentPaddingValues 컨텐츠 패딩값
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenreChipButtonGroup(
    genreNames: List<String>,
    onResetClick: () -> Unit,
    onGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = NapzakMarketTheme.colors.white,
    contentPaddingValues: PaddingValues = PaddingValues(),
) {
    CompositionLocalProvider(
        value = LocalOverscrollConfiguration provides null,
        content = {
            AnimatedContent(
                targetState = genreNames.isNotEmpty(),
                label = ANIMATION_LABEL,
                transitionSpec = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    ) togetherWith slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    )
                },
            ) { hasGenre ->
                if (hasGenre) {
                    LazyRow(
                        modifier = modifier
                            .background(backgroundColor)
                            .wrapContentHeight(),
                        contentPadding = contentPaddingValues,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        stickyHeader {
                            Icon(
                                imageVector = ImageVector.vectorResource(ic_reset_genre_22),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .background(color = backgroundColor)
                                    .padding(vertical = 3.dp)
                                    .clip(CircleShape)
                                    .clickable(onClick = onResetClick),
                            )

                            Spacer(modifier = Modifier.width(5.dp))
                        }

                        itemsIndexed(
                            items = genreNames,
                            key = { _, genre -> genre },
                        ) { index, genre ->
                            val startPadding = if (index == 0) 0.dp else 5.dp

                            RemovableGenreChip(
                                text = genre,
                                onClick = { onGenreClick(genre) },
                                modifier = Modifier
                                    .padding(start = startPadding)
                                    .animateItem(
                                        fadeInSpec = tween(CHIP_ANIMATION_DURATION),
                                        fadeOutSpec = tween(CHIP_ANIMATION_DURATION),
                                    ),
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun RemovableGenreChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(50)
    val genreText = remember(text) {
        if (text.length > MAX_TEXT_LENGTH) text.substring(0, MAX_TEXT_LENGTH) + ELLIPSIS
        else text
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .semantics(
                mergeDescendants = true,
                properties = {
                    role = Role.Button
                    contentDescription = text
                },
            )
            .clip(shape)
            .border(
                width = 1.dp,
                color = NapzakMarketTheme.colors.purple500,
                shape = shape,
            )
            .clickable(onClick = onClick)
            .background(color = NapzakMarketTheme.colors.white)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(
            text = genreText,
            style = NapzakMarketTheme.typography.caption12sb,
            color = NapzakMarketTheme.colors.purple500,
        )

        Icon(
            imageVector = ImageVector.vectorResource(ic_cancel_genre_10),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun GenreChipButtonGroupPreview() {
    NapzakMarketTheme {
        val genreNames: MutableList<String> = remember {
            mutableStateListOf("나루토", "원피스", "사카모토데이즈", "진격의 거인", "조조의 기묘한 모험")
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .padding(vertical = 30.dp)
        ) {

            GenreChipButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                genreNames = genreNames.toList(),
                contentPaddingValues = PaddingValues(end = 20.dp),
                onResetClick = { genreNames.clear() },
                onGenreClick = {
                    genreNames.remove(it)
                }
            )
        }
    }
}