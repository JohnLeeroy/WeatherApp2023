package com.example.weatherapp2023.data.openmeteo

import com.squareup.moshi.Json

data class CurrentWeather (
    val temperature: Double,
    @field:Json(name = "windspeed")
    val windSpeed: Double,
    @field:Json(name = "winddirection")
    val windDirection: Double,
    @field:Json(name = "weathercode")
    val weatherCode: Long,
    val time: String
)