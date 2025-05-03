package com.napzak.market.store.model

data class StoreDetail(
    val storeId: Long,
    val nickname: String,
    val description: String,
    val photoUrl: String,
    val coverUrl: String,
    val isOwner: Boolean,
    val genrePreferences: List<StoreDetailGenre>,
) {
    // TODO: 삭제해야 함
    companion object {
        val mockStoreInfo = StoreDetail(
            storeId = 1,
            nickname = "납작핑",
            description = "잡덕입니다. 최애는 짱구, 철수, 흰둥이, 긴토키, 히지카타, 고죠 사토루 관련 상품 판매 및 구매 제시 채팅 언제든 환영합니다 :)",
            photoUrl = "",
            coverUrl = "",
            isOwner = true,
            genrePreferences = listOf(
                StoreDetailGenre(0, "산리오0"),
                StoreDetailGenre(1, "산리오1"),
                StoreDetailGenre(2, "산리오2"),
                StoreDetailGenre(3, "산리오3"),
                StoreDetailGenre(4, "산리오4"),
                StoreDetailGenre(5, "산리오5"),
                StoreDetailGenre(6, "산리오6"),
            ),
        )
    }
}

data class StoreDetailGenre(
    val genreId: Long,
    val genreName: String,
)