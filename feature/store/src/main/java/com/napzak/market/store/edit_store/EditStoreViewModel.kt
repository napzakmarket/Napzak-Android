package com.napzak.market.store.edit_store

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private var originalStoreDetail by mutableStateOf(
        StoreEditProfile(
            coverUrl = "",
            photoUrl = "",
            nickname = "",
            description = "",
            preferredGenres = emptyList()
        )
    )

    suspend fun getEditProfile() {
        storeRepository.fetchEditProfile()
            .onSuccess {
                _uiState.update { currentState ->
                    originalStoreDetail = it
                    currentState.copy(
                        loadState = UiState.Success(Unit),
                        storeDetail = it
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(loadState = UiState.Failure(it.toString()))
                }
            }
    }

    fun checkNicknameValidity() = viewModelScope.launch {
        storeRepository.getValidateNickname(_uiState.value.storeDetail.nickname)
            .onFailure { exception ->
                UiState.Failure(exception.getHttpExceptionMessage() ?: "")
            }
    }

    fun saveEditedProfile() = viewModelScope.launch {
        runCatching {
            val s3Urls = getS3Urls()
            updateEditProfile(coverUrl = s3Urls.first, photoUrl = s3Urls.second)
        }.onSuccess {
            // TODO: 이전 화면으로 이동
        }.onFailure {
            Timber.tag("EditStoreViewModel").e(it)
        } // TODO: 실패했을 경우에 대한 처리 논의
    }

    private suspend fun getS3Urls(): Pair<String?, String?> = uploadStorePhotoUseCase(
        coverPhoto = _uiState.value.storeDetail.coverUrl.takeIf { it != originalStoreDetail.coverUrl },
        profilePhoto = _uiState.value.storeDetail.photoUrl.takeIf { it != originalStoreDetail.photoUrl },
    ).fold(
        onSuccess = { s3Urls ->
            val coverUrl = s3Urls[UploadStorePhotoUseCase.COVER_IMAGE_TITLE]
            val profileUrl = s3Urls[UploadStorePhotoUseCase.PROFILE_IMAGE_TITLE]
            coverUrl to profileUrl
        },
        onFailure = { throw it }
    )

    private suspend fun updateEditProfile(coverUrl: String?, photoUrl: String?) {
        updateStoreDetail(
            coverUrl = coverUrl?.takeIf { it.isNotBlank() } ?: _uiState.value.storeDetail.coverUrl,
            photoUrl = photoUrl?.takeIf { it.isNotBlank() } ?: _uiState.value.storeDetail.photoUrl
        )

        storeRepository.updateEditProfile(_uiState.value.storeDetail).onFailure {
            throw it
        }
    }

    fun updateStoreDetail(
        coverUrl: String = _uiState.value.storeDetail.coverUrl,
        photoUrl: String = _uiState.value.storeDetail.photoUrl,
        name: String = _uiState.value.storeDetail.nickname,
        description: String = _uiState.value.storeDetail.description,
        genres: List<StoreEditGenre> = _uiState.value.storeDetail.preferredGenres
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                storeDetail = currentState.storeDetail.copy(
                    coverUrl = coverUrl,
                    photoUrl = photoUrl,
                    nickname = name,
                    description = description,
                    preferredGenres = genres
                )
            )
        }
    }

    fun updatePhoto(photoType: PhotoType, uri: Uri?) {
        if (uri != null) {
            val uriString = uri.toString()
            when (photoType) {
                PhotoType.COVER -> {
                    updateStoreDetail(coverUrl = uriString)
                }

                PhotoType.PROFILE -> {
                    updateStoreDetail(photoUrl = uriString)
                }
            }
        }
    }
}