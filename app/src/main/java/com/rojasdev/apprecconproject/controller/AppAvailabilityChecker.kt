package com.rojasdev.apprecconproject.controller

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig

object AppAvailabilityChecker {

    fun checkAppAvailability(
        context: Context,
        onReady: () -> Unit,
        onBlocked: () -> Unit,
        onError: () -> Unit
    ) {
        val remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 // Cada 1 hora
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf("is_funcional" to true))

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isEnabled = remoteConfig.getBoolean("is_funcional")
                    Log.i("AppAvailability","is_funcional: $isEnabled")
                    if (isEnabled) {
                        onReady()
                    } else {
                        onBlocked()
                    }
                } else {
                    onError()
                }
            }
    }
}
