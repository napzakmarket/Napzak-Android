package com.napzak.market.onboarding.genre

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.napzak.market.designsystem.R.drawable.ic_arrow_left
import com.napzak.market.designsystem.R.drawable.ic_third_step_indicator
import com.napzak.market.designsystem.R.string.warning_snackbar_genre_limit_message
import com.napzak.market.designsystem.component.GenreChipButtonGroup
import com.napzak.market.designsystem.component.WarningSnackBar
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.textfield.SearchTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.onboarding.R.string.onboarding_genre_done
import com.napzak.market.feature.onboarding.R.string.onboarding_genre_edit_hint
import com.napzak.market.feature.onboarding.R.string.onboarding_genre_selected_limitation
import com.napzak.market.feature.onboarding.R.string.onboarding_genre_skip
import com.napzak.market.feature.onboarding.R.string.onboarding_genre_sub_title
import com.napzak.market.feature.onboarding.R.string.onboarding_genre_title
import com.napzak.market.onboarding.genre.component.GenreGridList
import com.napzak.market.onboarding.genre.model.GenreUiModel
import com.napzak.market.onboarding.genre.model.GenreUiState
import com.napzak.market.util.android.noRippleClickable

@Composable
fun GenreRoute(
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
    onSkipClick: () -> Unit,
    viewModel: GenreViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.updateGenres(
            listOf(
                GenreUiModel("김종명", "..."),
                GenreUiModel("김채린", "..."),
                GenreUiModel("이석준", "..."),
                GenreUiModel("이연진", "..."),
                GenreUiModel("장재원", "..."),
                GenreUiModel("납작마켓", "..."),
                GenreUiModel("안드", "..."),
                GenreUiModel("파이팅", "..."),
                GenreUiModel("나진짜", "..."),
                GenreUiModel("오늘", "..."),
                GenreUiModel("안잘거야", "..."),
                GenreUiModel("내가만약", "..."),
                GenreUiModel("자면", "..."),
                GenreUiModel("납작", "..."),
                GenreUiModel("아니고", "..."),
                GenreUiModel("넙적이야", "..."),
            )
        )
    }

    GenreScreen(
        uiState = viewModel.uiState.collectAsState().value,
        onGenreClick = viewModel::onGenreClick,
        onGenreRemove = viewModel::onGenreRemove,
        onResetAllGenres = viewModel::onResetAllGenres,
        onSearchTextChange = viewModel::onSearchTextChange,
        onSearchTextReset = { viewModel.onSearchTextChange("") },
        onBackClick = onBackClick,
        onDoneClick = onDoneClick,
        onSkipClick = onSkipClick,
    )
}

@Composable
fun GenreScreen(
    uiState: GenreUiState,
    onGenreClick: (GenreUiModel) -> Unit,
    onGenreRemove: (GenreUiModel) -> Unit,
    onResetAllGenres: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchTextReset: () -> Unit,
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.white),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 40.dp),
        ) {
            GenreTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(24.dp))

            GenreTitleSection()

            Spacer(modifier = Modifier.height(18.dp))

            SearchTextField(
                text = uiState.searchText,
                onTextChange = onSearchTextChange,
                hint = stringResource(onboarding_genre_edit_hint),
                onResetClick = onSearchTextReset,
                onSearchClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                readOnly = true,
                enabled = false,
            )

            if (uiState.selectedGenres.isNotEmpty()) {
                GenreChipButtonGroup(
                    genreNames = uiState.selectedGenres.map { it.name },
                    onResetClick = onResetAllGenres,
                    onGenreClick = { name ->
                        uiState.selectedGenres.find { it.name == name }?.let { onGenreRemove(it) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    contentPaddingValues = PaddingValues(vertical = 8.dp),
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                GenreGridList(
                    genres = uiState.genres,
                    onGenreClick = onGenreClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (uiState.isMaxSelected) {
                WarningSnackBar(
                    message = stringResource(warning_snackbar_genre_limit_message),
                    modifier = Modifier
                        .padding(bottom = 16.dp),
                )
            }

            NapzakButton(
                text = stringResource(onboarding_genre_done),
                onClick = onDoneClick,
                enabled = uiState.isCompleted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            )

            TextButton(onClick = onSkipClick) {
                Text(
                    text = stringResource(onboarding_genre_skip),
                    style = NapzakMarketTheme.typography.caption12m,
                    color = NapzakMarketTheme.colors.gray300,
                )
            }
        }
    }
}

@Composable
private fun GenreTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_arrow_left),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .noRippleClickable(onClick = onBackClick)
                .size(12.dp),
        )

        Icon(
            imageVector = ImageVector.vectorResource(ic_third_step_indicator),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun GenreTitleSection() {
    Text(
        text = stringResource(onboarding_genre_selected_limitation),
        style = NapzakMarketTheme.typography.caption10sb,
        color = NapzakMarketTheme.colors.purple500,
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = stringResource(onboarding_genre_title),
        style = NapzakMarketTheme.typography.title20b,
        color = NapzakMarketTheme.colors.gray400,
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = stringResource(onboarding_genre_sub_title),
        style = NapzakMarketTheme.typography.caption12r,
        color = NapzakMarketTheme.colors.gray300,
    )

    Spacer(modifier = Modifier.height(18.dp))
}

@Preview(showBackground = true)
@Composable
fun GenreScreenPreview() {
    NapzakMarketTheme {
        GenreScreen(
            uiState = GenreUiState(
                genres = listOf(
                    GenreUiModel("로맨스", "...", isSelected = true),
                    GenreUiModel("스릴러", "...", isSelected = false),
                    GenreUiModel("코미디", "...", isSelected = true),
                ),
                searchText = "",
            ),
            onGenreClick = {},
            onGenreRemove = {},
            onResetAllGenres = {},
            onSearchTextChange = {},
            onSearchTextReset = {},
            onBackClick = {},
            onDoneClick = {},
            onSkipClick = {}
        )
    }
}
