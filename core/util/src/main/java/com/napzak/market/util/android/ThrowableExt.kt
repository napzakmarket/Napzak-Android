package com.napzak.market.util.android

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException

fun Throwable.getHttpExceptionMessage(): String? = when (this) {
    is HttpException -> {
        val errorBody = response()?.errorBody()?.string()

        if (!errorBody.isNullOrBlank()) {
            runCatching {
                Json.decodeFromString<ErrorMessage>(errorBody).message
            }.fold(
                onSuccess = { it },
                onFailure = { null },
            )
        } else null
    }

    else -> null
}

@Serializable
private data class ErrorMessage(
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String,
)
