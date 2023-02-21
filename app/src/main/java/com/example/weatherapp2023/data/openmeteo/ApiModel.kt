package com.example.weatherapp2023.data.openmeteo

import com.squareup.moshi.Json

data class ForecastResponse (
    val latitude: Double,
    val longitude: Double,

    @field:Json(name = "generationtime_ms")
    val generationtimeMS: Double,

    @field:Json(name = "utc_offset_seconds")
    val utcOffsetSeconds: Long,

    val timezone: String,

    @field:Json(name = "timezone_abbreviation")
    val timezoneAbbreviation: String,

    val elevation: Long,

    @field:Json(name = "hourly_units")
    val hourlyUnits: HourlyUnits,

    val hourly: Hourly,

    @field:Json(name = "daily_units")
    val dailyUnits: DailyUnits,

    val daily: Daily,

    @field:Json(name = "current_weather")
    val currentWeather: CurrentWeather
)

data class Daily (
    val time: List<String>,

    @field:Json(name = "temperature_2m_max")
    val temperature2MMax: List<Double>,

    @field:Json(name = "temperature_2m_min")
    val temperature2MMin: List<Double>,

    val weathercode: List<Int>
)

data class DailyUnits (
    val time: String,

    @field:Json(name = "temperature_2m_max")
    val temperatureMax: String,

    @field:Json(name = "temperature_2m_min")
    val temperatureMin: String
)

data class Hourly (
    val time: List<String>,

    @field:Json(name = "temperature_2m")
    val temperature: List<Double>,

    val rain: List<Double>,

    val weathercode: List<Int>
)

data class HourlyUnits (
    val time: String,

    @field:Json(name = "temperature_2m")
    val temperature: String,

    val rain: String
)