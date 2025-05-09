package com.napzak.market.store.edit_store.state

import com.napzak.market.common.state.UiState
import com.napzak.market.store.model.StoreEditProfile

internal data class EditStoreUiState(
    val loadState: UiState<Unit> = UiState.Loading,
    val nickNameValidState: UiState<String> = UiState.Empty,
    val originalStoreDetail: StoreEditProfile = EmptyStoreDetail,
    val storeDetail: StoreEditProfile = EmptyStoreDetail,
) {
    val isNameChanged get() = storeDetail.nickname != originalStoreDetail.nickname
    val isDescriptionChanged get() = storeDetail.description != originalStoreDetail.description
    val isGenresChanged get() = storeDetail.preferredGenres != originalStoreDetail.preferredGenres
    val isCoverUrlChanged get() = storeDetail.coverUrl != originalStoreDetail.coverUrl
    val isPhotoUrlChanged get() = storeDetail.photoUrl != originalStoreDetail.photoUrl

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
