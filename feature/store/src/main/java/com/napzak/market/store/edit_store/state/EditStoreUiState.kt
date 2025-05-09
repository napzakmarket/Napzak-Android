package com.napzak.market.store.edit_store.state

import com.napzak.market.common.state.UiState
import com.napzak.market.store.model.StoreEditProfile

internal data class EditStoreUiState(
    val loadState: UiState<Unit> = UiState.Loading,
    val nickNameValidState: UiState<String> = UiState.Empty,
    val storeDetail: StoreEditProfile = EmptyStoreDetail,
) {
    companion object {
        val EmptyStoreDetail = StoreEditProfile(
            coverUrl = "",
            photoUrl = "",
            nickname = "",
            description = "",
            preferredGenres = emptyList()
        )
    }
}
