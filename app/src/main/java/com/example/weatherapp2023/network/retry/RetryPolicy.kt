package com.example.weatherapp2023.network.retry

interface RetryPolicy {
    val maxRetries: Int
    val initialDelayMillis: Long
    fun calculateBackoffDelayMillis(attemptCount: Int): Long
}

