package com.example.weatherapp2023.data

import com.example.weatherapp2023.data.base.ApiResult
import com.example.weatherapp2023.data.openmeteo.ForecastResponse
import com.example.weatherapp2023.data.openmeteo.OpenMeteoWeatherService
import com.example.weatherapp2023.network.retry.DoublingBackoffRetryPolicy
import com.example.weatherapp2023.network.retry.retryWithPolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.Response

class WeatherRepository(private val weatherService: OpenMeteoWeatherService) {

    suspend fun getWeather(lat: Double, lng: Double): Response<ForecastResponse> =
        withContext(Dispatchers.IO) {
            weatherService.fetchCurrentWeather(lat, lng)
        }

    fun getWeatherFlow(lat: Double, lng: Double): Flow<ApiResult<ForecastResponse>> = flow {
        var result = weatherService.forceFetchCurrentWeather(lat, lng)
        if (result.isSuccessful) {
            emit(ApiResult.Success(result.body()))
        } else {
            var errorMessage = result.errorBody()?.string() ?: result.raw().message
            emit(ApiResult.Error(errorMessage, result.raw().code))
        }
    }.flowOn(Dispatchers.IO)
        .retryWithPolicy(DoublingBackoffRetryPolicy.Default)
        .catch {
            emit(ApiResult.Error("Max Retries reached.  Exception: ${it.message}", -1))
        }
}