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
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_x_button_24
import com.napzak.market.designsystem.R.string.genre_apply_button
import com.napzak.market.designsystem.R.string.genre_search_genre_limit_notice
import com.napzak.market.designsystem.R.string.genre_search_hint
import com.napzak.market.designsystem.R.string.genre_search_select_genre
import com.napzak.market.designsystem.R.string.warning_snackbar_genre_limit_message
import com.napzak.market.designsystem.component.GenreChipButtonGroup
import com.napzak.market.designsystem.component.GenreListItem
import com.napzak.market.designsystem.component.WarningSnackBar
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.textfield.SearchTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.genre.model.Genre
import com.napzak.market.ui_util.noRippleClickable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 장르 검색 BottomSheet
 *
 * @param initialSelectedGenreList 선택한 장르 리스트
 * @param genreItems 하단에 보여지는 장르 리스트
 * @param onDismissRequest x 버튼 또는 BottomSheet 외의 영역 클릭 시 실행됨
 * @param onTextChange 검색어 입력 시 실행됨
 * @param onButtonClick 적용하기 버튼 클릭 시 실행됨
 * @param sheetState 바텀시트 상태 값 (default값 사용 권장)
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreSearchBottomSheet(
    initialSelectedGenreList: List<Genre>,
    genreItems: List<Genre>,
    onDismissRequest: () -> Unit,
    onTextChange: (String) -> Unit,
    onButtonClick: (List<Genre>) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var searchText by remember { mutableStateOf("") }
    var selectedGenreList by remember {
        mutableStateOf<List<Genre>>(
            initialSelectedGenreList
        )
    }
    var isShownSnackBar by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { sheetState.expand() }
    LaunchedEffect(searchText) { onTextChange(searchText) }

    ModalBottomSheet(
        onDismissRequest = {
            focusManager.clearFocus()
            onDismissRequest()
        },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 31.dp, topEnd = 31.dp),
        containerColor = NapzakMarketTheme.colors.white,
        scrimColor = NapzakMarketTheme.colors.transBlack,
        dragHandle = null,
    ) {
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
                        imageVector = ImageVector.vectorResource(ic_x_button_24),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .noRippleClickable {
                                coroutineScope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        focusManager.clearFocus()
                                        onDismissRequest()
                                    }
                            }
                            .padding(end = 18.dp),
                    )

                    GenreSearchNoticeSection()

                    SearchTextField(
                        text = searchText,
                        onTextChange = { newSearchText ->
                            searchText = newSearchText
                        },
                        hint = stringResource(genre_search_hint),
                        onResetClick = { searchText = "" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp),
                        onSearchClick = { onTextChange(searchText) },
                    )

                    if (selectedGenreList.isNotEmpty()) {
                        GenreChipButtonGroup(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 23.dp),
                            genreNames = selectedGenreList.map(Genre::genreName),
                            contentPaddingValues = PaddingValues(end = 20.dp),
                            onResetClick = { selectedGenreList = emptyList() },
                            onGenreClick = { genreName ->
                                selectedGenreList =
                                    selectedGenreList.filterNot { it.genreName == genreName }
                            },
                        )
                    } else {
                        Spacer(Modifier.height(25.dp))
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = NapzakMarketTheme.colors.gray50)
                        .padding(horizontal = 20.dp),
                ) {
                    GenreList(
                        genreItems = genreItems,
                        selectedGenreList = selectedGenreList,
                        onGenreItemClick = { genreItem, isSelected ->
                            if (isSelected) {
                                selectedGenreList =
                                    selectedGenreList.filterNot { it.genreName == genreItem.genreName }
                            } else {
                                if (selectedGenreList.size < 7) {
                                    selectedGenreList += genreItem
                                } else {
                                    isShownSnackBar = true
                                    scope.launch {
                                        delay(2000)
                                        isShownSnackBar = false
                                    }
                                }
                            }
                        },
                    )
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
                            .launch { sheetState.hide() }
                            .invokeOnCompletion {
                                focusManager.clearFocus()
                                onButtonClick(selectedGenreList)
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
    genreItems: List<Genre>,
    selectedGenreList: List<Genre>,
    onGenreItemClick: (Genre, Boolean) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        item { Spacer(Modifier.height(10.dp)) }

        items(genreItems) { genreItem ->
            val isSelected = selectedGenreList.contains(genreItem)

            GenreListItem(
                isSelected = isSelected,
                genreName = genreItem.genreName,
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

        Spacer(Modifier.height(64.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun GenreSearchBottomSheetPreview() {
    NapzakMarketTheme {
        GenreSearchBottomSheet(
            initialSelectedGenreList = listOf(
                Genre(0, "산리오"),
                Genre(1, "주술회전"),
                Genre(2, "진격의 거인"),
            ),
            genreItems = listOf(
                Genre(0, "산리오"),
                Genre(1, "주술회전"),
                Genre(2, "진격의 거인"),
                Genre(3, "산리오1"),
                Genre(4, "주술회전1"),
                Genre(5, "진격의 거인1"),
                Genre(6, "산리오2"),
                Genre(7, "주술회전2"),
                Genre(8, "진격의 거인2"),
                Genre(9, "산리오3"),
                Genre(10, "주술회전3"),
            ),
            onDismissRequest = { },
            onTextChange = { },
            onButtonClick = { },
        )
    }
}