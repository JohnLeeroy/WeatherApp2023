package com.example.weatherapp2023.data.openmeteo

import com.squareup.moshi.Json

data class CurrentWeatherResponse (
    val latitude: Double,
    val longitude: Double,

    @field:Json(name = "generationtime_ms")
    val generationtimeMS: Double,

    @field:Json(name = "utc_offset_seconds")
    val utcOffsetSeconds: Long,

    val timezone: String,

    @field:Json(name = "timezone_abbreviation")
    val timezoneAbbreviation: String,

    val elevation: Double,

    @field:Json(name = "current_weather")
    val currentWeather: CurrentWeather
)