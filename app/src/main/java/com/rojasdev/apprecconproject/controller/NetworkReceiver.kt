package com.rojasdev.apprecconproject.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

class NetworkReceiver(
    private val onNoInternet: () -> Unit,
    private val onInternetAvailable: () -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            if (!isNetworkAvailable(it)) {
                Log.i("NetworkReceiver", "SIN internet")
                onNoInternet()
            } else {
                Log.i("NetworkReceiver", "Conectado a internet")
                onInternetAvailable()
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
