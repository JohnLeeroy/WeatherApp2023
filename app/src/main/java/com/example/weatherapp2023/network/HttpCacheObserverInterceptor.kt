package com.example.weatherapp2023.network

import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class HttpCacheObserverInterceptor @Inject constructor(private val cache: Cache) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.d("HttpCacheObserverInterceptor: Request Count: ${cache.requestCount()}, Network Count ${cache.networkCount()} Hit Count: ${cache.hitCount()}")
        return chain.proceed(chain.request())
    }
}