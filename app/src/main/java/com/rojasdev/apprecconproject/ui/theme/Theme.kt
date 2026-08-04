package com.rojasdev.apprecconproject.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = CoffeePrimary,
    onPrimary = CoffeeOnPrimary,
    primaryContainer = CoffeePrimaryContainer,
    onPrimaryContainer = CoffeeOnPrimaryContainer,
    secondary = CoffeeSecondary,
    onSecondary = CoffeeOnSecondary,
    background = CoffeeBackground,
    surface = CoffeeSurface,
    onSurface = CoffeeOnSurface
)

@Composable
fun RecconTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
