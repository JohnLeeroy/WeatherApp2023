package com.example.weatherapp2023.forecast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2023.data.WeatherRepository
import com.example.weatherapp2023.data.base.ApiResult
import com.example.weatherapp2023.data.openmeteo.WeatherType
import com.example.weatherapp2023.forecast.data.CurrentTempUiModel
import com.example.weatherapp2023.forecast.data.ForecastRowModel
import com.example.weatherapp2023.forecast.data.ForecastUiModel
import com.example.weatherapp2023.forecast.data.ForecastUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WeatherForecastVM @Inject constructor(
    private val repository: WeatherRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ForecastUiState>(ForecastUiState.EmptyForecastState("Empty"))
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()

    fun getCurrentWeather() {
        viewModelScope.launch {
            repository.getWeatherFlow(37.7749, -122.4194)
                .onStart {
                    _uiState.emit(ForecastUiState.LoadingForecastState)
                }
                .map { response ->
                    return@map when (response) {
                        is ApiResult.Success -> {
                            response.data?.let { response ->
                                val models = mutableListOf<ForecastUiModel>()
                                val today = LocalDateTime.parse(
                                    response.currentWeather.time,
                                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                                )
                                val dailyWeather =
                                    response.daily.time.mapIndexed { index, time ->
                                        val date =
                                            LocalDate.parse(time, DateTimeFormatter.ISO_LOCAL_DATE)
                                        ForecastRowModel(
                                            time,
                                            date.format(dailyFormat),
                                            response.daily.temperature2MMax[index],
                                            response.daily.temperature2MMin[index],
                                            WeatherType.fromWMO(response.daily.weathercode[index])
                                        )
                                    }
                                models.add(
                                    CurrentTempUiModel(
                                        String.format(
                                            "%.0f",
                                            response.currentWeather.temperature
                                        ), response.daily.temperature2MMax[0].toString(),
                                        response.daily.temperature2MMin[0].toString(),
                                        today.format(currentFormat),
                                        WeatherType.fromWMO(response.daily.weathercode[0])
                                    )
                                )
                                models.addAll(dailyWeather)
//                                savedStateHandle["CURRENT_TEMP_KEY"] =
//                                    currentWeather.currentWeather.temperature
                                ForecastUiState.FullForecastState(models)
                            } ?: ForecastUiState.ForecastErrorState("Error: data missing")
                        }
                        is ApiResult.Error -> {
                            val message =
                                if (response.responseCode == ApiResult.UNKNOWN_ERROR_CODE) {
                                    "Unknown Error."
                                } else {
                                    "Network Error."
                                }
                            ForecastUiState.ForecastErrorState(message)
                        }
                    }
                }
                .collect { weatherUiState -> _uiState.update { weatherUiState } }
        }
    }

    companion object {
        const val CURRENT_TEMP_KEY = "temp"
        private val currentFormat = DateTimeFormatter.ofPattern("MMM dd, h:mm a")
        private val dailyFormat = DateTimeFormatter.ofPattern("EEEE, MMM dd")
//        private val from HourlyFormat = SimpleDateFormat("YYYY-MM0DD")
    }
}