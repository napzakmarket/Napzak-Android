package com.napzak.market.remote.socket.type

enum class StompHeaderType(val value: String) {
    DESTINATION("destination"),
    ID("id"),
    HOST("host"),
    CONTENT_LENGTH("content-length"),
    CONTENT_TYPE("content-type"),
}