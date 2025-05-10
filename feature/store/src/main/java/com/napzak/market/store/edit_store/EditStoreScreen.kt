package com.napzak.market.store.edit_store

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.component.bottomsheet.Genre
import com.napzak.market.designsystem.component.bottomsheet.GenreSearchBottomSheet
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.textfield.NapzakDefaultTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.drawable.ic_left_chevron
import com.napzak.market.feature.store.R.drawable.ic_profile_basic
import com.napzak.market.feature.store.R.drawable.ic_profile_edit
import com.napzak.market.feature.store.R.string.back_button_description
import com.napzak.market.feature.store.R.string.store_edit_button_genre
import com.napzak.market.feature.store.R.string.store_edit_button_name_check
import com.napzak.market.feature.store.R.string.store_edit_button_proceed
import com.napzak.market.feature.store.R.string.store_edit_hint_introduction
import com.napzak.market.feature.store.R.string.store_edit_hint_name
import com.napzak.market.feature.store.R.string.store_edit_profile
import com.napzak.market.feature.store.R.string.store_edit_sub_title_genre
import com.napzak.market.feature.store.R.string.store_edit_sub_title_introduction
import com.napzak.market.feature.store.R.string.store_edit_sub_title_name
import com.napzak.market.feature.store.R.string.store_edit_title_genre
import com.napzak.market.feature.store.R.string.store_edit_title_introduction
import com.napzak.market.feature.store.R.string.store_edit_title_name
import com.napzak.market.util.android.noRippleClickable

private const val DESCRIPTION_MAX_LENGTH = 200

@Composable
internal fun EditStoreRoute(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var storeName by remember { mutableStateOf("") }
    var storeIntroduction by remember { mutableStateOf("") }
    var storeGenres by remember { mutableStateOf(emptyList<Genre>()) }

    EditStoreScreen(
        storeCover = "",
        storeProfile = "",
        storeName = storeName,
        storeIntroduction = storeIntroduction,
        storeGenres = storeGenres,
        onStoreNameChange = { storeName = it },
        onStoreIntroductionChange = { storeIntroduction = it },
        onGenreSearchTextChange = { }, // TODO: 장르 검색 API 연결
        onBackButtonClick = onNavigateUp,
        onNameValidityCheckClick = { }, // TODO: 이름 유효성 검사 API 연결
        onProceedButtonClick = { }, // TODO: 마켓 수정 API 연결
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditStoreScreen(
    storeCover: String,
    storeProfile: String,
    storeName: String,
    storeIntroduction: String,
    storeGenres: List<Genre>,
    onStoreNameChange: (String) -> Unit,
    onStoreIntroductionChange: (String) -> Unit,
    onGenreSearchTextChange: (String) -> Unit,
    onBackButtonClick: () -> Unit,
    onNameValidityCheckClick: () -> Unit,
    onProceedButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var bottomSheetVisibility by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            EditStoreTopBar(
                onBackButtonClick = onBackButtonClick,
            )
        },
        bottomBar = {
            EditStoreProceedButton(
                onClick = onProceedButtonClick,
            )
        },
        containerColor = NapzakMarketTheme.colors.white,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            EditStorePhotoSection(
                storeCover = storeCover,
                storePhoto = storeProfile,
                onEditClick = {},
            )

            Spacer(Modifier.height(24.dp))

            EditStoreNameSection(
                marketName = storeName,
                onNameChange = onStoreNameChange,
                onNameValidityCheckClick = onNameValidityCheckClick,
                checkEnabled = false,
            )

            SectionDivider()

            EditStoreIntroductionSection(
                introduction = storeIntroduction,
                onIntroductionChange = onStoreIntroductionChange,
            )

            SectionDivider()

            EditInterestedGenreSection(
                genres = storeGenres.map { it.genreName },
                onGenreSelectButtonClick = { bottomSheetVisibility = true },
            )

            Spacer(Modifier.height(18.dp))
        }

        if (bottomSheetVisibility) {
            GenreSearchBottomSheet(
                initialSelectedGenreList = emptyList(),
                genreItems = emptyList(),
                sheetState = bottomSheetState,
                onDismissRequest = {
                    bottomSheetVisibility = false
                },
                onTextChange = onGenreSearchTextChange,
                onButtonClick = { bottomSheetVisibility = false },
            )
        }
    }
}

@Composable
private fun EditStoreTopBar(
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(start = 20.dp, top = 62.dp, end = 20.dp, bottom = 22.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_left_chevron),
            contentDescription = stringResource(back_button_description),
            tint = NapzakMarketTheme.colors.gray200,
            modifier = Modifier.noRippleClickable(onBackButtonClick),
        )

        Spacer(Modifier.width(9.dp))

        Text(
            text = stringResource(store_edit_profile),
            style = NapzakMarketTheme.typography.body16b.copy(
                color = NapzakMarketTheme.colors.gray400,
            )
        )
    }
}

@Composable
private fun EditStoreProceedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 63.dp, top = 13.dp),
    ) {
        NapzakButton(
            text = stringResource(store_edit_button_proceed),
            onClick = onClick,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

/**
 * 마켓 프로필 및 커버 이미지를 편집하는 컴포넌트
 */
@Composable
private fun EditStorePhotoSection(
    storeCover: String,
    storePhoto: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data(storeCover)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2.25f)
                .background(NapzakMarketTheme.colors.gray200),
        )

        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data(storePhoto)
                .placeholder(ic_profile_basic)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .size(110.dp)
                .border(
                    width = 5.dp,
                    color = NapzakMarketTheme.colors.white,
                    shape = CircleShape,
                ),
        )

        Icon(
            imageVector = ImageVector.vectorResource(ic_profile_edit),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 96.dp, bottom = 16.dp)
                .noRippleClickable(onEditClick),
        )
    }
}

@Composable
private fun EditStoreProfileContainer(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
) {
    val paddingValues = PaddingValues(horizontal = 20.dp)

    Text(
        text = title,
        style = NapzakMarketTheme.typography.body14sb.copy(
            color = NapzakMarketTheme.colors.gray400,
        ),
        modifier = Modifier.padding(paddingValues),
    )

    Spacer(Modifier.height(4.dp))

    Text(
        text = subtitle,
        style = NapzakMarketTheme.typography.caption10r.copy(
            color = NapzakMarketTheme.colors.gray300,
        ),
        modifier = Modifier.padding(paddingValues),
    )

    Spacer(Modifier.height(20.dp))

    content()
}

/**
 * 마켓의 이름을 수정하는 입력 필드를 제공하는 컴포넌트
 */
@Composable
private fun EditStoreNameSection(
    marketName: String,
    onNameChange: (String) -> Unit,
    checkEnabled: Boolean,
    onNameValidityCheckClick: () -> Unit,
) {
    val buttonColor = with(NapzakMarketTheme.colors) {
        if (checkEnabled) purple500
        else gray200
    }

    EditStoreProfileContainer(
        title = stringResource(store_edit_title_name),
        subtitle = stringResource(store_edit_sub_title_name),
    ) {
        NapzakDefaultTextField(
            text = marketName,
            onTextChange = onNameChange,
            hint = stringResource(store_edit_hint_name),
            textStyle = NapzakMarketTheme.typography.caption12sb,
            hintTextStyle = NapzakMarketTheme.typography.caption12m,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .background(
                    NapzakMarketTheme.colors.gray50,
                    RoundedCornerShape(14.dp),
                )
                .padding(PaddingValues(16.dp, 10.dp, 10.dp, 10.dp)),
            suffix = {
                Box(
                    modifier = Modifier
                        .noRippleClickable { if (checkEnabled) onNameValidityCheckClick() }
                        .background(buttonColor, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(store_edit_button_name_check),
                        style = NapzakMarketTheme.typography.caption12sb.copy(
                            color = NapzakMarketTheme.colors.gray50,
                        ),
                    )
                }
            }
        )
    }
}

/**
 * 마켓의 소개를 수정하는 입력 필드를 제공하는 컴포넌트
 */
@Composable
private fun EditStoreIntroductionSection(
    introduction: String,
    onIntroductionChange: (String) -> Unit,
) {
    val paddingValues = PaddingValues(horizontal = 20.dp)

    EditStoreProfileContainer(
        title = stringResource(store_edit_title_introduction),
        subtitle = stringResource(store_edit_sub_title_introduction),
    ) {
        NapzakDefaultTextField(
            text = introduction,
            onTextChange = onIntroductionChange,
            hint = stringResource(store_edit_hint_introduction),
            textStyle = NapzakMarketTheme.typography.caption12sb,
            hintTextStyle = NapzakMarketTheme.typography.caption12m,
            verticalAlignment = Alignment.Top,
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    NapzakMarketTheme.colors.gray50,
                    RoundedCornerShape(14.dp)
                )
                .padding(PaddingValues(14.dp, 16.dp, 14.dp, 16.dp)),
        )

        Spacer(Modifier.height(14.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
        ) {
            Text(
                text = "${introduction.length}/$DESCRIPTION_MAX_LENGTH",
                style = NapzakMarketTheme.typography.caption10r.copy(
                    color = NapzakMarketTheme.colors.gray300,
                ),
            )
        }
    }
}

/**
 * 관심 장르 목록을 보여주고, 장르 선택 버튼을 제공하는 컴포넌트
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EditInterestedGenreSection(
    genres: List<String>,
    onGenreSelectButtonClick: () -> Unit,
) {
    val paddingValues = PaddingValues(horizontal = 20.dp)
    val contentColor = NapzakMarketTheme.colors.purple500

    EditStoreProfileContainer(
        title = stringResource(store_edit_title_genre),
        subtitle = stringResource(store_edit_sub_title_genre),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(paddingValues),
        ) {
            genres.forEach { genre ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = contentColor,
                            shape = RoundedCornerShape(size = 50.dp),
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = genre,
                        style = NapzakMarketTheme.typography.caption12sb.copy(
                            color = contentColor,
                        ),
                    )
                }
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

/**
 * 섹션 간 분할을 담당하는 4dp 두께의 가로 줄 컴포넌트
 */
@Composable
private fun SectionDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        color = NapzakMarketTheme.colors.gray10,
        thickness = 4.dp,
        modifier = modifier.padding(vertical = 30.dp),
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun EditStoreScreenPreview() {
    NapzakMarketTheme {
        var storeName by remember { mutableStateOf("") }
        var storeIntroduction by remember { mutableStateOf("") }
        var storeGenres by remember { mutableStateOf(emptyList<Genre>()) }

        EditStoreScreen(
            storeCover = "",
            storeProfile = "",
            storeName = storeName,
            storeIntroduction = storeIntroduction,
            storeGenres = storeGenres,
            onStoreNameChange = { storeName = it },
            onStoreIntroductionChange = { storeIntroduction = it },
            onGenreSearchTextChange = { },
            onBackButtonClick = {},
            onNameValidityCheckClick = {},
            onProceedButtonClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
