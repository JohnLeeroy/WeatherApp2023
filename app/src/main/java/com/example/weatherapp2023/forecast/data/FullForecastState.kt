package com.example.weatherapp2023.forecast.data

sealed class ForecastUiState {
    data class FullForecastState(
        val forecast: List<ForecastUiModel> = emptyList()
    ) : ForecastUiState()

    object LoadingForecastState : ForecastUiState()
    data class EmptyForecastState(val message: String) : ForecastUiState()
    data class ForecastErrorState(val message: String) : ForecastUiState()
}

