package com.napzak.market.store.store.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.SortType
import com.napzak.market.designsystem.component.bottomsheet.GenreSearchBottomSheet
import com.napzak.market.designsystem.component.bottomsheet.SortBottomSheet
import com.napzak.market.designsystem.component.dialog.NapzakDialog
import com.napzak.market.designsystem.component.dialog.NapzakDialogDefault
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.string.store_block_dialog_cancel
import com.napzak.market.feature.store.R.string.store_block_dialog_confirm
import com.napzak.market.feature.store.R.string.store_block_dialog_content
import com.napzak.market.feature.store.R.string.store_block_dialog_title
import com.napzak.market.genre.model.Genre
import com.napzak.market.store.store.state.StoreBottomSheetState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StoreBottomSheetScreen(
    storeBottomSheetState: StoreBottomSheetState,
    sheetState: SheetState,
    selectedGenres: List<Genre>,
    genreItems: List<Genre>,
    sortType: SortType,
    isStoreBlocked: Boolean,
    onDismissRequest: (BottomSheetType) -> Unit,
    onSortItemClick: (SortType) -> Unit,
    onTextChange: (String) -> Unit,
    onGenreSelectButtonClick: (List<Genre>) -> Unit,
    onStoreReportButtonClick: () -> Unit,
    onStoreBlockButtonClick: () -> Unit,
    onStoreBlockConfirm: () -> Unit,
    onStoreBlockDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    with(storeBottomSheetState) {
        if (isGenreSearchingBottomSheetVisible) {
            GenreSearchBottomSheet(
                initialSelectedGenreList = selectedGenres,
                sheetState = sheetState,
                genreItems = genreItems,
                onDismissRequest = { onDismissRequest(BottomSheetType.GENRE_SEARCHING) },
                onTextChange = onTextChange,
                onButtonClick = onGenreSelectButtonClick,
            )
        }

        if (isSortBottomSheetVisible) {
            SortBottomSheet(
                selectedSortType = sortType,
                sheetState = sheetState,
                onDismissRequest = { onDismissRequest(BottomSheetType.SORT) },
                onSortItemClick = onSortItemClick,
            )
        }

        if (isStoreReportBottomSheetVisible) {
            StoreReportBottomSheet(
                sheetState = sheetState,
                isStoreBlocked = isStoreBlocked,
                onDismissRequest = { onDismissRequest(BottomSheetType.STORE_REPORT) },
                onReportButtonClick = onStoreReportButtonClick,
                onBlockButtonClick = onStoreBlockButtonClick,
                modifier = modifier,
            )
        }

        if (isStoreBlockDialogVisible) {
            NapzakDialog(
                title = stringResource(store_block_dialog_title),
                subTitle = stringResource(store_block_dialog_content),
                confirmText = stringResource(store_block_dialog_confirm),
                dismissText = stringResource(store_block_dialog_cancel),
                onConfirmClick = onStoreBlockConfirm,
                onDismissClick = onStoreBlockDismiss,
                dialogColor = NapzakDialogDefault.color.copy(
                    titleColor = NapzakMarketTheme.colors.black,
                ),
            )
        }
    }
}