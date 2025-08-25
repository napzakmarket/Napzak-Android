package com.napzak.market.store.edit_store

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.usecase.GetGenreNamesUseCase
import com.napzak.market.presigned_url.model.UploadImage
import com.napzak.market.presigned_url.model.UploadImage.ImageType
import com.napzak.market.presigned_url.type.PhotoType
import com.napzak.market.presigned_url.usecase.UploadImagesUseCase
import com.napzak.market.store.edit_store.state.EditStoreUiState
import com.napzak.market.store.model.NicknameValidationResult
import com.napzak.market.store.model.StoreEditGenre
import com.napzak.market.store.model.StoreEditProfile
import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.store.usecase.CheckNicknameDuplicationUseCase
import com.napzak.market.store.usecase.ValidateNicknameUseCase
import com.napzak.market.ui_util.getHttpExceptionMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
internal class EditStoreViewModel @Inject constructor(
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val storeRepository: StoreRepository,
    private val validateNicknameUseCase: ValidateNicknameUseCase,
    private val checkNicknameDuplicationUseCase: CheckNicknameDuplicationUseCase,
    private val getGenreNamesUseCase: GetGenreNamesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditStoreUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<EditStoreSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val _genreSearchText = MutableStateFlow("")

    init {
        val debounceDelay = 500L
        viewModelScope.launch {
            _genreSearchText.debounce(debounceDelay)
                .collectLatest { searchText ->
                    updateSearchedGenres(searchText)
                }
        }
    }

    private suspend fun updateSearchedGenres(searchText: String) {
        getGenreNamesUseCase(searchText)
            .onSuccess { genres ->
                updateUiState(searchedGenres = genres)
            }
            .onFailure {
                updateUiState(searchedGenres = emptyList())
            }
    }

    fun updateGenreSearchText(searchText: String) {
        _genreSearchText.update { searchText }
    }

    suspend fun getEditProfile() {
        storeRepository.fetchEditProfile()
            .onSuccess { storeDetail ->
                updateUiState(
                    loadState = UiState.Success(Unit),
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
            updateUiState(isUploading = true)
            updateProfileImageUrl()
            storeRepository.updateEditProfile(_uiState.value.storeDetail).getOrThrow()
        }.onSuccess {
            updateUiState(isUploading = false)
            _sideEffect.send(EditStoreSideEffect.OnEditComplete)
        }.onFailure {
            updateUiState(isUploading = false)
            Timber.tag("EditStoreViewModel").e(it) // TODO: 실패했을 경우에 대한 처리 논의
        }
    }

    private suspend fun updateProfileImageUrl() = with(_uiState.value) {
        buildList {
            if (isCoverUrlChanged) add(UploadImage(ImageType.COVER, storeDetail.coverUrl))
            if (isPhotoUrlChanged) add(UploadImage(ImageType.PROFILE, storeDetail.photoUrl))
        }.run {
            if (this.isNotEmpty()) {
                val imageUrls = uploadImagesUseCase(
                    images = this
                ).getOrThrow()

                updateUiState(
                    coverUrl = imageUrls[ImageType.COVER] ?: originalStoreDetail.coverUrl,
                    photoUrl = imageUrls[ImageType.PROFILE] ?: originalStoreDetail.photoUrl,
                )
            }
        }
    }

    fun checkNicknameDuplication() = viewModelScope.launch {
        checkNicknameDuplicationUseCase(_uiState.value.storeDetail.nickname)
            .onSuccess {
                updateUiState(
                    nickNameDuplicationState = UiState.Success("사용할 수 있는 이름이에요!")
                )
            }
            .onFailure { exception ->
                val errorMessage = exception.getHttpExceptionMessage() ?: ""
                updateUiState(
                    nickNameDuplicationState = UiState.Failure(errorMessage)
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

    fun updateNickname(value: String) {
        val nickname = value.take(NICKNAME_MAX_LENGTH)

        val nicknameValidationResult =
            if (value.length <= NICKNAME_MAX_LENGTH) validateNicknameUseCase(nickname)
            else validateNicknameUseCase(value)


        updateUiState(
            name = nickname,
            nickNameValidationState = nicknameValidationResult,
            nickNameDuplicationState = UiState.Empty
        )
    }

    fun updateUiState(
        loadState: UiState<Unit> = _uiState.value.loadState,
        isUploading: Boolean = _uiState.value.isUploading,
        nickNameValidationState: NicknameValidationResult = _uiState.value.nickNameValidationState,
        nickNameDuplicationState: UiState<String> = _uiState.value.nickNameDuplicationState,
        searchedGenres: List<Genre> = _uiState.value.searchedGenres,
        originalStoreDetail: StoreEditProfile = _uiState.value.originalStoreDetail,
        storeDetail: StoreEditProfile = _uiState.value.storeDetail
    ) {
        updateUiState(
            loadState = loadState,
            isUploading = isUploading,
            nickNameValidationState = nickNameValidationState,
            nickNameDuplicationState = nickNameDuplicationState,
            searchedGenres = searchedGenres,
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
        isUploading: Boolean = _uiState.value.isUploading,
        nickNameValidationState: NicknameValidationResult = _uiState.value.nickNameValidationState,
        nickNameDuplicationState: UiState<String> = _uiState.value.nickNameDuplicationState,
        searchedGenres: List<Genre> = _uiState.value.searchedGenres,
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
                isUploading = isUploading,
                nickNameValidationState = nickNameValidationState,
                nickNameDuplicationState = nickNameDuplicationState,
                searchedGenres = searchedGenres,
                originalStoreDetail = originalStoreDetail,
                storeDetail = currentState.storeDetail.copy(
                    coverUrl = newCoverUrl,
                    photoUrl = newPhotoUrl,
                    nickname = name,
                    description = description.take(DESCRIPTION_MAX_LENGTH),
                    preferredGenres = genres.take(GENRE_MAX_LENGTH)
                )
            )
        }
    }

    companion object {
        private const val NICKNAME_MAX_LENGTH = 20
        private const val DESCRIPTION_MAX_LENGTH = 200
        private const val GENRE_MAX_LENGTH = 7
    }
}