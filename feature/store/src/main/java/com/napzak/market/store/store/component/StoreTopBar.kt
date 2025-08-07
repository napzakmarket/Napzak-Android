package com.napzak.market.store.store.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.napzak.market.designsystem.component.topbar.MenuTopBar
import com.napzak.market.designsystem.component.topbar.NavigateUpTopBar

@Composable
internal fun StoreTopBar(
    isOwner: Boolean,
    onBackButtonClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isOwner) {
        NavigateUpTopBar(
            onNavigateUp = onBackButtonClick,
            modifier = modifier,
        )
    } else {
        MenuTopBar(
            title = "",
            onNavigateUp = onBackButtonClick,
            onMenuClick = onMenuButtonClick,
            modifier = modifier,
        )
    }
}
