package com.napzak.market.registration.purchase

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.usecase.ClearCacheUseCase
import com.napzak.market.presigned_url.usecase.CompressImageUseCase
import com.napzak.market.presigned_url.usecase.GetProductPresignedUrlUseCase
import com.napzak.market.presigned_url.usecase.UploadImageUseCase
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect.ShowToast
import com.napzak.market.registration.RegistrationViewModel
import com.napzak.market.registration.model.Photo
import com.napzak.market.registration.model.ProductImage
import com.napzak.market.registration.model.PurchaseRegistrationProduct
import com.napzak.market.registration.purchase.state.PurchaseContract.PurchaseUiState
import com.napzak.market.registration.usecase.EditRegisteredProductUseCase
import com.napzak.market.registration.usecase.GetRegisteredPurchaseProductUseCase
import com.napzak.market.registration.usecase.RegisterProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseRegistrationViewModel @Inject constructor(
    getProductPresignedUrlUseCase: GetProductPresignedUrlUseCase,
    uploadImageUseCase: UploadImageUseCase,
    savedStateHandle: SavedStateHandle,
    compressImageUseCase: CompressImageUseCase,
    clearCacheUseCase: ClearCacheUseCase,
    private val registerProductUseCase: RegisterProductUseCase,
    private val getRegisteredPurchaseProductUseCase: GetRegisteredPurchaseProductUseCase,
    private val editRegisteredProductUseCase: EditRegisteredProductUseCase,
) : RegistrationViewModel(
    getProductPresignedUrlUseCase,
    uploadImageUseCase,
    compressImageUseCase,
    clearCacheUseCase,
) {
    private var productId: Long? = null

    private val _uiState = MutableStateFlow(PurchaseUiState())
    val purchaseUiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<Long?>(PRODUCT_ID_KEY, null)
                .filterNotNull()
                .take(1)
                .collect { id ->
                    productId = id
                    getRegisteredPurchaseProduct(id)
                }
        }
    }

    fun updateNegotiable(isNegotiable: Boolean) = _uiState.update { currentState ->
        currentState.copy(isNegotiable = isNegotiable)
    }

    fun updateButtonState(): Boolean = with(registrationUiState.value) {
        return !(imageUris.isEmpty() || genre == null || title.isEmpty() || description.isEmpty()
                || price.isEmpty() || price.toIntOrNull()?.rem(1000) != 0)
    }

    override suspend fun uploadProduct(presignedUrls: List<PresignedUrl>): Result<Long> = runCatching {
        val purchaseState = _uiState.value
        val registrationState = registrationUiState.value
        val product = PurchaseRegistrationProduct(
            imageUrls = presignedUrls.mapIndexed { index, presignedUrl ->
                ProductImage(
                    photoId = registrationState.imageUris[index].photoId,
                    imageUrl = presignedUrl.url.substringBefore(VALUE_DELIMITER),
                    sequence = presignedUrl.imageName.substringAfter(KEY_DELIMITER).toInt(),
                )
            },
            genreId = registrationState.genre?.genreId ?: 0L,
            genreName = registrationState.genre?.genreName.orEmpty(),
            title = registrationState.title,
            description = registrationState.description,
            price = registrationState.price.toInt(),
            isPriceNegotiable = purchaseState.isNegotiable,
        )

        productId?.let { id ->
            editRegisteredProductUseCase(id, product).getOrThrow()
            _sideEffect.emit(ShowToast(EDIT_SUCCESS))
            id
        } ?: run {
            registerProductUseCase(product).getOrThrow()
        }
    }

    fun getRegisteredPurchaseProduct(productId: Long) = viewModelScope.launch {
        updateLoadState(UiState.Loading)

        getRegisteredPurchaseProductUseCase(productId).onSuccess { product ->
            _uiState.update {
                it.copy(isNegotiable = product.isPriceNegotiable)
            }

            updatePhotos(product.imageUrls.map { Photo(it.imageUrl.toUri()) })
            updateGenre(Genre(product.genreId, product.genreName))
            updateTitle(product.title)
            updateDescription(product.description)
            updatePrice(product.price.toString())

            updateLoadState(UiState.Success(Unit))
        }.onFailure {
            updateLoadState(UiState.Failure(UPLOADING_PRODUCT_ERROR_MESSAGE))
        }
    }
}
