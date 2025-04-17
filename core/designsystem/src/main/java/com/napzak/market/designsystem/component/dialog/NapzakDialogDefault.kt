package com.napzak.market.designsystem.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Stable
object NapzakDialogDefault {
    val color: NapzakDialogColor
        @Composable get() = NapzakDialogColor(
            titleColor = NapzakMarketTheme.colors.purple500,
            subTitleColor = NapzakMarketTheme.colors.gray300,
            confirmColor = NapzakMarketTheme.colors.gray300,
            dismissColor = NapzakMarketTheme.colors.gray300,
            containerColor = NapzakMarketTheme.colors.white,
            lineColor = NapzakMarketTheme.colors.gray200,
        )
}

@Stable
class NapzakDialogColor(
    val titleColor: Color,
    val subTitleColor: Color,
    val confirmColor: Color,
    val dismissColor: Color,
    val containerColor: Color,
    val lineColor: Color,
) {
    fun copy(
        titleColor: Color = this.titleColor,
        subTitleColor: Color = this.subTitleColor,
        confirmColor: Color = this.confirmColor,
        dismissColor: Color = this.dismissColor,
        containerColor: Color = this.containerColor,
        lineColor: Color = this.lineColor,
    ) = NapzakDialogColor(
        titleColor = titleColor,
        subTitleColor = subTitleColor,
        confirmColor = confirmColor,
        dismissColor = dismissColor,
        containerColor = containerColor,
        lineColor = lineColor,
    )
}
