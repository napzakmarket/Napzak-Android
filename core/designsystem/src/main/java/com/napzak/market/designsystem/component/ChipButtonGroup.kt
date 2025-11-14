package com.napzak.market.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollFactory
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
import com.napzak.market.designsystem.R.drawable.ic_circle_purple_cancel
import com.napzak.market.designsystem.R.drawable.ic_circle_purple_reset
import com.napzak.market.designsystem.theme.NapzakMarketTheme

private const val MAX_TEXT_LENGTH = 5
private const val CHIP_ANIMATION_DURATION = 200
private const val ANIMATION_LABEL = "chip_group"
private const val ELLIPSIS = "..."

/**
 * 칩 목록을 관리하는 그룹 컴포넌트입니다. 를 클릭하여 삭제하거나 초기화 버튼을 누르는 기능을 제공합니다.
 *
 * @param items 칩 이름 목록
 * @param onResetClick 초기화 버튼 클릭 콜백
 * @param onChipClick  클릭 콜백
 * @param modifier 수정자
 * @param backgroundColor 배경색
 * @param contentPaddingValues 컨텐츠 패딩값
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChipButtonGroup(
    items: List<String>,
    onResetClick: () -> Unit,
    onChipClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = NapzakMarketTheme.colors.white,
    contentPaddingValues: PaddingValues = PaddingValues(),
) {
    CompositionLocalProvider(
        value = LocalOverscrollFactory provides null,
        content = {
            AnimatedContent(
                targetState = items.isNotEmpty(),
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
                                imageVector = ImageVector.vectorResource(ic_circle_purple_reset),
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
                            items = items,
                            key = { _, item -> item },
                        ) { index, item ->
                            val startPadding = if (index == 0) 0.dp else 5.dp

                            RemovableChip(
                                text = item,
                                onClick = { onChipClick(item) },
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
private fun RemovableChip(
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
            imageVector = ImageVector.vectorResource(ic_circle_purple_cancel),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ChipButtonGroupPreview() {
    NapzakMarketTheme {
        val genreNames: MutableList<String> = remember {
            mutableStateListOf("나루토", "원피스", "사카모토데이즈", "진격의 거인", "조조의 기묘한 모험")
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .padding(vertical = 30.dp)
        ) {

            ChipButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                items = genreNames.toList(),
                contentPaddingValues = PaddingValues(end = 20.dp),
                onResetClick = { genreNames.clear() },
                onChipClick = {
                    genreNames.remove(it)
                }
            )
        }
    }
}