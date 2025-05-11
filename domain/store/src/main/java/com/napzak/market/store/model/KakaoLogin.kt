package com.napzak.market.store.model

data class KakaoLogin(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String,
    val role: String,
)

enum class UserRole {
    STORE, PENDING;

    companion object {
        fun from(value: String): UserRole {
            return when (value) {
                "STORE" -> STORE
                "PENDING" -> PENDING
                else -> throw IllegalArgumentException("Unknown role: $value")
            }
        }
    }
}