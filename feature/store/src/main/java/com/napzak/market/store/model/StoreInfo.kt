package com.napzak.market.store.model

import com.napzak.market.designsystem.component.bottomsheet.Genre

data class StoreInfo(
    // TODO: 추후 domain으로 이동 예정
    val storeId: Long,
    val storeNickName: String,
    val storeDescription: String,
    val storePhoto: String,
    val storeCover: String,
    val isStoreOwner: Boolean,
    val genrePreferences: List<Genre>, // TODO: 추후 domain Genre로 변경
) {
    companion object {
        val mockStoreInfo = StoreInfo(
            storeId = 1,
            storeNickName = "납작핑",
            storeDescription = "잡덕입니다. 최애는 짱구, 철수, 흰둥이, 긴토키, 히지카타, 고죠 사토루 관련 상품 판매 및 구매 제시 채팅 언제든 환영합니다 :)",
            storePhoto = "",
            storeCover = "",
            isStoreOwner = true,
            genrePreferences = listOf(
                Genre(0, "산리오0"),
                Genre(1, "산리오1"),
                Genre(2, "산리오2"),
                Genre(3, "산리오3"),
                Genre(4, "산리오4"),
                Genre(5, "산리오5"),
                Genre(6, "산리오6"),
            )
        )
    }
}
