package com.example.weatherapp2023.network.retry

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import okio.IOException

fun <T> Flow<T>.retryWithPolicy(
    retryPolicy: RetryPolicy
): Flow<T> {
    var currentDelay = retryPolicy.initialDelayMillis
    return retryWhen { cause, attempt ->
        if (cause is IOException && attempt < retryPolicy.maxRetries) {
            delay(currentDelay)
            currentDelay = retryPolicy.calculateBackoffDelayMillis(attempt.toInt())
            return@retryWhen true
        } else {
            return@retryWhen false
        }
    }
}
