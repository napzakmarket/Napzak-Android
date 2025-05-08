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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GenreRoute(
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
    onSkipClick: () -> Unit,
    viewModel: GenreViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var isShownSnackBar by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.updatePreferredGenre()
    }

    GenreScreen(
        uiState = uiState,
        onGenreClick = { genre ->
            if (!viewModel.onGenreClick(genre) && uiState.selectedGenres.size >= 7) {
                isShownSnackBar = true
                scope.launch {
                    delay(2000)
                    isShownSnackBar = false
                }
            }
        },
        onGenreRemove = viewModel::onGenreRemove,
        onAllGenresReset = viewModel::onResetAllGenres,
        onSearchTextChange = viewModel::onSearchTextChange,
        onSearchTextSubmit = viewModel::onSearchTextSubmit,
        onSearchTextReset = { viewModel.onSearchTextChange("") },
        onBackClick = onBackClick,
        onDoneClick = onDoneClick,
        onSkipClick = onSkipClick,
        isShownSnackBar = isShownSnackBar,
    )
}

@Composable
fun GenreScreen(
    uiState: GenreUiState,
    onGenreClick: (GenreUiModel) -> Unit,
    onGenreRemove: (GenreUiModel) -> Unit,
    onAllGenresReset: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchTextSubmit: () -> Unit,
    onSearchTextReset: () -> Unit,
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
    onSkipClick: () -> Unit,
    isShownSnackBar: Boolean,
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
                onSearchClick = onSearchTextSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                readOnly = false,
                enabled = true,
            )

            if (uiState.selectedGenres.isNotEmpty()) {
                GenreChipButtonGroup(
                    genreNames = uiState.selectedGenres.map { it.name },
                    onResetClick = onAllGenresReset,
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
            if (isShownSnackBar) {
                WarningSnackBar(
                    message = stringResource(warning_snackbar_genre_limit_message),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
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
            uiState = GenreUiState(),
            onGenreClick = {},
            onGenreRemove = {},
            onAllGenresReset = {},
            onSearchTextChange = {},
            onSearchTextSubmit = {},
            onSearchTextReset = {},
            onBackClick = {},
            onDoneClick = {},
            isShownSnackBar = true,
            onSkipClick = {}
        )
    }
}
