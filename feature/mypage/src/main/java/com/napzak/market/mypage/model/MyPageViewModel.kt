package com.napzak.market.mypage.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.mypage.state.MyPageUiState
import com.napzak.market.store.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        fetchStoreInfo()
    }

    private fun fetchStoreInfo() = viewModelScope.launch {
        storeRepository.fetchStoreInfo()
            .onSuccess { storeInfo ->
                _uiState.value = MyPageUiState(
                    nickname = storeInfo.storeNickname,
                    profileImageUrl = storeInfo.storePhoto,
                    salesCount = storeInfo.totalSellCount,
                    purchaseCount = storeInfo.totalBuyCount,
                )
            }
            .onFailure {}
    }
}