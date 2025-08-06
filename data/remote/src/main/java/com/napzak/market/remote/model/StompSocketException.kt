package com.napzak.market.remote.model

sealed class StompSocketException : Exception() {
    data class ConnectFailed(override val cause: Exception) : StompSocketException()
    data class HeartBeatFailed(override val cause: Exception) : StompSocketException()
}