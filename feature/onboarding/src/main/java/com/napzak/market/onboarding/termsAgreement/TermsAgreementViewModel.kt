package com.napzak.market.onboarding.termsAgreement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.onboarding.termsAgreement.model.TermType
import com.napzak.market.onboarding.termsAgreement.model.TermsAgreementUiState
import com.napzak.market.store.usecase.CheckTermsAgreementUseCase
import com.napzak.market.store.usecase.SetTermsAgreementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsAgreementViewModel
@Inject constructor(
    private val checkTermsAgreementUseCase: CheckTermsAgreementUseCase,
    private val setTermsAgreementUseCase: SetTermsAgreementUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TermsAgreementUiState())
    val uiState: StateFlow<TermsAgreementUiState> = _uiState.asStateFlow()

    fun checkTermsAgreement() {
        viewModelScope.launch {
            checkTermsAgreementUseCase()
                .onSuccess { agreement ->
                    _uiState.update {
                        it.copy(
                            terms = agreement.termList,
                            bundleId = agreement.bundleId,
                        )
                    }
                }
                .onFailure {
                    // TODO: 추후 구현
                }
        }
    }

    fun onAllAgreementClick() {
        _uiState.update {
            it.copy(
                agreedTerms = if (uiState.value.isAllAgreed) {
                    emptySet()
                } else {
                    TermType.entries.toSet()
                }
            )
        }
    }

    fun onAgreementClick(term: TermType) {
        _uiState.update { current ->
            current.copy(agreedTerms = current.agreedTerms.toMutableSet().apply {
                if (contains(term)) remove(term) else add(term)
            })
        }
    }

    fun updateTermsAgreement(
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        val bundleId = uiState.value.bundleId ?: return

        viewModelScope.launch {
            setTermsAgreementUseCase(bundleId)
                .onSuccess { onSuccess() }
                .onFailure { onFailure() }
        }
    }
}
