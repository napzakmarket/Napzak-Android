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

suspend inline fun <T, R> Result<T>.suspendFlatMap(
    crossinline f: suspend (T) -> Result<R>
): Result<R> =
    fold(
        onSuccess = { t -> f(t) },
        onFailure = { e ->
            if (e is CancellationException) throw e
            else Result.failure(e)
        }
    )
