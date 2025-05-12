package com.napzak.market.store.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginRequest(
    @SerialName("socialType")
    val socialType: String = "KAKAO",
)