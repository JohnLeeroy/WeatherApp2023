package com.example.weatherapp2023.data.openmeteo

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface OpenMeteoWeatherService {

    @GET("forecast?timezone=auto&current_weather=true")
    suspend fun fetchForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double
    ): Response<CurrentWeatherResponse>

    @GET("forecast?timezone=auto&current_weather=true&hourly=temperature_2m,weathercode,rain&daily=temperature_2m_max,weathercode,temperature_2m_min&timezone=auto&temperature_unit=fahrenheit")
    suspend fun fetchCurrentWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double
    ): Response<ForecastResponse>

    @GET("forecast?timezone=auto&current_weather=true&hourly=temperature_2m,weathercode,rain&daily=temperature_2m_max,weathercode,temperature_2m_min&timezone=auto&temperature_unit=fahrenheit")
    @Headers("Cache-Control: no-cache")
    suspend fun forceFetchCurrentWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double
    ): Response<ForecastResponse>

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

