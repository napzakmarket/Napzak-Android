package com.napzak.market.designsystem.component.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.napzak.market.designsystem.theme.NapzakMarketTheme

/**
 * 토글 버튼 색상
 *
 * toggleOnColor : 토글 ON
 * toggleOffColor : 토글 OFF (or 비활성화 상태)
 * toggleDisableColor: 토글 ON + 비활성화 상태
 */
@Stable
object NapzakToggleButtonDefault {
    val color: NapzakToggleButtonColor
        @Composable get() = NapzakToggleButtonColor(
            toggleOnColor = NapzakMarketTheme.colors.purple500,
            toggleOffColor = NapzakMarketTheme.colors.gray200,
            toggleDisableColor = NapzakMarketTheme.colors.purple200,
        )
}

@Stable
class NapzakToggleButtonColor(
    val toggleOnColor: Color,
    val toggleOffColor: Color,
    val toggleDisableColor: Color,
) {
    fun copy(
        toggleOnColor: Color = this.toggleOnColor,
        toggleOffColor: Color = this.toggleOffColor,
        toggleDisableColor: Color = this.toggleDisableColor,
    ) = NapzakToggleButtonColor(
        toggleOnColor = toggleOnColor,
        toggleOffColor = toggleOffColor,
        toggleDisableColor = toggleDisableColor,
    )
}
