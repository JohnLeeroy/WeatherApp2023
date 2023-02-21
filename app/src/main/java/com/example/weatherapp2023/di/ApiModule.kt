package com.example.weatherapp2023.di

import android.app.Application
import android.content.Context
import com.example.weatherapp2023.data.NetworkServiceConfig
import com.example.weatherapp2023.network.NetworkStatusRepo
import com.example.weatherapp2023.data.openmeteo.OpenMeteoWeatherService
import com.example.weatherapp2023.data.openmeteo.OpenMeteoWeatherServiceFactory
import com.example.weatherapp2023.data.WeatherRepository
import com.example.weatherapp2023.network.HttpCachingInterceptorFactory
import com.example.weatherapp2023.network.OfflineCacheInterceptorFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun networkServiceConfig() = NetworkServiceConfig(FiveMegabytes, 60, 100)

    @Provides
    fun provideWeatherRepository(service: OpenMeteoWeatherService): WeatherRepository =
        WeatherRepository(service)

    @Provides
    fun provideHttpClientCache(app: Application, config: NetworkServiceConfig) =
        Cache(app.cacheDir, config.cacheSizeInBytes)

    @Provides
    fun provideNetworkStatusRepo(@ApplicationContext context: Context) = NetworkStatusRepo(
        context,
        Dispatchers.Main,
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    )

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    @Provides
    fun provideOpenMeteoWeatherService(
        cache: Cache,
        cachingInterceptorFactory: HttpCachingInterceptorFactory,
        offlineCacheInterceptorFactory: OfflineCacheInterceptorFactory,
        loggingInterceptor: HttpLoggingInterceptor?,
        config: NetworkServiceConfig
    ): OpenMeteoWeatherService =
        OpenMeteoWeatherServiceFactory(
            cachingInterceptorFactory,
            offlineCacheInterceptorFactory,
            loggingInterceptor
        ).create(config, cache)

    private companion object {
        const val FiveMegabytes = 5 * 1024L * 1024L
    }
}