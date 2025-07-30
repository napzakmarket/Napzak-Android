package com.napzak.market.ui_util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


/**
 * Non clickable sticky header
 *
 * stickyHeader 뒤 content의 클릭 방지를 위한 확장 함수
 */
@OptIn(ExperimentalFoundationApi::class)
inline fun LazyListScope.nonClickableStickyHeader(
    modifier: Modifier = Modifier,
    crossinline content: @Composable () -> Unit,
) = stickyHeader {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { },
    ) {
        content()
    }
}
