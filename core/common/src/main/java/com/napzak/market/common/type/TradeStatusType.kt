package com.napzak.market.common.type

enum class TradeStatusType(
    val label: String,
) {
    OnSale(
        label = ""
    ),
    Sold(
        label = "판매 완료"
    ),
    Bought(
        label = "구매 완료"
    ),
    Reserved(
        label = "예약중"
    ),
}