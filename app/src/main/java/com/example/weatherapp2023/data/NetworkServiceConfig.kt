package com.example.weatherapp2023.data

data class NetworkServiceConfig(
    val cacheSizeInBytes: Long,
    val maxAgeSeconds: Int,
    val maxStaleSeconds: Int
)