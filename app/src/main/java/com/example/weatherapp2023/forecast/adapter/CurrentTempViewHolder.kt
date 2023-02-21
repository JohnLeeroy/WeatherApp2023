package com.example.weatherapp2023.forecast.adapter

import com.example.weatherapp2023.databinding.LayoutCurrentTempBinding
import com.example.weatherapp2023.forecast.data.CurrentTempUiModel
import com.example.weatherapp2023.forecast.data.ForecastUiModel
import com.example.weatherapp2023.util.BaseViewHolder

class CurrentTempViewHolder(private val binding: LayoutCurrentTempBinding) :
    BaseViewHolder<ForecastUiModel>(binding.root) {

    override fun bind(model: ForecastUiModel) {
        (model as? CurrentTempUiModel)?.run {
            binding.tempLabel.text = model.temperature
        }
    }
}