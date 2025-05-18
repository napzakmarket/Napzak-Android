package com.napzak.market.store.model

data class KakaoLogin(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String?,
    val role: String,
)

enum class UserRole {
    ONBOARDING, STORE, WITHDRAWN;

    companion object {
        fun from(value: String): UserRole {
            return when (value) {
                "ONBOARDING" -> ONBOARDING
                "STORE" -> STORE
                "WITHDRAWN" ->WITHDRAWN
                else -> throw IllegalArgumentException("Unknown role: $value")
            }
        }
    }
}