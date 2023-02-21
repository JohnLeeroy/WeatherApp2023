package com.example.weatherapp2023.forecast.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.example.weatherapp2023.data.openmeteo.WeatherType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForecastRowModel(
    val date: String,
    val dayOfWeek: String,
    val tempHigh: Double,
    val tempLow: Double,
    val weatherType: WeatherType
): ForecastUiModel(), Parcelable