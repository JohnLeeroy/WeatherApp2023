package com.example.weatherapp2023.network.retry

import kotlin.math.pow

data class ExponentialBackoffRetryPolicy(
    override val maxRetries: Int = 3,
    override val initialDelayMillis: Long = 500) : RetryPolicy {
    override fun calculateBackoffDelayMillis(attemptCount: Int): Long {
        return if(attemptCount == 0) {
            initialDelayMillis
        } else {
            2.0.pow(attemptCount).toLong()
        }
    }

    companion object {
        val Default = ExponentialBackoffRetryPolicy()
    }
}