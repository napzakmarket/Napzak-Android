package com.napzak.market.ui_util

import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource


fun clearFocusOnScrollConnection(focusManager: FocusManager) =
    object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            if (available.y != 0f && source == NestedScrollSource.Fling) {
                focusManager.clearFocus()
            }
            return Offset.Zero
        }
    }
