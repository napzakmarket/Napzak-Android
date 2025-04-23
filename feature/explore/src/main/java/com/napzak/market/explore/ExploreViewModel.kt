package com.napzak.market.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.explore.state.ExploreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class ExploreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val searchTerm: String? = savedStateHandle.get<String>(SEARCH_TERM_KEY)

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState = _uiState.asStateFlow()

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

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val SEARCH_TERM_KEY = "searchTerm"
    }
}