package com.napzak.market.registration.genre.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.genre_search_error
import com.napzak.market.feature.registration.R.string.genre_search_request

@Composable
fun GenreSearchEmptyView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(genre_search_error),
            style = NapzakMarketTheme.typography.body14r.copy(
                color = NapzakMarketTheme.colors.gray300,
                textAlign = TextAlign.Center,
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(genre_search_request),
            style = NapzakMarketTheme.typography.body14sb.copy(
                color = NapzakMarketTheme.colors.purple500,
            ),
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = NapzakMarketTheme.colors.purple500,
                    shape = RoundedCornerShape(8.dp),
                ).padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}

@Preview
@Composable
private fun GenreSearchEmptyViewPreview() {
    NapzakMarketTheme {
        GenreSearchEmptyView()
    }
}