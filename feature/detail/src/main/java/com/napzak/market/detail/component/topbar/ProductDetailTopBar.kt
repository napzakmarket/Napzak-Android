package com.napzak.market.detail.component.topbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.detail.R.drawable.ic_back
import com.napzak.market.feature.detail.R.drawable.ic_option
import com.napzak.market.feature.detail.R.string.detail_top_bar_content_description_back
import com.napzak.market.feature.detail.R.string.detail_top_bar_content_description_option
import com.napzak.market.util.android.napzakRippleClickable

@Composable
internal fun DetailTopBar(
    onBackClick: () -> Unit,
    onOptionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shadowElevation = 1.dp,
        color = NapzakMarketTheme.colors.white,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_back),
                contentDescription = stringResource(detail_top_bar_content_description_back),
                tint = NapzakMarketTheme.colors.gray200,
                modifier = Modifier
                    .napzakRippleClickable(
                        role = Role.Button,
                        onClick = onBackClick,
                    ),
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = ImageVector.vectorResource(ic_option),
                contentDescription = stringResource(detail_top_bar_content_description_option),
                tint = NapzakMarketTheme.colors.gray200,
                modifier = Modifier
                    .napzakRippleClickable(
                        role = Role.Button,
                        onClick = onOptionClick,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailTopBarPreview() {
    NapzakMarketTheme {
        DetailTopBar(
            onBackClick = {},
            onOptionClick = {},
        )
    }
}