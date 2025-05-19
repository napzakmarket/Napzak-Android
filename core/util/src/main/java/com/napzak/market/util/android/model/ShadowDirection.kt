package com.napzak.market.util.android.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ShadowDirection(
    val offsetX: Dp,
    val offsetY: Dp
) {
    Top(0.dp, -1.dp),
    Bottom(0.dp, 1.dp),
    Start(-1.dp, 0.dp),
    End(1.dp, 0.dp),
    None(0.dp, 0.dp),
}