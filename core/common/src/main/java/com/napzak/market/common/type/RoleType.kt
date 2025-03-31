package com.napzak.market.common.type

enum class RoleType(
    val label: String,
) {
    STORE("상정"),
    ONBOARDING("온보딩"),
    REPORTED("신고 계정");

    companion object {
        fun get(name: String): RoleType = try {
            RoleType.valueOf(name.uppercase())
        } catch (e: IllegalArgumentException) {
            STORE
        }
    }
}