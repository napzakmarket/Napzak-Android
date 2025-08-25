package com.napzak.market.designsystem.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme

/**
 * 납작 Toggle Button
 *
 * @param isToggleOn 토글의 On, OFF 상태
 * @param onToggleClick 토글이 클릭 될 때 호출되는 콜백함수
 * @param modifier 수정자
 * @param isClickable 토글 활성화 상태 여부
 */
@Composable
fun NapzakToggleButton(
    isToggleOn: Boolean,
    onToggleClick: () -> Unit,
    modifier: Modifier = Modifier,
    isClickable: Boolean = true,
    toggleButtonColor: NapzakToggleButtonColor = NapzakToggleButtonDefault.color,
) {
    val toggleWidth = 48.dp
    val toggleHeight = 28.dp
    val padding = 2.dp
    val thumbSize = toggleHeight - padding * 2

    val offsetX by animateDpAsState(
        targetValue = if (isToggleOn) toggleWidth - thumbSize - padding * 2 else 0.dp,
        label = "thumb_offset",
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isToggleOn && isClickable -> toggleButtonColor.toggleOnColor
            isToggleOn && !isClickable -> toggleButtonColor.toggleDisableColor
            else -> toggleButtonColor.toggleOffColor
        },
        label = "bg_color"
    )

    Box(
        modifier = modifier
            .size(toggleWidth, toggleHeight)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .toggleable(
                value = isToggleOn,
                interactionSource = null,
                indication = null,
                onValueChange = {
                    if (isClickable) onToggleClick()
                },
            )
            .padding(padding),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = offsetX)
                .background(NapzakMarketTheme.colors.white, RoundedCornerShape(50)),
        )
    }
}

@Preview
@Composable
private fun NapzakToggleButtonPreview(modifier: Modifier = Modifier) {
    NapzakToggleButton(
        isToggleOn = true,
        onToggleClick = {},
        modifier = modifier,
    )
}
