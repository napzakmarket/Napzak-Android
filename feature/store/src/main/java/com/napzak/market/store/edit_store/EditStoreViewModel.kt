package com.napzak.market.store.edit_store

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.presigned_url.type.PhotoType
import com.napzak.market.presigned_url.usecase.UploadStorePhotoUseCase
import com.napzak.market.store.edit_store.state.EditStoreUiState
import com.napzak.market.store.model.StoreEditGenre
import com.napzak.market.store.model.StoreEditProfile
import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.util.android.getHttpExceptionMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class EditStoreViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
    private val uploadStorePhotoUseCase: UploadStorePhotoUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditStoreUiState())
    val uiState = _uiState.asStateFlow()


    suspend fun getEditProfile() {
        storeRepository.fetchEditProfile()
            .onSuccess { storeDetail ->
                updateUiState(
                    loadState = UiState.Success(Unit),
                    nickNameValidState = UiState.Empty,
                    storeDetail = storeDetail,
                    originalStoreDetail = storeDetail,
                )
            }.onFailure {
                updateUiState(
                    loadState = UiState.Failure(it.toString()),
                )
            }
    }

    fun saveEditedProfile() = viewModelScope.launch {
        runCatching {
            getProfilePreSignedUrl()
            storeRepository.updateEditProfile(_uiState.value.storeDetail).getOrThrow()
        }.onSuccess {
            getEditProfile() // TODO: 이전 화면으로 이동
        }.onFailure {
            Timber.tag("EditStoreViewModel").e(it) // TODO: 실패했을 경우에 대한 처리 논의
        }
    }

    private suspend fun getProfilePreSignedUrl() = with(_uiState.value) {
        if (isCoverUrlChanged || isPhotoUrlChanged) {
            val presignedUrls = uploadStorePhotoUseCase(
                coverPhoto = storeDetail.coverUrl.takeIf { isCoverUrlChanged },
                profilePhoto = storeDetail.photoUrl.takeIf { isPhotoUrlChanged },
            ).getOrThrow()

            val coverUrl = presignedUrls[PhotoType.COVER] ?: originalStoreDetail.coverUrl
            val profileUrl = presignedUrls[PhotoType.PROFILE] ?: originalStoreDetail.photoUrl

            updateUiState(coverUrl = coverUrl, photoUrl = profileUrl)
        }
    }

    fun updatePhoto(photoType: PhotoType, uri: Uri?) {
        if (uri != null) {
            val uriString = uri.toString()
            when (photoType) {
                PhotoType.COVER -> {
                    updateUiState(coverUrl = uriString)
                }

                PhotoType.PROFILE -> {
                    updateUiState(photoUrl = uriString)
                }
            }
        }
    }

    fun checkNicknameValidity() = viewModelScope.launch {
        storeRepository.getValidateNickname(_uiState.value.storeDetail.nickname)
            .onSuccess {
                updateUiState(
                    nickNameValidState = UiState.Success("사용할 수 있는 이름이에요!")
                )
            }
            .onFailure { exception ->
                updateUiState(
                    nickNameValidState = UiState.Failure(
                        exception.getHttpExceptionMessage() ?: ""
                    )
                )
            }
    }

    fun checkSubmitButton(uiState: EditStoreUiState): Boolean = with(uiState) {
        nickNameValidState is UiState.Success
                || storeDetail.description != originalStoreDetail.description
                || storeDetail.preferredGenres != originalStoreDetail.preferredGenres
                || storeDetail.coverUrl != originalStoreDetail.coverUrl
                || storeDetail.photoUrl != originalStoreDetail.photoUrl
    }


    fun updateUiState(
        loadState: UiState<Unit> = _uiState.value.loadState,
        nickNameValidState: UiState<String> = _uiState.value.nickNameValidState,
        originalStoreDetail: StoreEditProfile = _uiState.value.originalStoreDetail,
        storeDetail: StoreEditProfile = _uiState.value.storeDetail
    ) {
        updateUiState(
            loadState = loadState,
            nickNameValidState = nickNameValidState,
            originalStoreDetail = originalStoreDetail,
            coverUrl = storeDetail.coverUrl,
            photoUrl = storeDetail.photoUrl,
            name = storeDetail.nickname,
            description = storeDetail.description,
            genres = storeDetail.preferredGenres
        )
    }

    fun updateUiState(
        loadState: UiState<Unit> = _uiState.value.loadState,
        nickNameValidState: UiState<String> = _uiState.value.nickNameValidState,
        originalStoreDetail: StoreEditProfile = _uiState.value.originalStoreDetail,
        coverUrl: String = _uiState.value.storeDetail.coverUrl,
        photoUrl: String = _uiState.value.storeDetail.photoUrl,
        name: String = _uiState.value.storeDetail.nickname,
        description: String = _uiState.value.storeDetail.description,
        genres: List<StoreEditGenre> = _uiState.value.storeDetail.preferredGenres
    ) {
        val newCoverUrl = coverUrl.toUri().buildUpon().clearQuery().build().toString()
        val newPhotoUrl = photoUrl.toUri().buildUpon().clearQuery().build().toString()

        _uiState.update { currentState ->
            currentState.copy(
                loadState = loadState,
                nickNameValidState = nickNameValidState,
                originalStoreDetail = originalStoreDetail,
                storeDetail = currentState.storeDetail.copy(
                    coverUrl = newCoverUrl,
                    photoUrl = newPhotoUrl,
                    nickname = name,
                    description = description,
                    preferredGenres = genres
                )
            )
        }
    }
}