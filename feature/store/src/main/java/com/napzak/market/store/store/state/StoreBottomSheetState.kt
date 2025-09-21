package com.napzak.market.store.store.state

data class StoreBottomSheetState(
    val isSortBottomSheetVisible: Boolean = false,
    val isGenreSearchingBottomSheetVisible: Boolean = false,
    val isStoreReportBottomSheetVisible: Boolean = false,
    val isStoreBlockDialogVisible: Boolean = false,
)
