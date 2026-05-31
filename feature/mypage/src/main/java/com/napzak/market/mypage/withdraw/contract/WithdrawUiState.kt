package com.napzak.market.mypage.withdraw.contract

import com.napzak.market.mypage.withdraw.type.WithdrawReasonType

data class WithdrawUiState(
    val reason: WithdrawReasonType,
    val description: String,
    val isWithdrawing: Boolean,
) {
    companion object {
        val default = WithdrawUiState(
            reason = WithdrawReasonType.HARD_TO_FIND,
            description = "",
            isWithdrawing = false,
        )
    }
}
