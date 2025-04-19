package com.napzak.market.registration.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_arrow_right
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.genre
import com.napzak.market.util.android.noRippleClickable

/**
 * Registration genre button
 *
 * @param selectedGenre: 현재 등록하려는 물품의 장르
 * @param onGenreClick: 장르 선택 화면으로 이동
 */
@Composable
internal fun RegistrationGenreButton(
    selectedGenre: String,
    onGenreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .noRippleClickable(onGenreClick)
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(genre),
            style = NapzakMarketTheme.typography.body14b.copy(
                color = NapzakMarketTheme.colors.gray500,
            ),
        )
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedGenre,
                style = NapzakMarketTheme.typography.body14sb.copy(
                    color = NapzakMarketTheme.colors.purple500,
                ),
            )
            Icon(
                imageVector = ImageVector.vectorResource(ic_arrow_right),
                contentDescription = null,
                tint = NapzakMarketTheme.colors.gray300,
            )
        }
    }
}

@Preview
@Composable
private fun RegistrationGenreButtonPreview() {
    NapzakMarketTheme {
        RegistrationGenreButton(
            selectedGenre = "사카모토데이즈",
            onGenreClick = { },
        )
    }
}
