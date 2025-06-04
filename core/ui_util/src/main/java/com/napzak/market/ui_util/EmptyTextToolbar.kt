package com.napzak.market.ui_util

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus

class EmptyTextToolbar : TextToolbar {
    override val status: TextToolbarStatus
        get() = TextToolbarStatus.Hidden

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?,
    ) {}

    override fun hide() {}
}

val LocalEmptyTextToolbar = staticCompositionLocalOf<EmptyTextToolbar> {
    error("No TextToolbar provided")
}
