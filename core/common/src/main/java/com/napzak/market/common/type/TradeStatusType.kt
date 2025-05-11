package com.napzak.market.common.type

enum class TradeStatusType(
    val label: String,
) {
    BEFORE_TRADE(label = ""),
    COMPLETED_SELL(label = "판매 완료"),
    COMPLETED_BUY(label = "구매 완료"),
    RESERVED(label = "예약중");

    val typeName
        get() = if (name.startsWith("COMPLETED")) "COMPLETED"
        else name

    companion object {
        fun get(name: String, tradeType: TradeType): TradeStatusType {
            return try {
                val upperCaseName = name.uppercase()
                when {
                    upperCaseName == "COMPLETED" && tradeType == TradeType.SELL -> COMPLETED_SELL
                    upperCaseName == "COMPLETED" && tradeType == TradeType.BUY -> COMPLETED_BUY
                    else -> valueOf(upperCaseName)
                }
            } catch (e: Exception) {
                BEFORE_TRADE
            }
        }
    }
}