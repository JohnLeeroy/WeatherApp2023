package com.example.weatherapp2023.network.retry

data class DoublingBackoffRetryPolicy(
    override val maxRetries: Int = 3,
    override val initialDelayMillis: Long = 500) : RetryPolicy {
    override fun calculateBackoffDelayMillis(attemptCount: Int): Long {
        var delayDuration = initialDelayMillis
        for(i in 0 until attemptCount) {
            delayDuration *= 2
        }
        return delayDuration
    }

    companion object {
        val Default = DoublingBackoffRetryPolicy()
    }
}

