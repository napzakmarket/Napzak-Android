package com.napzak.market.onboarding.termsAgreement.model

data class TermsAgreementUiState(
    val agreedTerms: Set<TermType> = emptySet(),
) {
    val isAllAgreed: Boolean
        get() = TermType.entries
            .filter { it.isRequired }
            .all { agreedTerms.contains(it) }

    fun isChecked(term: TermType): Boolean = agreedTerms.contains(term)
}