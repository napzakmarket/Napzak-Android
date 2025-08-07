package com.napzak.market.chat.model

sealed class ChatSocketException : Throwable() {
    class ConnectionFailureException(override val cause: Throwable) : ChatSocketException()
    class SendFailureException(override val cause: Throwable) : ChatSocketException()
    class SubscriptionFailureException(override val cause: Throwable) : ChatSocketException()
}