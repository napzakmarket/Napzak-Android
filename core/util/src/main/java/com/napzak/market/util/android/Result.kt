package com.napzak.market.util.android

import kotlin.coroutines.cancellation.CancellationException

suspend fun <R> suspendRunCatching(block: suspend () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Exception) {
        return Result.failure(e)
    }
}
