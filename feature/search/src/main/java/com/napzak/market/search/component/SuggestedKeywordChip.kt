package com.napzak.market.search.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable

/**
 * 추천 검색어 Chip
 *
 * @param keyword 추천 검색어 값
 * @param onKeywordChipClick chip 클릭 시 실행됨
 * @param lineColor 선, 글자 컬러값
 */
@Composable
internal fun SuggestedKeywordChip(
    keyword: String,
    onKeywordChipClick: () -> Unit,
    lineColor: Color = NapzakMarketTheme.colors.purple500,
) {
    Box(
        modifier = Modifier
            .noRippleClickable(onKeywordChipClick)
            .border(
                width = 1.dp,
                color = lineColor,
                shape = RoundedCornerShape(size = 50.dp)
            )
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = keyword,
            style = NapzakMarketTheme.typography.caption12sb,
            color = lineColor,
        )
    }
}

@Preview
@Composable
private fun SuggestedKeywordChipPreview() {
    NapzakMarketTheme {
        SuggestedKeywordChip(
            keyword = "짱구는 못말려 날아라 수제김밥",
            onKeywordChipClick = { },
        )
    }
}