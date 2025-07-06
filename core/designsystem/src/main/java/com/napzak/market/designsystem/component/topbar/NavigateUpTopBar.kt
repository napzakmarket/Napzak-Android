package com.napzak.market.designsystem.component.topbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_back_24
import com.napzak.market.designsystem.R.string.top_bar_navigate_up
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.noRippleClickable


@Composable
fun NavigateUpTopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    titleColor: Color = NapzakMarketTheme.colors.gray400,
    iconColor: Color = NapzakMarketTheme.colors.gray200,
) {
    Surface(
        modifier = modifier,
        shadowElevation = 2.dp,
        color = NapzakMarketTheme.colors.white,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp)
                .padding(top = 34.dp, bottom = 18.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_back_24),
                contentDescription = stringResource(top_bar_navigate_up),
                tint = iconColor,
                modifier = Modifier
                    .semantics { role = Role.Button }
                    .noRippleClickable(
                        onClick = onNavigateUp,
                    ),
            )

            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = title,
                style = NapzakMarketTheme.typography.body16b.copy(
                    color = titleColor,
                ),
                maxLines = 1,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NavigateUpTopBarPreview() {
    NapzakMarketTheme {
        Column {
            NavigateUpTopBar(
                title = "테스트",
                onNavigateUp = {},
            )
            Spacer(Modifier.height(40.dp))
        }
    }
}
