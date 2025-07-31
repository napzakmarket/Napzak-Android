package com.napzak.market.store.store.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.drawable.ic_kebap
import com.napzak.market.feature.store.R.drawable.ic_left_chevron
import com.napzak.market.feature.store.R.string.back_button_description
import com.napzak.market.ui_util.noRippleClickable


@Composable
internal fun StoreTopBar(
    isOwner: Boolean,
    onBackButtonClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(start = 20.dp, top = 62.dp, end = 20.dp, bottom = 22.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_left_chevron),
            contentDescription = stringResource(back_button_description),
            tint = NapzakMarketTheme.colors.gray200,
            modifier = Modifier.noRippleClickable(onBackButtonClick),
        )

        Spacer(Modifier.weight(1f))

        if (!isOwner) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_kebap),
                contentDescription = null,
                tint = NapzakMarketTheme.colors.gray200,
                modifier = Modifier.noRippleClickable(onMenuButtonClick),
            )
        }
    }
}
