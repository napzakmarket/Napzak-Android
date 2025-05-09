package com.napzak.market.store.edit_store

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private var originalStoreDetail by mutableStateOf(
        StoreEditProfile(
            coverUrl = "",
            photoUrl = "",
            nickname = "",
            description = "",
            preferredGenres = emptyList(),
        )
    )

    suspend fun getEditProfile() {
        storeRepository.fetchEditProfile()
            .onSuccess {
                originalStoreDetail = it
                updateUiState(
                    loadState = UiState.Success(Unit),
                    coverUrl = it.coverUrl,
                    photoUrl = it.photoUrl,
                    name = it.nickname,
                    description = it.description,
                    genres = it.preferredGenres,
                )
            }.onFailure {
                updateUiState(
                    loadState = UiState.Failure(it.toString()),
                )
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
            val (coverUrl, photoUrl) = getPresignedUrl()

            val newCoverUrl = coverUrl.takeIf { it.isNotBlank() } ?: originalStoreDetail.coverUrl
            val newPhotoUrl = photoUrl.takeIf { it.isNotBlank() } ?: originalStoreDetail.photoUrl
            updateUiState(coverUrl = newCoverUrl, photoUrl = newPhotoUrl)

            storeRepository.updateEditProfile(_uiState.value.storeDetail).getOrThrow()
        }.onSuccess {
            getEditProfile() // TODO: 이전 화면으로 이동
        }.onFailure {
            Timber.tag("EditStoreViewModel").e(it) // TODO: 실패했을 경우에 대한 처리 논의
        }
    }

    private suspend fun getPresignedUrl(): Pair<String, String> {
        val presignedUrls = uploadStorePhotoUseCase(
            coverPhoto = _uiState.value.storeDetail.coverUrl.takeIf { it != originalStoreDetail.coverUrl },
            profilePhoto = _uiState.value.storeDetail.photoUrl.takeIf { it != originalStoreDetail.photoUrl },
        ).getOrThrow()

        val coverUrl = presignedUrls[PhotoType.COVER] ?: ""
        val profileUrl = presignedUrls[PhotoType.PROFILE] ?: ""
        return coverUrl to profileUrl
    }

    fun updateUiState(
        loadState: UiState<Unit> = _uiState.value.loadState,
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
}