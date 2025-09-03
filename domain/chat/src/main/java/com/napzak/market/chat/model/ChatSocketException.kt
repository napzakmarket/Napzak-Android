package com.napzak.market.chat.model

sealed class ChatSocketException : Throwable() {
    class ConnectionFailureException(
        override val message: String? = "소켓이 연결되어있지 않습니다."
    ) : ChatSocketException()
    class SendFailureException(override val cause: Throwable) : ChatSocketException()
    class SubscriptionFailureException(override val cause: Throwable) : ChatSocketException()
}