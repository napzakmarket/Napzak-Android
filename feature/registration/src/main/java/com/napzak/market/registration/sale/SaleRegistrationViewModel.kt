package com.napzak.market.registration.sale

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.ProductConditionType
import com.napzak.market.common.type.ProductConditionType.Companion.fromConditionByName
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
import com.napzak.market.registration.model.SaleRegistrationProduct
import com.napzak.market.registration.sale.state.SaleContract.SaleUiState
import com.napzak.market.registration.usecase.EditRegisteredProductUseCase
import com.napzak.market.registration.usecase.GetRegisteredSaleProductUseCase
import com.napzak.market.registration.usecase.RegisterProductUseCase
import com.napzak.market.ui_util.priceToNumericTransformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleRegistrationViewModel @Inject constructor(
    getProductPresignedUrlUseCase: GetProductPresignedUrlUseCase,
    uploadImageUseCase: UploadImageUseCase,
    savedStateHandle: SavedStateHandle,
    compressImageUseCase: CompressImageUseCase,
    clearCacheUseCase: ClearCacheUseCase,
    private val registerProductUseCase: RegisterProductUseCase,
    private val getRegisteredSaleProductUseCase: GetRegisteredSaleProductUseCase,
    private val editRegisteredProductUseCase: EditRegisteredProductUseCase,
) : RegistrationViewModel(
    getProductPresignedUrlUseCase,
    uploadImageUseCase,
    compressImageUseCase,
    clearCacheUseCase,
) {
    private var productId: Long? = null

    private val _uiState = MutableStateFlow(SaleUiState())
    val saleUiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<Long?>(PRODUCT_ID_KEY, null)
                .filterNotNull()
                .take(1)
                .collect { id ->
                    productId = id
                    getRegisteredSaleProduct(id)
                }
        }
    }

    fun updateCondition(condition: ProductConditionType) = _uiState.update { currentState ->
        currentState.copy(condition = condition)
    }

    fun updateShippingFeeInclusion(isShippingFeeIncluded: Boolean) =
        _uiState.update { currentState ->
            currentState.copy(isShippingFeeIncluded = isShippingFeeIncluded)
        }

    fun updateNormalShippingFeeInclusion(isNormalShippingChecked: Boolean) =
        _uiState.update { currentState ->
            currentState.copy(isNormalShippingChecked = isNormalShippingChecked)
        }

    fun updateNormalShippingFee(normalShippingFee: String) = _uiState.update { currentState ->
        currentState.copy(normalShippingFee = normalShippingFee)
    }

    fun updateHalfShippingFeeInclusion(isExpressShippingChecked: Boolean) =
        _uiState.update { currentState ->
            currentState.copy(isHalfShippingChecked = isExpressShippingChecked)
        }

    fun updateHalfShippingFee(expressShippingFee: String) = _uiState.update { currentState ->
        currentState.copy(halfShippingFee = expressShippingFee)
    }

    fun updateButtonState(): Boolean {
        val registrationState = registrationUiState.value
        val saleState = _uiState.value

        return !(registrationState.imageUris.isEmpty() || registrationState.genre == null || registrationState.title.isEmpty()
                || registrationState.description.isEmpty() || registrationState.price.isEmpty() || saleState.condition == null
                || saleState.isShippingFeeIncluded == null || (saleState.isShippingFeeIncluded == false && (saleState.normalShippingFee.isEmpty() && saleState.halfShippingFee.isEmpty()))
                || (saleState.isNormalShippingChecked && saleState.normalShippingFee.priceToNumericTransformation() < 100) || (saleState.isHalfShippingChecked && saleState.halfShippingFee.isEmpty()))
    }

    override suspend fun uploadProduct(presignedUrls: List<PresignedUrl>): Result<Long> = runCatching {
        val saleState = _uiState.value
        val registrationState = registrationUiState.value
        val product = SaleRegistrationProduct(
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
            productCondition = saleState.condition?.label,
            isDeliveryIncluded = saleState.isShippingFeeIncluded == true,
            standardDeliveryFee = saleState.normalShippingFee.priceToNumericTransformation(),
            halfDeliveryFee = saleState.halfShippingFee.priceToNumericTransformation(),
        )

        productId?.let { id ->
            editRegisteredProductUseCase(id, product).getOrThrow()
            _sideEffect.emit(ShowToast(EDIT_SUCCESS))
            id
        } ?: run {
            registerProductUseCase(product).getOrThrow()
        }
    }

    fun getRegisteredSaleProduct(productId: Long) = viewModelScope.launch {
        updateLoadState(UiState.Loading)

        getRegisteredSaleProductUseCase(productId).onSuccess { product ->
            _uiState.update {
                it.copy(
                    condition = fromConditionByName(product.productCondition),
                    isShippingFeeIncluded = product.isDeliveryIncluded,
                    normalShippingFee = product.standardDeliveryFee.takeIf { it != 0 }?.toString() ?: "",
                    halfShippingFee = product.halfDeliveryFee.takeIf { it != 0 }?.toString() ?: "",
                )
            }
            if (product.standardDeliveryFee > 0) updateNormalShippingFeeInclusion(true)
            if (product.halfDeliveryFee > 0) updateHalfShippingFeeInclusion(true)

            updatePhotos(product.imageUrls.map {
                Photo(
                    uri = it.imageUrl.toUri(),
                    photoId = it.photoId,
                )
            })
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
