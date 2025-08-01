package com.napzak.market.store.store.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.img_emtpy_upload
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.string.store_empty_product_subtitle
import com.napzak.market.feature.store.R.string.store_empty_product_title

@Composable
internal fun StoreEmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 48.dp, bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(img_emtpy_upload),
            contentDescription = null,
            tint = Color.Unspecified,
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(store_empty_product_title),
            style = NapzakMarketTheme.typography.body16sb.copy(
                color = NapzakMarketTheme.colors.gray300,
            )
        )

        Text(
            text = stringResource(store_empty_product_subtitle),
            style = NapzakMarketTheme.typography.caption12sb.copy(
                color = NapzakMarketTheme.colors.gray200,
            )
        )
    }
}

@Preview
@Composable
private fun StoreEmptyViewPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        StoreEmptyView(modifier)
    }
}