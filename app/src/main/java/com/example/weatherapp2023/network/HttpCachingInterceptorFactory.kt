package com.example.weatherapp2023.network

import com.example.weatherapp2023.data.NetworkServiceConfig
import okhttp3.CacheControl
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Related documents
 * https://square.github.io/okhttp/4.x/okhttp/okhttp3/-cache/
 * https://square.github.io/okhttp/features/caching/
 */
@Singleton
class HttpCachingInterceptorFactory @Inject constructor() {

    fun create(config: NetworkServiceConfig): Interceptor = Interceptor { chain ->
        var request = chain.request()
        val shouldUseCache = request.header(CACHE_CONTROL_HEADER) != CACHE_CONTROL_NO_CACHE
        if (shouldUseCache) {
            request = request.newBuilder()
                .cacheControl(
                    CacheControl.Builder()
                        .maxAge(config.maxAgeSeconds, TimeUnit.SECONDS)
                        .maxStale(config.maxStaleSeconds, TimeUnit.SECONDS)
                        .build()
                ).build()
        } else {
            // More efficient to force a cached response to be validated by the server
            // rather than using no-cache
            request = request.newBuilder()
                .cacheControl(
                    CacheControl.Builder()
                        .maxAge(0, TimeUnit.SECONDS)
                        .build()
                ).build()
        }
        chain.proceed(request)
    }

    companion object {
        const val CACHE_CONTROL_HEADER = "Cache-Control"
        const val CACHE_CONTROL_NO_CACHE = "no-cache"
    }
}

