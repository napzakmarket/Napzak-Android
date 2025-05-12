package com.napzak.market.login.model

data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val route: LoginFlowRoute? = null,
)

sealed class LoginFlowRoute {
    object Terms : LoginFlowRoute()
    object Main : LoginFlowRoute()
}