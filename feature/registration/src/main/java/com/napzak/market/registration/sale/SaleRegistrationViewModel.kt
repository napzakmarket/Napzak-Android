package com.napzak.market.registration.sale

import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.ProductConditionType
import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.usecase.GetProductPresignedUrlUseCase
import com.napzak.market.presigned_url.usecase.UploadImageUseCase
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect.NavigateToDetail
import com.napzak.market.registration.RegistrationViewModel
import com.napzak.market.registration.event.GenreEventBus
import com.napzak.market.registration.model.ProductImage
import com.napzak.market.registration.model.SaleRegistrationProduct
import com.napzak.market.registration.sale.state.SaleContract.SaleUiState
import com.napzak.market.registration.usecase.RegisterProductUseCase
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
    // TODO: 등록 수정 Use Case
    getProductPresignedUrlUseCase: GetProductPresignedUrlUseCase,
    uploadImageUseCase: UploadImageUseCase,
    private val registerProductUseCase: RegisterProductUseCase,
) : RegistrationViewModel(
    getProductPresignedUrlUseCase,
    uploadImageUseCase,
) {
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
            imageUrls = presignedUrls.map {
                ProductImage(
                    imageUrl = it.url.substringBefore(VALUE_DELIMITER),
                    sequence = it.imageName.substringAfter(KEY_DELIMITER).toInt(),
                )
            },
            genreId = registrationState.genre?.genreId ?: 0L,
            genreName = registrationState.genre?.genreName.orEmpty(),
            title = registrationState.title,
            description = registrationState.description,
            price = registrationState.price.toInt(),
            productCondition = saleState.condition?.label,
            isDeliveryIncluded = saleState.isShippingFeeIncluded == true,
            standardDeliveryFee = saleState.normalShippingFee.toInt(),
            halfDeliveryFee = saleState.halfShippingFee.toInt(),
        )
        registerProductUseCase(product).onSuccess { productId ->
            updateLoadState(UiState.Success(Unit))
            _sideEffect.emit(NavigateToDetail(productId))
        }.onFailure {
            updateLoadState(UiState.Failure(UPLOADING_PRODUCT_ERROR_MESSAGE))
        }
    }
}
