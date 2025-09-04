package com.napzak.market.mypage.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.napzak.market.common.state.UiState
import com.napzak.market.mixpanel.MixpanelConstants.VIEWED_MYPAGE
import com.napzak.market.mypage.mypage.state.MyPageUiState
import com.napzak.market.store.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MyPageViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
    private val mixpanel: MixpanelAPI?,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    fun fetchStoreInfo() = viewModelScope.launch {
        _uiState.update { it.copy(loadState = UiState.Loading) }
        storeRepository.fetchStoreInfo()
            .onSuccess { storeInfo ->
                _uiState.update {
                    it.copy(
                        loadState = UiState.Success(Unit),
                        storeId = storeInfo.storeId,
                        nickname = storeInfo.nickname,
                        profileImageUrl = storeInfo.photoUrl,
                        salesCount = storeInfo.salesCount,
                        purchaseCount = storeInfo.purchaseCount,
                        serviceLink = storeInfo.serviceLink
                    )
                }
            }
            .onFailure { e ->
                _uiState.update { it.copy(loadState = UiState.Failure(e.message.toString())) }
                // TODO: 실패 시 사용자에게 메시지 표시 또는 로깅 처리
                // 예: _uiState.value = _uiState.value.copy(error = throwable.message)
                // Timber.e(throwable, "Store 정보 가져오기 실패")
            }
    }

    internal fun trackViewedMyPage() = mixpanel?.track(VIEWED_MYPAGE)
}