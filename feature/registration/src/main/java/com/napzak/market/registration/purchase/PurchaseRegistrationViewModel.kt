package com.napzak.market.registration.purchase

import androidx.lifecycle.viewModelScope
import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.usecase.GetProductPresignedUrlUseCase
import com.napzak.market.presigned_url.usecase.UploadImageUseCase
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect
import com.napzak.market.registration.RegistrationViewModel
import com.napzak.market.registration.event.GenreEventBus
import com.napzak.market.registration.purchase.state.PurchaseContract.PurchaseUiState
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
    // TODO: Use Case 구현 필요
    getProductPresignedUrlUseCase: GetProductPresignedUrlUseCase,
    uploadImageUseCase: UploadImageUseCase,
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

    }
}
