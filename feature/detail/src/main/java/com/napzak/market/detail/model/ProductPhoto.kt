package com.napzak.market.detail.model

// TODO: 도메인으로 이동
data class ProductPhoto(
    val photoId: Long,
    val photoUrl: String,
    val photoSequence: Int,
) {
    companion object {
        val mockList = buildList {
            repeat(3) { time ->
                add(
                    ProductPhoto(
                        photoId = time.toLong(),
                        photoUrl = "",
                        photoSequence = time,
                    )
                )
            }
        }
    }
}