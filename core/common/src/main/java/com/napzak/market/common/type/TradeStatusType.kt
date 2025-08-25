package com.napzak.market.common.type

enum class TradeStatusType(
    val label: String,
) {
    BEFORE_TRADE_SELL(label = "판매중"),
    BEFORE_TRADE_BUY(label = "구매중"),
    COMPLETED_SELL(label = "판매 완료"),
    COMPLETED_BUY(label = "구매 완료"),
    RESERVED(label = "예약중");

    val typeName
        get() = when {
            name.startsWith("COMPLETED") -> "COMPLETED"
            name.startsWith("BEFORE_TRADE") -> "BEFORE_TRADE"
            else -> name
        }

    companion object {
        fun get(name: String, tradeType: TradeType): TradeStatusType {
            return try {
                val upperCaseName = name.uppercase()
                when {
                    upperCaseName == "BEFORE_TRADE" && tradeType == TradeType.SELL -> BEFORE_TRADE_SELL
                    upperCaseName == "BEFORE_TRADE" && tradeType == TradeType.BUY -> BEFORE_TRADE_BUY
                    upperCaseName == "COMPLETED" && tradeType == TradeType.SELL -> COMPLETED_SELL
                    upperCaseName == "COMPLETED" && tradeType == TradeType.BUY -> COMPLETED_BUY
                    else -> valueOf(upperCaseName)
                }
            } catch (e: Exception) {
                RESERVED
            }
        }
    }
}