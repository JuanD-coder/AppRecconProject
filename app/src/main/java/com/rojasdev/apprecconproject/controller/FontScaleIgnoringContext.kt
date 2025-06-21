package com.rojasdev.apprecconproject.controller

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources

class FontScaleIgnoringContext(base: Context) : ContextWrapper(base) {
    override fun getResources(): Resources {val resources = super.getResources()
        val configuration = Configuration(resources.configuration)
        configuration.fontScale = 1.0f // Establece la escala de fuente a 1.0 (valor predeterminado)
        return createConfigurationContext(configuration).resources
    }
}