package com.example.weatherapp2023.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// excerpts from https://crocsandcoffee.medium.com/android-network-status-listener-stateflow-c5468844f4b5
// ConnectivityManager.NetworkCallback is introduced in Android N so this implementation does not support below API 24
//
@Singleton
class NetworkStatusRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mainDispatcher: CoroutineDispatcher,
    private val appScope: CoroutineScope
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _networkStateFlow = MutableStateFlow(getCurrentNetwork())
    val networkStateFlow = _networkStateFlow.asStateFlow()

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    init {
        _networkStateFlow
            .subscriptionCount
            .map { count -> count > 0 } // map count into active/inactive flag
            .distinctUntilChanged() // only react to true <--> false changes
            .onEach { isActive ->
                /** Only subscribe to network callbacks if we have an active subscriber */
                if (isActive) subscribe()
                else unsubscribe()
            }
            .launchIn(appScope)
    }

    private fun getCurrentNetwork(): NetworkStatus {
        return if (isInternetAvailable())
            NetworkStatus.NetworkConnected
        else
            NetworkStatus.NetworkDisconnected
    }

    private fun emitNetworkState(networkStatus: NetworkStatus) {
        appScope.launch(mainDispatcher) {
            _networkStateFlow.emit(networkStatus)
        }
    }

    private fun subscribe() {
        unsubscribe()
        networkCallback = DefaultNetworkCallback()
        networkCallback?.run {
            connectivityManager.registerDefaultNetworkCallback(this)
        }
        emitNetworkState(getCurrentNetwork())
    }

    private fun unsubscribe() {
        networkCallback?.run {
            connectivityManager.unregisterNetworkCallback(this)
        }
        networkCallback = null
    }

    fun isInternetAvailable(): Boolean {
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        return caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
    }

    // Will internet usage cost the user money to use?
    fun isInternetMetered(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        return !(caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) ?: false)
    }

    private inner class DefaultNetworkCallback : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) =
            emitNetworkState(NetworkStatus.NetworkConnected)

        override fun onUnavailable() = emitNetworkState(NetworkStatus.NetworkConnected)
    }
}

