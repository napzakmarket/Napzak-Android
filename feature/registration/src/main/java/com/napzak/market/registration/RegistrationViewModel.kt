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
        updateLoadState(UiState.Loading)
        val imageUris = _uiState.value.imageUris
        val localImages = imageUris.mapIndexedNotNull { index, photo ->
            if (!photo.uri.toString().startsWith(REMOTE_URL_KEY)) {
                index to photo.uri.toString()
            } else null
        }

        if (localImages.isEmpty()) {
            val remoteImages = imageUris.mapIndexed { index, photo ->
                PresignedUrl(
                    imageName = "${KEY_DELIMITER}${index + 1}",
                    url = photo.uri.toString(),
                )
            }
            uploadProduct(remoteImages)
            return@launch
        }

        getProductPresignedUrlUseCase(localImages.map { it.second }).onSuccess { presignedUrls ->
            val originalIndexedPresignedUrls = presignedUrls.zip(localImages) { presignedUrl, (originalIndex, _) ->
                presignedUrl.copy(
                    imageName = "${KEY_DELIMITER}${originalIndex + 1}"
                )
            }
            uploadImageViaPresignedUrl(originalIndexedPresignedUrls)
        }.onFailure {
            updateLoadState(UiState.Failure(RETRIEVING_URL_ERROR_MESSAGE))
        }
    }

    private fun uploadImageViaPresignedUrl(presignedUrls: List<PresignedUrl>) = viewModelScope.launch {
        val imageUris = _uiState.value.imageUris

        val (remoteImages, localImages) = imageUris.mapIndexed { index, photo ->
            index to photo.uri.toString()
        }.partition { (_, uri) -> uri.startsWith(REMOTE_URL_KEY) }

        val localPresignedUrls = presignedUrls.zip(localImages) { presignedUrl, (_, uri) ->
            presignedUrl to uri
        }

        val remotePresignedUrls = remoteImages.map { (index, uri) ->
            PresignedUrl(
                imageName = "${KEY_DELIMITER}${index + 1}",
                url = uri
            )
        }

        runCatching {
            coroutineScope {
                val uploadResults = localPresignedUrls.map { (presignedUrl, photoUri) ->
                    async { uploadImageUseCase(presignedUrl.url, photoUri) }
                }.awaitAll()

                if (uploadResults.all { results -> results.isSuccess }) {
                    val sortedPresignedUrls = (presignedUrls + remotePresignedUrls).sortedBy { it.imageName.substringAfter(KEY_DELIMITER).toInt() }
                    uploadProduct(sortedPresignedUrls)
                } else {
                    updateLoadState(UiState.Failure(UPLOADING_PRODUCT_ERROR_MESSAGE))
                }
            }
        }
    }

    protected abstract fun uploadProduct(presignedUrls: List<PresignedUrl>): Job

    companion object {
        private const val REMOTE_URL_KEY = "https://"
        internal const val PRODUCT_ID_KEY = "productId"
        internal const val KEY_DELIMITER = "image_"
        internal const val VALUE_DELIMITER = "?"
        internal const val UPLOADING_PRODUCT_ERROR_MESSAGE = "failed to register product."
        private const val RETRIEVING_URL_ERROR_MESSAGE = "failed to retrieve URL."
    }
}
