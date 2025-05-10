package com.napzak.market.registration.sale

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.ProductConditionType
import com.napzak.market.common.type.ProductConditionType.Companion.fromConditionByName
import com.napzak.market.genre.model.Genre
import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.usecase.GetProductPresignedUrlUseCase
import com.napzak.market.presigned_url.usecase.UploadImageUseCase
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect.NavigateToDetail
import com.napzak.market.registration.RegistrationViewModel
import com.napzak.market.registration.event.GenreEventBus
import com.napzak.market.registration.model.Photo
import com.napzak.market.registration.model.ProductImage
import com.napzak.market.registration.model.SaleRegistrationProduct
import com.napzak.market.registration.sale.state.SaleContract.SaleUiState
import com.napzak.market.registration.usecase.EditRegisteredProductUseCase
import com.napzak.market.registration.usecase.GetRegisteredSaleProductUseCase
import com.napzak.market.registration.usecase.RegisterProductUseCase
import com.napzak.market.util.android.priceToNumericTransformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleRegistrationViewModel @Inject constructor(
    getProductPresignedUrlUseCase: GetProductPresignedUrlUseCase,
    uploadImageUseCase: UploadImageUseCase,
    savedStateHandle: SavedStateHandle,
    private val registerProductUseCase: RegisterProductUseCase,
    private val getRegisteredSaleProductUseCase: GetRegisteredSaleProductUseCase,
    private val editRegisteredProductUseCase: EditRegisteredProductUseCase,
) : RegistrationViewModel(
    getProductPresignedUrlUseCase,
    uploadImageUseCase,
) {
    private val productId: Long? = savedStateHandle.get<Long>(PRODUCT_ID_KEY)

    private val _uiState = MutableStateFlow(SaleUiState())
    val saleUiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<RegistrationSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            GenreEventBus.genreSelected.collect { genre ->
                updateGenre(genre)
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
                || (saleState.isNormalShippingChecked && saleState.normalShippingFee.isEmpty()) || (saleState.isHalfShippingChecked && saleState.halfShippingFee.isEmpty()))
    }

    override fun uploadProduct(presignedUrls: List<PresignedUrl>) = viewModelScope.launch {
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
            editRegisteredProductUseCase(id, product).onSuccess {
                updateLoadState(UiState.Success(Unit))
                _sideEffect.emit(NavigateToDetail(id))
            }.onFailure {
                updateLoadState(UiState.Failure(UPLOADING_PRODUCT_ERROR_MESSAGE))
            }
        } ?: run {
            registerProductUseCase(product).onSuccess { productId ->
                updateLoadState(UiState.Success(Unit))
                _sideEffect.emit(NavigateToDetail(productId))
            }.onFailure {
                updateLoadState(UiState.Failure(UPLOADING_PRODUCT_ERROR_MESSAGE))
            }
        }
    }

    fun getRegisteredSaleProduct() = viewModelScope.launch {
        productId?.let { id ->
            updateLoadState(UiState.Loading)

            getRegisteredSaleProductUseCase(id).onSuccess { product ->
                _uiState.update {
                    it.copy(
                        condition = fromConditionByName(product.productCondition),
                        isShippingFeeIncluded = product.isDeliveryIncluded,
                        normalShippingFee = product.standardDeliveryFee.toString(),
                        halfShippingFee = product.halfDeliveryFee.toString(),
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
}
