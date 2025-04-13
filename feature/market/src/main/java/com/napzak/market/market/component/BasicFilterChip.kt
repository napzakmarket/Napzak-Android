package com.napzak.market.market.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable

@Composable
internal fun BasicFilterChip(
    filterName: String,
    isClicked: Boolean,
    onChipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textColor = if (isClicked) NapzakMarketTheme.colors.purple500
    else NapzakMarketTheme.colors.gray400
    val borderColor = if (isClicked) NapzakMarketTheme.colors.purple500
    else NapzakMarketTheme.colors.gray100

    Row(
        modifier = modifier
            .noRippleClickable(onChipClick)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(size = 72.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = filterName,
            style = NapzakMarketTheme.typography.caption12sb,
            color = textColor,
        )
    }
}

@Preview
@Composable
private fun FilterChipPreview() {
    NapzakMarketTheme {
        Column {
            BasicFilterChip(
                filterName = "미개봉",
                isClicked = false,
                onChipClick = { },
            )
            BasicFilterChip(
                filterName = "미개봉",
                isClicked = true,
                onChipClick = { },
            )
        }
    }
}