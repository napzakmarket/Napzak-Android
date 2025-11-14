package com.napzak.market.designsystem.component.bottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowInsetsControllerCompat
import com.napzak.market.designsystem.R.drawable.ic_dark_gray_cancel
import com.napzak.market.designsystem.R.string.genre_apply_button
import com.napzak.market.designsystem.R.string.genre_search_genre_limit_notice
import com.napzak.market.designsystem.R.string.genre_search_hint
import com.napzak.market.designsystem.R.string.genre_search_select_genre
import com.napzak.market.designsystem.R.string.warning_snackbar_genre_limit_message
import com.napzak.market.designsystem.component.ChipButtonGroup
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.loading.NapzakLoadingSpinnerOverlay
import com.napzak.market.designsystem.component.textfield.SearchTextField
import com.napzak.market.designsystem.component.toast.WarningSnackBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.disableContrastEnforcement
import com.napzak.market.ui_util.noRippleClickable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 장르 검색 BottomSheet
 *
 * @param genreBottomSheetState 장르 바텀시트의 상태홀더
 * @param onDismissRequest x 버튼 또는 BottomSheet 외의 영역 클릭 시 실행됨
 * @param onTextChange 검색어 입력 시 실행됨
 * @param onButtonClick 적용하기 버튼 클릭 시 실행됨
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreSearchBottomSheet(
    genreBottomSheetState: GenreBottomSheetState,
    onDismissRequest: () -> Unit,
    onTextChange: (String) -> Unit,
    onButtonClick: (List<BottomSheetGenre>) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var isShownSnackBar by remember { mutableStateOf(false) }
    var isLoadingOn by remember { mutableStateOf(true) }

    LaunchedEffect(genreBottomSheetState.sheetState.currentValue) {
        if (genreBottomSheetState.sheetState.currentValue == SheetValue.Expanded) {
            delay(500)
            isLoadingOn = false
        }
    }

    LaunchedEffect(genreBottomSheetState.searchText) { onTextChange(genreBottomSheetState.searchText) }

    ModalBottomSheet(
        onDismissRequest = {
            focusManager.clearFocus()
            onDismissRequest()
        },
        sheetState = genreBottomSheetState.sheetState,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = NapzakMarketTheme.colors.white,
        scrimColor = NapzakMarketTheme.colors.transBlack,
        dragHandle = null,
    ) {
        val view = LocalView.current
        val dialogWindow = (view.parent as? DialogWindowProvider)?.window

        SideEffect {
            dialogWindow?.let { window ->
                window.disableContrastEnforcement()
                val controller = WindowInsetsControllerCompat(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = true
            }
        }

        Box(
            modifier = Modifier.fillMaxHeight(0.75f),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = NapzakMarketTheme.colors.white)
                        .padding(top = 18.dp, start = 20.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(ic_dark_gray_cancel),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .noRippleClickable {
                                coroutineScope
                                    .launch { genreBottomSheetState.hide() }
                                    .invokeOnCompletion {
                                        focusManager.clearFocus()
                                        onDismissRequest()
                                    }
                            }
                            .padding(end = 18.dp),
                    )

                    GenreSearchNoticeSection()

                    SearchTextField(
                        text = genreBottomSheetState.searchText,
                        onTextChange = { newSearchText ->
                            genreBottomSheetState.searchText = newSearchText
                        },
                        hint = stringResource(genre_search_hint),
                        onResetClick = { genreBottomSheetState.searchText = "" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp),
                        onSearchClick = { onTextChange(genreBottomSheetState.searchText) },
                    )

                    if (genreBottomSheetState.selectedGenres.isNotEmpty()) {
                        ChipButtonGroup(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 23.dp),
                            items = genreBottomSheetState.selectedGenres.map(BottomSheetGenre::name),
                            contentPaddingValues = PaddingValues(end = 20.dp),
                            onResetClick = genreBottomSheetState::resetSelectedGenres,
                            onChipClick = genreBottomSheetState::removeSelectedGenre,
                        )
                    } else {
                        Spacer(Modifier.height(25.dp))
                    }
                }

                if (isLoadingOn) {
                    NapzakLoadingSpinnerOverlay(Modifier.background(NapzakMarketTheme.colors.gray50))
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = NapzakMarketTheme.colors.gray50)
                            .padding(horizontal = 20.dp),
                    ) {
                        GenreList(
                            genreItems = genreBottomSheetState.genreItems,
                            selectedGenreList = genreBottomSheetState.selectedGenres,
                            onGenreItemClick = genreBottomSheetState::handleGenreClick
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.3889f)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                NapzakMarketTheme.colors.white,
                            )
                        )
                    ),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedVisibility(
                    visible = isShownSnackBar,
                    enter = fadeIn(initialAlpha = 0f),
                    exit = fadeOut(targetAlpha = 0f),
                ) {
                    WarningSnackBar(
                        message = stringResource(warning_snackbar_genre_limit_message),
                        modifier = Modifier
                            .padding(bottom = 36.dp)
                    )
                }

                ButtonSection(
                    onButtonClick = {
                        coroutineScope
                            .launch { genreBottomSheetState.hide() }
                            .invokeOnCompletion {
                                focusManager.clearFocus()
                                onButtonClick(genreBottomSheetState.selectedGenres)
                            }
                    }
                )
            }
        }
    }
}

@Composable
private fun GenreSearchNoticeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 25.dp),
    ) {
        Text(
            text = stringResource(genre_search_select_genre),
            style = NapzakMarketTheme.typography.title20b,
            color = NapzakMarketTheme.colors.gray500,
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = stringResource(genre_search_genre_limit_notice),
            style = NapzakMarketTheme.typography.caption12m,
            color = NapzakMarketTheme.colors.purple500,
        )
    }
}

@Composable
private fun GenreList(
    genreItems: List<BottomSheetGenre>,
    selectedGenreList: List<BottomSheetGenre>,
    onGenreItemClick: (BottomSheetGenre, Boolean) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        item { Spacer(Modifier.height(10.dp)) }

        items(genreItems) { genreItem ->
            val isSelected = selectedGenreList.contains(genreItem)

            GenreListItem(
                isSelected = isSelected,
                genreName = genreItem.name,
                onGenreItemClick = { onGenreItemClick(genreItem, isSelected) }
            )
        }

        item { Spacer(Modifier.height(120.dp)) }
    }
}

@Composable
private fun ButtonSection(
    onButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        NapzakButton(
            text = stringResource(genre_apply_button),
            onClick = onButtonClick,
            enabled = true,
            modifier = Modifier
                .fillMaxWidth(),
        )

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun GenreListItem(
    isSelected: Boolean,
    genreName: String,
    onGenreItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textStyle =
        if (isSelected) NapzakMarketTheme.typography.body14sb
        else NapzakMarketTheme.typography.body14r
    val textColor =
        if (isSelected) NapzakMarketTheme.colors.purple500
        else NapzakMarketTheme.colors.gray400

    Text(
        text = genreName,
        style = textStyle,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .noRippleClickable(onGenreItemClick),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun GenreSearchBottomSheetPreview() {
    NapzakMarketTheme {
        val sheetState = rememberGenreBottomSheetState(
            initiallySelectedGenres = listOf(
                BottomSheetGenre(0, "산리오"),
                BottomSheetGenre(1, "주술회전"),
                BottomSheetGenre(2, "진격의 거인"),
            ),
            genres = listOf(
                BottomSheetGenre(0, "산리오"),
                BottomSheetGenre(1, "주술회전"),
                BottomSheetGenre(2, "진격의 거인"),
                BottomSheetGenre(3, "산리오1"),
                BottomSheetGenre(4, "주술회전1"),
                BottomSheetGenre(5, "진격의 거인1"),
                BottomSheetGenre(6, "산리오2"),
                BottomSheetGenre(7, "주술회전2"),
                BottomSheetGenre(8, "진격의 거인2"),
                BottomSheetGenre(9, "산리오3"),
                BottomSheetGenre(10, "주술회전3"),
            ),
        )

        GenreSearchBottomSheet(
            genreBottomSheetState = sheetState,
            onDismissRequest = { },
            onTextChange = { },
            onButtonClick = { },
        )
    }
}