package com.napzak.market.registration.purchase

import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.usecase.GetProductPresignedUrlUseCase
import com.napzak.market.presigned_url.usecase.UploadImageUseCase
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect.NavigateToDetail
import com.napzak.market.registration.RegistrationViewModel
import com.napzak.market.registration.event.GenreEventBus
import com.napzak.market.registration.model.ProductImage
import com.napzak.market.registration.model.PurchaseRegistrationProduct
import com.napzak.market.registration.model.SaleRegistrationProduct
import com.napzak.market.registration.purchase.state.PurchaseContract.PurchaseUiState
import com.napzak.market.registration.usecase.RegisterProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseRegistrationViewModel @Inject constructor(
    // TODO: 등록 수정 Use Case
    getProductPresignedUrlUseCase: GetProductPresignedUrlUseCase,
    uploadImageUseCase: UploadImageUseCase,
    private val registerProductUseCase: RegisterProductUseCase,
) : RegistrationViewModel(
    getProductPresignedUrlUseCase,
    uploadImageUseCase,
) {
    private val _uiState = MutableStateFlow(PurchaseUiState())
    val purchaseUiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<RegistrationSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            GenreEventBus.genreSelected.collect { genre ->
                updateGenre(genre)
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

    override fun uploadProduct(presignedUrls: List<PresignedUrl>) = viewModelScope.launch {
        val purchaseState = _uiState.value
        val registrationState = registrationUiState.value
        val product = PurchaseRegistrationProduct(
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
            isPriceNegotiable = purchaseState.isNegotiable,
        )
        registerProductUseCase(product).onSuccess { productId ->
            updateLoadState(UiState.Success(Unit))
            _sideEffect.emit(NavigateToDetail(productId))
        }.onFailure {
            updateLoadState(UiState.Failure(UPLOADING_PRODUCT_ERROR_MESSAGE))
        }
    }
}
