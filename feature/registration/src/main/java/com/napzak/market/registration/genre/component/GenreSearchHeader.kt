package com.napzak.market.registration.genre.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.component.textfield.SearchTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.genre_search_title
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.noRippleClickable

private const val BLANK = ""

@Composable
fun GenreSearchHeader(
    onBackClick: () -> Unit,
    searchTerm: String,
    onSearchTermChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .napzakGradientShadow(
                height = 4.dp,
                startColor = NapzakMarketTheme.colors.shadowBlack,
                endColor = NapzakMarketTheme.colors.transWhite,
                direction = ShadowDirection.Bottom,
            )
            .background(color = NapzakMarketTheme.colors.white)
            .padding(bottom = 24.dp),
    ) {
        val paddedModifier = Modifier.padding(horizontal = 28.dp)

        Spacer(modifier = Modifier.height(18.dp))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_gray_arrow_left),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .noRippleClickable(onBackClick)
                .padding(start = 20.dp),
            tint = NapzakMarketTheme.colors.gray200,
        )

        Spacer(modifier = Modifier.height(38.dp))

        Text(
            text = stringResource(genre_search_title),
            style = NapzakMarketTheme.typography.title20b.copy(
                color = NapzakMarketTheme.colors.gray500,
            ),
            modifier = paddedModifier,
        )

        Spacer(modifier = Modifier.height(24.dp))

        SearchTextField(
            text = searchTerm,
            onTextChange = onSearchTermChange,
            hint = stringResource(com.napzak.market.feature.registration.R.string.genre_search_hint),
            onResetClick = { onSearchTermChange(BLANK) },
            onSearchClick = {},
            modifier = paddedModifier,
        )
    }
}

@Preview
@Composable
private fun GenreSearchHeaderPreview() {
    NapzakMarketTheme {
        GenreSearchHeader(
            onBackClick = {},
            searchTerm = BLANK,
            onSearchTermChange = {},
        )
    }
}
