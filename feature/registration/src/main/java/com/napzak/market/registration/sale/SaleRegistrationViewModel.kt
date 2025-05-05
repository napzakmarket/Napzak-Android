package com.napzak.market.registration.sale

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.napzak.market.common.type.ProductConditionType
import com.napzak.market.registration.sale.state.SaleContract.SaleSideEffect
import com.napzak.market.registration.sale.state.SaleContract.SaleUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SaleRegistrationViewModel @Inject constructor(
    // TODO: Use Case 구현 필요
) : ViewModel() {
    private val _uiState = MutableStateFlow(SaleUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SaleSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun updatePhotos(newImageUrlList: List<Uri>) = _uiState.update { currentState ->
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

    fun updateGenre(genre: String) = _uiState.update { currentState ->
        currentState.copy(genre = genre)
    }

    fun updateTitle(title: String) = _uiState.update { currentState ->
        currentState.copy(title = title)
    }

    fun updateDescription(description: String) = _uiState.update { currentState ->
        currentState.copy(description = description)
    }

    fun updateCondition(condition: ProductConditionType) = _uiState.update { currentState ->
        currentState.copy(condition = condition)
    }

    fun updatePrice(price: String) = _uiState.update { currentState ->
        currentState.copy(price = price)
    }

    fun updateShippingFeeInclusion(isShippingFeeIncluded: Boolean) = _uiState.update { currentState ->
        currentState.copy(isShippingFeeIncluded = isShippingFeeIncluded)
    }

    fun updateNormalShippingFeeInclusion(isNormalShippingChecked: Boolean) = _uiState.update { currentState ->
        currentState.copy(isNormalShippingChecked = isNormalShippingChecked)
    }

    fun updateNormalShippingFee(normalShippingFee: String) = _uiState.update { currentState ->
        currentState.copy(normalShippingFee = normalShippingFee)
    }

    fun updateHalfShippingFeeInclusion(isExpressShippingChecked: Boolean) = _uiState.update { currentState ->
        currentState.copy(isHalfShippingChecked = isExpressShippingChecked)
    }

    fun updateHalfShippingFee(expressShippingFee: String) = _uiState.update { currentState ->
        currentState.copy(halfShippingFee = expressShippingFee)
    }

    fun updateButtonState(): Boolean = with(_uiState.value) {
        return !(imageUris.isEmpty() || genre == null || title.isEmpty() || description.isEmpty()
                || condition == null || price.isEmpty() || isShippingFeeIncluded == null
                || (isShippingFeeIncluded == false && (normalShippingFee.isEmpty() && halfShippingFee.isEmpty()))
                || (isNormalShippingChecked && normalShippingFee.isEmpty()) || (isHalfShippingChecked && halfShippingFee.isEmpty()))
    }

    fun registerProduct() {
        // TODO: API 연결 필요
    }
}
