package com.example.weatherapp2023

import android.app.Application
import android.content.ComponentCallbacks2
import com.bumptech.glide.Glide
import com.example.weatherapp2023.network.NetworkStatusRepo
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Cache
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class WeatherApp : Application() {

    @Inject lateinit var httpCache: Cache

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND,
            ComponentCallbacks2.TRIM_MEMORY_MODERATE,
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
            ComponentCallbacks2.TRIM_MEMORY_MODERATE,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> {
                httpCache.evictAll()
                Glide.get(this).trimMemory(level)
            }
            else -> {

            }
        }
    }
}