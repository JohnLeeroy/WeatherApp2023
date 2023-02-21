package com.example.weatherapp2023.forecast.adapter

import android.view.View
import com.example.weatherapp2023.forecast.data.ForecastUiModel
import com.example.weatherapp2023.util.BaseViewHolder

sealed class BaseForecastViewHolder(view: View) : BaseViewHolder<ForecastUiModel>(view) {
}