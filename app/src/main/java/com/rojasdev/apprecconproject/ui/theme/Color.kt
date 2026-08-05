package com.rojasdev.apprecconproject.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ── Brand accents (Legacy Essence) ─────────────────────────
val HippieGreenLegacy = Color(0xFF4F864F) // Principal Informes
val ThunderbirdLegacy = Color(0xFFC62828)  // Principal Recolección (Ajustado de #a60e18 para M3)
val OrangeLegacy = Color(0xFFF16821)      // Principal Jornales
val CinnabarLegacy = Color(0xFFE63933)    // Domingos / Alertas
val TomThumbLegacy = Color(0xFF065C19)    // Variante Verde Oscuro

val RedAccent = ThunderbirdLegacy
val OnRedAccent = Color(0xFFFFFFFF)
val RedSoft = Color(0xFFFFEBEE)
val OnRedSoft = ThunderbirdLegacy

val OrangeAccent = OrangeLegacy
val OnOrangeAccent = Color(0xFFFFFFFF)
val OrangeSoft = Color(0xFFFFF3E0)
val OnOrangeSoft = OrangeLegacy

val GreenAccent = HippieGreenLegacy
val OnGreenAccent = Color(0xFFFFFFFF)
val GreenSoft = Color(0xFFE8F5E9)
val OnGreenSoft = HippieGreenLegacy

// Dark variants (lighter for contrast on dark backgrounds)
val RedAccentDark = Color(0xFFFF8A80)
val OnRedAccentDark = Color(0xFF690005)
val RedSoftDark = Color(0xFF3D1512)
val OnRedSoftDark = Color(0xFFFFB3AB)

val OrangeAccentDark = Color(0xFFFFB74D)
val OnOrangeAccentDark = Color(0xFF3D1F00)
val OrangeSoftDark = Color(0xFF3D2100)
val OnOrangeSoftDark = Color(0xFFFFCC80)

val GreenAccentDark = Color(0xFFA5D6A7)
val OnGreenAccentDark = Color(0xFF00391A)
val GreenSoftDark = Color(0xFF123A15)
val OnGreenSoftDark = Color(0xFFB9E4BB)

// ── M3 semantic roles ─────────────────────────────────────
val LightColorScheme = lightColorScheme(
    primary = RedAccent,
    onPrimary = OnRedAccent,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = Color(0xFF410002),
    secondary = OrangeAccent,
    onSecondary = OnOrangeAccent,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFF2B1600),
    tertiary = GreenAccent,
    onTertiary = OnGreenAccent,
    tertiaryContainer = Color(0xFFC8E6C9),
    onTertiaryContainer = Color(0xFF002106),
    background = Color(0xFFFAF9F6),
    onBackground = Color(0xFF1D1B1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1D1B1A),
    surfaceVariant = Color(0xFFF0EDE9),
    onSurfaceVariant = Color(0xFF4F4A47),
    outline = Color(0xFF7A7571),
    outlineVariant = Color(0xFFDDD9D4)
)

val DarkColorScheme = darkColorScheme(
    primary = RedAccentDark,
    onPrimary = OnRedAccentDark,
    primaryContainer = Color(0xFF93000A),
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = OrangeAccentDark,
    onSecondary = OnOrangeAccentDark,
    secondaryContainer = Color(0xFF753C00),
    onSecondaryContainer = Color(0xFFFFE0B2),
    tertiary = GreenAccentDark,
    onTertiary = OnGreenAccentDark,
    tertiaryContainer = Color(0xFF00522A),
    onTertiaryContainer = Color(0xFFC8E6C9),
    background = Color(0xFF1A1412),
    onBackground = Color(0xFFEBE4E0),
    surface = Color(0xFF1A1412),
    onSurface = Color(0xFFEBE4E0),
    surfaceVariant = Color(0xFF4A4440),
    onSurfaceVariant = Color(0xFFCBC4BF),
    outline = Color(0xFF948D88),
    outlineVariant = Color(0xFF4A4440)
)

// ── Module accents (esencia legacy) ───────────────────────
data class RecconModuleColors(
    val accent: Color,
    val onAccent: Color,
    val soft: Color,
    val onSoft: Color
)

data class RecconColors(
    val recollection: RecconModuleColors,
    val labor: RecconModuleColors,
    val accounting: RecconModuleColors
)

val LightRecconColors = RecconColors(
    recollection = RecconModuleColors(RedAccent, OnRedAccent, RedSoft, OnRedSoft),
    labor = RecconModuleColors(OrangeAccent, OnOrangeAccent, OrangeSoft, OnOrangeSoft),
    accounting = RecconModuleColors(GreenAccent, OnGreenAccent, GreenSoft, OnGreenSoft)
)

val DarkRecconColors = RecconColors(
    recollection = RecconModuleColors(RedAccentDark, OnRedAccentDark, RedSoftDark, OnRedSoftDark),
    labor = RecconModuleColors(OrangeAccentDark, OnOrangeAccentDark, OrangeSoftDark, OnOrangeSoftDark),
    accounting = RecconModuleColors(GreenAccentDark, OnGreenAccentDark, GreenSoftDark, OnGreenSoftDark)
)

val LocalRecconColors = staticCompositionLocalOf { LightRecconColors }