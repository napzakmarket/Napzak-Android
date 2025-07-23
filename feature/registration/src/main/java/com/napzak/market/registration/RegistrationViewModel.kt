package com.napzak.market.registration

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.usecase.GetProductPresignedUrlUseCase
import com.napzak.market.presigned_url.usecase.UploadImageUseCase
import com.napzak.market.registration.RegistrationContract.RegistrationUiState
import com.napzak.market.registration.model.Photo
import com.napzak.market.registration.model.Photo.PhotoStatus
import com.napzak.market.registration.usecase.ClearCacheUseCase
import com.napzak.market.registration.usecase.CompressImageUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class RegistrationViewModel(
    protected val getProductPresignedUrlUseCase: GetProductPresignedUrlUseCase,
    protected val uploadImageUseCase: UploadImageUseCase,
    protected val compressImageUseCase: CompressImageUseCase,
    protected val clearCacheUseCase: ClearCacheUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val registrationUiState = _uiState.asStateFlow()

    fun updatePhotos(newPhotos: List<Photo>) = viewModelScope.launch {
        _uiState.update { currentState ->
            currentState.copy(
                imageUris = (currentState.imageUris + newPhotos).toImmutableList()
            )
        }

        newPhotos.forEach { newPhoto ->
            launch {
                val result = compressImageUseCase(newPhoto.uri.toString())
                val photoStatus = if (result.isSuccess) PhotoStatus.SUCCESS else PhotoStatus.ERROR
                val compressedUri = result.getOrNull()?.toUri()

                _uiState.update { currentState ->
                    currentState.copy(
                        imageUris = currentState.imageUris.map { originalPhoto ->
                            if (originalPhoto.uuid == newPhoto.uuid) {
                                originalPhoto.copy(
                                    compressedUri = compressedUri
                                        ?: originalPhoto.uri, // TODO: 이미지 압축 or 쿼리 실패 시 어떻게 처리할 지?
                                    status = photoStatus,
                                )
                            } else originalPhoto
                        }.toImmutableList()
                    )
                }
            }
        }
    }

    fun deletePhoto(photoIndex: Int) = _uiState.update { currentState ->
        currentState.copy(
            imageUris = currentState.imageUris.filterIndexed { index, _ ->
                index != photoIndex
            }.toImmutableList()
        )
    }

    fun updateRepresentPhoto(newPhoto: Int) = _uiState.update { currentState ->
        val newImageUrlList = currentState.imageUris.toMutableList()

        newImageUrlList.add(0, newImageUrlList.removeAt(newPhoto))
        currentState.copy(
            imageUris = newImageUrlList.toImmutableList()
        )
    }

    fun updateGenre(genre: Genre) = _uiState.update { currentState ->
        currentState.copy(genre = genre)
    }

    fun updateTitle(title: String) = _uiState.update { currentState ->
        currentState.copy(title = title)
    }

    fun updateDescription(description: String) = _uiState.update { currentState ->
        currentState.copy(description = description)
    }

    fun updatePrice(price: String) = _uiState.update { currentState ->
        currentState.copy(price = price)
    }

    internal fun updateLoadState(loadState: UiState<Unit>) = _uiState.update { currentState ->
        currentState.copy(loadState = loadState)
    }

    fun getPresignedUrl() = viewModelScope.launch {
        updateLoadState(UiState.Loading)

        getProductPresignedUrlUseCase(
            registrationUiState.value.imageUris.mapIndexed { index, photo ->
                photo.compressedUri.toString() to index
            }
        ).onSuccess { presignedUrls ->
            uploadImageViaPresignedUrl(presignedUrls)
        }.onFailure {
            updateLoadState(UiState.Failure(it.message ?: UNKNOWN_ERROR))
        }
    }

    private suspend fun uploadImageViaPresignedUrl(
        presignedUrls: List<PresignedUrl>,
    ) {
        val imageUris = registrationUiState.value.imageUris
        val indexedImages = imageUris.mapIndexed { index, photo ->
            index to photo.uri.toString()
        }

        uploadImageUseCase(presignedUrls, indexedImages)
            .onSuccess { newPresignedUrls ->
                uploadProduct(newPresignedUrls)
            }.onFailure {
                updateLoadState(UiState.Failure(it.message ?: UNKNOWN_ERROR))
            }
    }

    protected abstract fun uploadProduct(presignedUrls: List<PresignedUrl>): Job

    fun clearCachedImage() = viewModelScope.launch {
        clearCacheUseCase()
    }

    companion object {
        internal const val PRODUCT_ID_KEY = "productId"
        internal const val KEY_DELIMITER = "image_"
        internal const val VALUE_DELIMITER = "?"
        internal const val UPLOADING_PRODUCT_ERROR_MESSAGE = "failed to register product."
        internal const val UNKNOWN_ERROR = "unknown error."
    }
}
