package com.napzak.market.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.usecase.GetProductPresignedUrlUseCase
import com.napzak.market.presigned_url.usecase.UploadImageUseCase
import com.napzak.market.registration.RegistrationContract.RegistrationUiState
import com.napzak.market.registration.model.Photo
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class RegistrationViewModel(
    // TODO: 등록 정보 수정 API 연결
    protected val getProductPresignedUrlUseCase: GetProductPresignedUrlUseCase,
    protected val uploadImageUseCase: UploadImageUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val registrationUiState = _uiState.asStateFlow()

    fun updatePhotos(newImageUrlList: List<Photo>) = _uiState.update { currentState ->
        currentState.copy(
            imageUris = (currentState.imageUris + newImageUrlList).toImmutableList()
        )
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
        val result = getProductPresignedUrlUseCase(_uiState.value.imageUris.map { it.uri.toString() })
        updateLoadState(UiState.Loading)

        result.onSuccess { presignedUrls ->
            uploadImageViaPresignedUrl(presignedUrls = presignedUrls)
        }.onFailure {
            updateLoadState(UiState.Failure(RETRIEVING_URL_ERROR_MESSAGE))
        }
    }

    private fun uploadImageViaPresignedUrl(presignedUrls: List<PresignedUrl>) =
        viewModelScope.launch {
            val imageUris = _uiState.value.imageUris

            val sortedPresignedUrls = presignedUrls.sortedBy {
                it.imageName.substringAfter(KEY_DELIMITER).toInt()
            }

            val productPhotos = sortedPresignedUrls.zip(imageUris) { presignedUrl, photo ->
                presignedUrl.url to photo.uri.toString()
            }

            runCatching {
                coroutineScope {
                    val uploadResults = productPhotos.map { (presignedUrl, imageUri) ->
                        async { uploadImageUseCase(presignedUrl, imageUri) }
                    }.awaitAll()

                    if (uploadResults.all { results -> results.isSuccess }) {
                        uploadProduct(sortedPresignedUrls)
                    } else {
                        updateLoadState(UiState.Failure(UPLOADING_PRODUCT_ERROR_MESSAGE))
                    }
                }
            }
        }

    fun editRegisteredProduct() {
        // TODO: API 연결
    }

    protected abstract fun uploadProduct(presignedUrls: List<PresignedUrl>): Job

    companion object {
        internal const val KEY_DELIMITER = "image_"
        internal const val VALUE_DELIMITER = "?"
        private const val UPLOADING_PRODUCT_ERROR_MESSAGE = "failed to register product."
        private const val RETRIEVING_URL_ERROR_MESSAGE = "failed to retrieve URL."
    }
}
