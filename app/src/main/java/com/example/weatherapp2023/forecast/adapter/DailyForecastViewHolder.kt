package com.example.weatherapp2023.forecast.adapter

import com.example.weatherapp2023.databinding.DailyForecastViewholderBinding
import com.example.weatherapp2023.forecast.data.ForecastRowModel
import com.example.weatherapp2023.forecast.data.ForecastUiModel
import com.example.weatherapp2023.util.BaseViewHolder

class DailyForecastViewHolder(private val binding: DailyForecastViewholderBinding) :
    BaseViewHolder<ForecastUiModel>(binding.root) {

    override fun bind(forecast: ForecastUiModel) {
        (forecast as? ForecastRowModel)?.run {
            binding.timeLabel.text = forecast.date
            binding.highTempLabel.text = String.format("%.0f", forecast.tempHigh)
            binding.lowTempLabel.text = String.format("%.0f", forecast.tempLow)
        }
    }

}