package com.napzak.market.store.edit_store.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.string.store_edit_button_genre
import com.napzak.market.feature.store.R.string.store_edit_sub_title_genre
import com.napzak.market.feature.store.R.string.store_edit_title_genre

/**
 * 관심 장르 목록을 보여주고, 장르 선택 버튼을 제공하는 컴포넌트
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun EditStorePreferredGenreSection(
    genres: List<String>,
    onGenreSelectButtonClick: () -> Unit,
) {
    val paddingValues = PaddingValues(horizontal = 20.dp)

    EditStoreProfileContainer(
        title = stringResource(store_edit_title_genre),
        subtitle = stringResource(store_edit_sub_title_genre),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(paddingValues),
        ) {
            genres.forEach { genreName ->
                GenreChip(genreName = genreName)
            }
        }

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = onGenreSelectButtonClick,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NapzakMarketTheme.colors.gray50,
                contentColor = NapzakMarketTheme.colors.gray400,
            ),
            shape = RoundedCornerShape(14.dp),
        ) {
            Text(
                text = stringResource(store_edit_button_genre),
                style = NapzakMarketTheme.typography.body14b,
            )
        }
    }
}

@Composable
private fun GenreChip(
    genreName: String,
    modifier: Modifier = Modifier,
) {
    val contentColor = NapzakMarketTheme.colors.purple500

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = contentColor,
                shape = RoundedCornerShape(size = 50.dp),
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(
            text = genreName,
            style = NapzakMarketTheme.typography.caption12sb.copy(
                color = contentColor,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditStoreInterestedGenreSectionPreview() {
    NapzakMarketTheme {
        Column {
            EditStorePreferredGenreSection(
                genres = listOf("장르1", "장르2", "장르3"),
                onGenreSelectButtonClick = {},
            )
        }
    }
}
