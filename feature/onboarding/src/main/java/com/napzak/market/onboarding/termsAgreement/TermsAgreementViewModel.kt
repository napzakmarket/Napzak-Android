package com.napzak.market.onboarding.termsAgreement

import androidx.lifecycle.ViewModel
import com.napzak.market.onboarding.termsAgreement.model.TermType
import com.napzak.market.onboarding.termsAgreement.model.TermsAgreementUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TermsAgreementViewModel
@Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TermsAgreementUiState())
    val uiState: StateFlow<TermsAgreementUiState> = _uiState.asStateFlow()

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
}
