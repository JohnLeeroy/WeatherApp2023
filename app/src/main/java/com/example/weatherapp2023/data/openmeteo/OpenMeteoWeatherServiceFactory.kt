package com.example.weatherapp2023.data.openmeteo

import com.example.weatherapp2023.data.NetworkServiceConfig
import com.example.weatherapp2023.network.HttpCacheObserverInterceptor
import com.example.weatherapp2023.network.HttpCachingInterceptorFactory
import com.example.weatherapp2023.network.OfflineCacheInterceptorFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject


class OpenMeteoWeatherServiceFactory @Inject constructor(
    private val cachingInterceptorFactory: HttpCachingInterceptorFactory,
    private val offlineCachingInterceptorFactory: OfflineCacheInterceptorFactory,
    private val loggingInterceptor: HttpLoggingInterceptor?
) {
    fun create(config: NetworkServiceConfig, cache: Cache?): OpenMeteoWeatherService {
        val clientBuilder = OkHttpClient.Builder()
        loggingInterceptor?.run {
            clientBuilder.addInterceptor(this)
        }
        cache?.run {
            clientBuilder.cache(cache)
            clientBuilder.addInterceptor(HttpCacheObserverInterceptor(cache))
        }
        clientBuilder.addInterceptor(cachingInterceptorFactory.create(config))
//        clientBuilder.addInterceptor(offlineCachingInterceptorFactory.create(config.maxStaleSeconds))
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(OpenMeteoWeatherService::class.java)
    }

    companion object {
        const val BASE_URL = "https://api.open-meteo.com/v1/"

        fun create(): OpenMeteoWeatherService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(OpenMeteoWeatherService::class.java)
        }
    }
}