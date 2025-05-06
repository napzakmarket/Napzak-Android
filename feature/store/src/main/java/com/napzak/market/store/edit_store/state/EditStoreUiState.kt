package com.napzak.market.store.edit_store.state

import com.napzak.market.common.state.UiState
import com.napzak.market.store.model.StoreEditProfile

internal data class EditStoreUiState(
    val loadState: UiState<Unit> = UiState.Loading,
    val nickNameValidState: UiState<Unit> = UiState.Loading,
    val storeDetail: StoreEditProfile = StoreEditProfile(
        coverUrl = "",
        photoUrl = "",
        nickname = "",
        description = "",
        preferredGenres = emptyList()
    ),
)
