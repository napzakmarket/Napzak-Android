package com.napzak.market.store.edit_store

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.component.bottomsheet.GenreSearchBottomSheet
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.topbar.NavigateUpTopBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.string.store_edit_button_proceed
import com.napzak.market.feature.store.R.string.store_edit_profile
import com.napzak.market.genre.model.Genre
import com.napzak.market.presigned_url.type.PhotoType
import com.napzak.market.store.edit_store.component.EditStoreDescriptionSection
import com.napzak.market.store.edit_store.component.EditStoreNickNameSection
import com.napzak.market.store.edit_store.component.EditStorePhotoSection
import com.napzak.market.store.edit_store.component.EditStorePreferredGenreSection
import com.napzak.market.store.edit_store.state.EditStoreUiState
import com.napzak.market.store.model.NicknameValidationResult
import com.napzak.market.store.model.StoreEditGenre

@Composable
internal fun EditStoreRoute(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditStoreViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getEditProfile()
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect {
                if (it is EditStoreSideEffect.OnEditComplete) onNavigateUp()
            }
    }

    EditStoreScreen(
        uiState = uiState,
        onStoreNameChange = {
            viewModel.updateNickname(value = it)
        },
        onStoreIntroductionChange = { viewModel.updateUiState(description = it) },
        onStoreGenreChange = { viewModel.updateUiState(genres = it) },
        onGenreSearchTextChange = viewModel::updateGenreSearchText,
        onBackButtonClick = onNavigateUp,
        onPhotoChange = viewModel::updatePhoto,
        onNameValidityCheckClick = viewModel::checkNicknameDuplication,
        onProceedButtonClick = viewModel::saveEditedProfile,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditStoreScreen(
    uiState: EditStoreUiState,
    onStoreNameChange: (String) -> Unit,
    onStoreIntroductionChange: (String) -> Unit,
    onStoreGenreChange: (List<StoreEditGenre>) -> Unit,
    onGenreSearchTextChange: (String) -> Unit,
    onBackButtonClick: () -> Unit,
    onPhotoChange: (PhotoType, Uri?) -> Unit,
    onNameValidityCheckClick: () -> Unit,
    onProceedButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            NavigateUpTopBar(
                title = stringResource(store_edit_profile),
                onNavigateUp = onBackButtonClick,
            )
        },
        bottomBar = {
            EditStoreProceedButton(
                onClick = {
                    onProceedButtonClick()
                    focusManager.clearFocus()
                },
                enabled = uiState.submitEnabled,
            )
        },
        containerColor = NapzakMarketTheme.colors.white,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        when (uiState.loadState) {
            is UiState.Success -> {
                val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                val bottomSheetVisibility = remember { mutableStateOf(false) }

                with(uiState.storeDetail) {
                    SuccessScreen(
                        storeCover = coverUrl,
                        storeProfile = photoUrl,
                        storeName = nickname,
                        storeIntroduction = description,
                        storeGenres = preferredGenres,
                        nickNameValidationState = uiState.nickNameValidationState,
                        nickNameDuplicationState = uiState.nickNameDuplicationState,
                        nickNameCheckEnabled = uiState.checkNickNameEnabled,
                        onStoreNameChange = onStoreNameChange,
                        onStoreIntroductionChange = onStoreIntroductionChange,
                        onNameValidityCheckClick = onNameValidityCheckClick,
                        onGenreSelectButtonClick = { bottomSheetVisibility.value = true },
                        onPhotoChange = onPhotoChange,
                        modifier = modifier.padding(innerPadding),
                    )
                }

                if (bottomSheetVisibility.value) {
                    val selectedGenres = uiState.storeDetail.preferredGenres.map {
                        Genre(
                            genreId = it.genreId,
                            genreName = it.genreName,
                        )
                    }
                    GenreSearchBottomSheet(
                        initialSelectedGenreList = selectedGenres,
                        genreItems = uiState.searchedGenres,
                        sheetState = bottomSheetState,
                        onDismissRequest = {
                            bottomSheetVisibility.value = false
                        },
                        onTextChange = onGenreSearchTextChange,
                        onButtonClick = { genres ->
                            onStoreGenreChange(genres.map { genre ->
                                StoreEditGenre(
                                    genreId = genre.genreId,
                                    genreName = genre.genreName,
                                )
                            })
                            bottomSheetVisibility.value = false
                        },
                    )
                }
            }

            else -> {
                // TODO: 다양한 UiState에 대한 화면 처리
            }
        }
    }
}

@Composable
private fun SuccessScreen(
    storeCover: String,
    storeProfile: String,
    storeName: String,
    storeIntroduction: String,
    storeGenres: List<StoreEditGenre>,
    nickNameValidationState: NicknameValidationResult,
    nickNameDuplicationState: UiState<String>,
    nickNameCheckEnabled: Boolean,
    onStoreNameChange: (String) -> Unit,
    onStoreIntroductionChange: (String) -> Unit,
    onPhotoChange: (PhotoType, Uri?) -> Unit,
    onNameValidityCheckClick: () -> Unit,
    onGenreSelectButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        EditStorePhotoSection(
            storeCover = storeCover,
            storePhoto = storeProfile,
            onPhotoChange = onPhotoChange,
        )

        Spacer(Modifier.height(24.dp))

        EditStoreNickNameSection(
            marketName = storeName,
            onNameChange = {
                onStoreNameChange(it)
            },
            nickNameValidationState = nickNameValidationState,
            nickNameDuplicationState = nickNameDuplicationState,
            onNameValidityCheckClick = onNameValidityCheckClick,
            checkEnabled = nickNameCheckEnabled,
        )

        SectionDivider(Modifier.padding(top = 13.dp, bottom = 30.dp))

        EditStoreDescriptionSection(
            description = storeIntroduction,
            onDescriptionChange = onStoreIntroductionChange,
        )

        SectionDivider(Modifier.padding(vertical = 30.dp))

        EditStorePreferredGenreSection(
            genres = storeGenres.map { it.genreName },
            onGenreSelectButtonClick = onGenreSelectButtonClick,
        )

        Spacer(Modifier.height(18.dp))
    }
}

@Composable
private fun EditStoreProceedButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 13.dp),
    ) {
        NapzakButton(
            text = stringResource(store_edit_button_proceed),
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.align(Alignment.Center),
        )
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
        modifier = modifier,
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun EditStoreScreenPreview() {
    NapzakMarketTheme {
        var storeName by remember { mutableStateOf("") }
        var storeIntroduction by remember { mutableStateOf("") }
        val storeGenres by remember { mutableStateOf(emptyList<StoreEditGenre>()) }

        SuccessScreen(
            storeCover = "",
            storeProfile = "",
            storeName = storeName,
            storeIntroduction = storeIntroduction,
            storeGenres = storeGenres,
            onStoreNameChange = { storeName = it },
            onStoreIntroductionChange = { storeIntroduction = it },
            onNameValidityCheckClick = {},
            onGenreSelectButtonClick = {},
            onPhotoChange = { _, _ -> },
            nickNameCheckEnabled = true,
            nickNameValidationState = NicknameValidationResult.Uninitialized,
            nickNameDuplicationState = UiState.Empty,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
