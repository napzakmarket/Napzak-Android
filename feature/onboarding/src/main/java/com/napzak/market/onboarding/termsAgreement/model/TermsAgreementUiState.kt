package com.napzak.market.onboarding.termsAgreement.model

import com.napzak.market.store.model.Terms

data class TermsAgreementUiState(
    val agreedTerms: Set<TermType> = emptySet(),
    val terms: List<Terms> = emptyList(),
    val bundleId: Int? = null,
) {
    val isAllAgreed: Boolean
        get() = TermType.entries
            .filter { it.isRequired }
            .all { agreedTerms.contains(it) }

    fun isChecked(term: TermType): Boolean = agreedTerms.contains(term)
}