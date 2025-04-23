package com.napzak.market.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.TradeType
import com.napzak.market.explore.model.Product
import com.napzak.market.explore.state.ExploreProducts
import com.napzak.market.explore.state.ExploreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ExploreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val searchTerm: String? = savedStateHandle.get<String>(SEARCH_TERM_KEY)

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState = _uiState.asStateFlow()

    fun updateExploreInformation() = viewModelScope.launch{
        with(uiState.value) {
            // TODO : 추후 API로 변경
            updateLoadState(
                UiState.Success(
                    ExploreProducts(productList = Product.mockMixedProduct)
                )
            )
        }
    }

    fun updateTradeType(newTradeType: TradeType) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedTab = newTradeType,
            )
        }
    }

    // genre필터 적용

    fun updateUnopenFilter() {
        _uiState.update { currentState ->
            currentState.copy(
                isUnopenSelected = !_uiState.value.isUnopenSelected,
            )
        }
    }

    fun updateSoldOutFilter() {
        _uiState.update { currentState ->
            currentState.copy(
                isSoldOutSelected = !_uiState.value.isSoldOutSelected,
            )
        }
    }

    // 좋아요 버튼 기능

    private fun updateLoadState(loadState: UiState<ExploreProducts>) =
        _uiState.update { currentState ->
            currentState.copy(
                loadState = loadState
            )
        }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val SEARCH_TERM_KEY = "searchTerm"
    }
}