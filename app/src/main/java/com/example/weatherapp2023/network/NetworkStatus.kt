package com.example.weatherapp2023.network

sealed class NetworkStatus {
    object NetworkConnected : NetworkStatus()
    object NetworkDisconnected : NetworkStatus()
}