package com.example.weatherapp2023.forecast.data

import com.example.weatherapp2023.data.openmeteo.WeatherType

data class CurrentTempUiModel(
    val temperature: String,
    val tempHigh: String,
    val tempLow: String,
    val date: String,
    val weatherType: WeatherType
    ) : ForecastUiModel()