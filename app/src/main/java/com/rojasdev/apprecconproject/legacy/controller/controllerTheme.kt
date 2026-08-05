package com.rojasdev.apprecconproject.legacy.controller

import android.content.Context
import android.content.res.Configuration

object controllerTheme {

    fun main(
        context:Context,
        day: () -> Unit,
        night: () -> Unit
    ){
        val configuration = context.resources.configuration
        val modoActual = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        when (modoActual) {
            Configuration.UI_MODE_NIGHT_YES -> {
                night()
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                day()
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                night()
            }
        }
    }

}