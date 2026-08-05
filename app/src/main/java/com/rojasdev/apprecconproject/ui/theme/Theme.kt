package com.rojasdev.apprecconproject.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

object RecconTheme {
    val colors: RecconColors
        @Composable
        @ReadOnlyComposable
        get() = LocalRecconColors.current
}

@Composable
fun RecconTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val recconColors = if (darkTheme) DarkRecconColors else LightRecconColors

    CompositionLocalProvider(LocalRecconColors provides recconColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = RecconTypography,
            shapes = RecconShapes,
            content = content
        )
    }
}