package com.example.weatherapp2023.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineCacheInterceptorFactory @Inject constructor(private val networkStatusRepo: NetworkStatusRepo) {
    fun create(maxStaleSeconds: Int) = Interceptor { chain ->
        var request = chain.request()
        if (!networkStatusRepo.isInternetAvailable()) {
            request = request.newBuilder()
                .cacheControl(
                    CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(maxStaleSeconds, TimeUnit.SECONDS)
                        .build()
                ).build()
        }
        chain.proceed(request)
    }
}