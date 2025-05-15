package com.napzak.market.store.edit_store.state

import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.store.model.NicknameValidationResult
import com.napzak.market.store.model.StoreEditProfile

internal data class EditStoreUiState(
    val loadState: UiState<Unit> = UiState.Loading,
    val nickNameValidationState: NicknameValidationResult = NicknameValidationResult.Empty,
    val nickNameDuplicationState: NicknameValidationResult = NicknameValidationResult.Empty,
    val searchedGenres: List<Genre> = emptyList(),
    val originalStoreDetail: StoreEditProfile = EmptyStoreDetail,
    val storeDetail: StoreEditProfile = EmptyStoreDetail,
) {
    val isNameChanged get() = storeDetail.nickname != originalStoreDetail.nickname
    val isDescriptionChanged get() = storeDetail.description != originalStoreDetail.description
    val isGenresChanged get() = storeDetail.preferredGenres != originalStoreDetail.preferredGenres
    val isCoverUrlChanged get() = storeDetail.coverUrl != originalStoreDetail.coverUrl
    val isPhotoUrlChanged get() = storeDetail.photoUrl != originalStoreDetail.photoUrl

    val checkNickNameEnabled
        get() = nickNameValidationState is NicknameValidationResult.Valid && isNameChanged

    val submitEnabled
        get() = (isNameChanged && nickNameDuplicationState is NicknameValidationResult.Valid)
                || (!isNameChanged && (isDescriptionChanged || isGenresChanged || isCoverUrlChanged || isPhotoUrlChanged))


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
