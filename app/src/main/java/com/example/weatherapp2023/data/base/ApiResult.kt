package com.example.weatherapp2023.data.base

sealed class ApiResult <out DATA> (val status: ApiStatus, val data: DATA?, val message:String?) {

    class Success<out DATA>(data: DATA?): ApiResult<DATA>(
        status = ApiStatus.SUCCESS,
        data = data,
        message = null
    )

    class Error(message: String, val responseCode: Int = UNKNOWN_ERROR_CODE): ApiResult<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        message = message
    )

    companion object {
        const val UNKNOWN_ERROR_CODE = -1
    }
}