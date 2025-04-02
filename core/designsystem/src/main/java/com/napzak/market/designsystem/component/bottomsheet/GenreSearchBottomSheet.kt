package com.napzak.market.designsystem.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_x_button_24
import com.napzak.market.designsystem.R.string.genre_search_hint
import com.napzak.market.designsystem.R.string.genre_search_select_genre
import com.napzak.market.designsystem.R.string.genre_search_genre_limit_notice
import com.napzak.market.designsystem.component.GenreChipButtonGroup
import com.napzak.market.designsystem.component.textfield.SearchTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable
import kotlinx.coroutines.launch

/**
 * 장르 검색 BottomSheet
 *
 * @param initialSelectedGenreList 선택한 장르 리스트
 * @param genreItems 하단에 보여지는 장르 리스트
 * @param sheetState BottomSheet 상태
 * @param debounce 검색어 입력 시 실행됨
 * @param onDismissRequest x 버튼 또는 BottomSheet 외의 영역 클릭 시 실행됨
 * @param onTextChange 검색어 입력 시 실행됨
 * @param onButtonClick 적용하기 버튼 클릭 시 실행됨
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreSearchBottomSheet(
    initialSelectedGenreList: List<Genre>,
    genreItems: List<Genre>,
    sheetState: SheetState,
    debounce: () -> Unit,
    onDismissRequest: () -> Unit,
    onTextChange: (String) -> Unit,
    onButtonClick: (List<Genre>) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var searchText by remember { mutableStateOf("") }
    var selectedGenreList by remember {
        mutableStateOf<List<Genre>>(
            initialSelectedGenreList
        )
    }

    LaunchedEffect(Unit) {
        debounce()
        sheetState.expand()
    }
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
            modifier = Modifier.height(594.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = NapzakMarketTheme.colors.white)
                        .padding(top = 18.dp)
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(ic_x_button_24),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .noRippleClickable {
                                onDismissRequest()
                                focusManager.clearFocus()
                            },
                    )

                    GenreSearchNoticeSection()

                    SearchTextField(
                        text = searchText,
                        onTextChange = { newSearchText ->
                            searchText = newSearchText
                            onTextChange(newSearchText)
                        },
                        hint = stringResource(genre_search_hint),
                        onResetClick = { searchText = "" },
                        modifier = Modifier.fillMaxWidth(),
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
                            onGenreClick = { genreItem ->
                                selectedGenreList =
                                    selectedGenreList.filterNot { it.genreName == genreItem }
                            },
                        )
                    } else {
                        Spacer(Modifier.height(25.dp))
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
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
                                selectedGenreList += genreItem
                            }
                        },
                    )
                }
            }

            ButtonSection(
                onButtonClick = { onButtonClick(selectedGenreList) }
            )
        }
    }
}

@Composable
fun GenreSearchNoticeSection() {
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
fun GenreList(
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
            val textStyle =
                if (isSelected) NapzakMarketTheme.typography.body14sb
                else NapzakMarketTheme.typography.body14r
            val textColor =
                if (isSelected) NapzakMarketTheme.colors.purple500
                else NapzakMarketTheme.colors.gray400

            Text(
                text = genreItem.genreName,
                style = textStyle,
                color = textColor,
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .noRippleClickable { onGenreItemClick(genreItem, isSelected) },
            )
        }

        item { Spacer(Modifier.height(13.dp)) }
    }
}

@Composable
fun ButtonSection(
    onButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        // TODO: 추후 버튼 컴포넌트로 변경 예정
        Text(
            text = "버튼",
            modifier = Modifier.noRippleClickable(onButtonClick),
        )

        Spacer(Modifier.height(64.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GenreSearchBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()

    NapzakMarketTheme {
        GenreSearchBottomSheet(
            initialSelectedGenreList = listOf(
                Genre(0, "산리오"),
                Genre(1, "주술회전"),
                Genre(2, "진격의 거인"),
            ),
            sheetState = sheetState,
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
            debounce = { },
            onDismissRequest = {
                coroutineScope.launch { sheetState.hide() }
            },
            onTextChange = { },
            onButtonClick = { },
        )
    }
}