package com.napzak.market.designsystem.component.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_logo
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
fun NapzakLogoTopBar(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth(),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_logo),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(width = 101.dp, height = 33.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NapzakLogoTopBarPreview() {
    NapzakMarketTheme {
        NapzakLogoTopBar(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 17.dp),
        )
    }
}
