# Professionalización de UI Compose Implementation Plan

> **For agentic workers:** Use `mobiai-mobile-executing-plans-with-subagents` (recommended) or `mobiai-mobile-executing-plans` to implement this plan task-by-task. Steps use checkbox syntax for tracking.

**Goal:** Elevar las 6 pantallas Compose a nivel profesional: tema M3 completo (light+dark), tokens semánticos, componentes reutilizables, edge-to-edge y estados de UI.

**Architecture:** Construir cimientos de tema (Color/Type/Dimens/Shape/Theme) → librería interna de componentes → refactorizar cada screen para consumir tokens y componentes, eliminando colores hardcodeados. Mantener la esencia legacy: rojo=recolección, naranja=jornales, verde=contabilidad.

**Tech Stack:** Jetpack Compose BOM 2026.06.00, Material 3, Navigation Compose, Hilt.

**Platform:** Android (minSdk 23, targetSdk 34, compileSdk 37)

---

## Decisiones de diseño

1. **Identidad de marca preservada** → `dynamicColor = false` por defecto. La paleta café/rojo/naranja/verde es la esencia legacy.
2. **Accents de módulo como extensión de tema** → `RecconTheme.colors.recollection / labor / accounting` (patrón estándar M3 con CompositionLocal). Cada módulo: `accent` (fuerte), `onAccent`, `soft` (container suave), `onSoft`.
3. **Roles semánticos M3**: `primary`=rojo, `secondary`=naranja, `tertiary`=verde. Usados para componentes estándar (botones, textfields, dialogs).
4. **Dark mode** vía `isSystemInDarkTheme()`. Variantes oscuras de los accents para contraste.
5. **Edge-to-edge**: `enableEdgeToEdge()` en activity + Scaffold maneja insets (las screens ya usan `padding(padding)` de Scaffold).

## Estructura de archivos

**Crear:**
- `app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Type.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Dimens.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Shape.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/components/RecconTopBar.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/components/SectionTitle.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/components/HeroSummaryCard.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/components/CompactStatCard.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/components/StateViews.kt`

**Modificar:**
- `app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Color.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Theme.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/ComposeMainActivity.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/main/MainScreen.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/recollection/RecollectionScreen.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/labor/LaborScreen.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/analytics/AnalyticsScreen.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/configuration/ConfigurationScreen.kt`
- `app/src/main/java/com/rojasdev/apprecconproject/ui/reports/ReportsScreen.kt`

---

### Task 1: Color.kt — Paletas light/dark + tokens de módulo

**Files:**
- Modify: `app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Color.kt`

- [ ] **Step 1: Reemplazar contenido completo de Color.kt**

```kotlin
package com.rojasdev.apprecconproject.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ── Brand accents ─────────────────────────────────────────
val RedAccent = Color(0xFFC62828)
val OnRedAccent = Color(0xFFFFFFFF)
val RedSoft = Color(0xFFFFEBEE)
val OnRedSoft = Color(0xFFC62828)

val OrangeAccent = Color(0xFFE65100)
val OnOrangeAccent = Color(0xFFFFFFFF)
val OrangeSoft = Color(0xFFFFF3E0)
val OnOrangeSoft = Color(0xFFE65100)

val GreenAccent = Color(0xFF2E7D32)
val OnGreenAccent = Color(0xFFFFFFFF)
val GreenSoft = Color(0xFFE8F5E9)
val OnGreenSoft = Color(0xFF2E7D32)

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

// ── Backwards-compat aliases (DEPRECATED) ────────────────
// Se eliminan en Task 13 cuando todas las pantallas usen tokens.
// Mantienen verde el build intermedio durante la migración.
val CoffeePrimary = RedAccent
val CoffeeSecondary = OrangeAccent
val CoffeeBackground = Color(0xFFFAF9F6)
val CoffeeSurface = Color(0xFFFFFFFF)
val CoffeeOnSurface = Color(0xFF1D1B1A)
val PastelRed = RedSoft
val OnPastelRed = OnRedSoft
val PastelOrange = OrangeSoft
val OnPastelOrange = OnOrangeSoft
val PastelGreen = GreenSoft
val OnPastelGreen = OnGreenSoft
val OnPastelBlue = Color(0xFF1976D2)
val OnPastelPurple = Color(0xFF7B1FA2)
```

- [ ] **Step 2: Build check parcial**

Run: `./gradlew :app:compileDebugKotlin`
Expected: PASS. Las screens siguen funcionando vía aliases deprecados; los refactors de pantalla los irán borrando.

---

### Task 2: Type.kt — Tipografía

**Files:**
- Create: `app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Type.kt`

- [ ] **Step 1: Crear Type.kt**

```kotlin
package com.rojasdev.apprecconproject.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val RecconTypography = Typography(
    displaySmall = TextStyle(fontWeight = FontWeight.Black, fontSize = 36.sp, lineHeight = 44.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.Black, fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
    titleSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp)
)
```

---

### Task 3: Dimens.kt + Shape.kt — Sistema de espaciado y formas

**Files:**
- Create: `app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Dimens.kt`
- Create: `app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Shape.kt`

- [ ] **Step 1: Crear Dimens.kt**

```kotlin
package com.rojasdev.apprecconproject.ui.theme

import androidx.compose.ui.unit.dp

object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
}
```

- [ ] **Step 2: Crear Shape.kt**

```kotlin
package com.rojasdev.apprecconproject.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val RecconShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)
```

---

### Task 4: Theme.kt — Dark mode + tokens en contexto

**Files:**
- Modify: `app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Theme.kt`

- [ ] **Step 1: Reemplazar contenido completo**

```kotlin
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
```

- [ ] **Step 2: Build check**

Run: `./gradlew :app:compileDebugKotlin`
Expected: PASS (Theme.kt deja de usar `CoffeePrimary`/`CoffeeBackground`; otras pantallas todavía las referencian por import `*` — no hay error).

---

### Task 5: Componentes reutilizables

**Files:**
- Create: `app/src/main/java/com/rojasdev/apprecconproject/ui/components/RecconTopBar.kt`
- Create: `app/src/main/java/com/rojasdev/apprecconproject/ui/components/SectionTitle.kt`
- Create: `app/src/main/java/com/rojasdev/apprecconproject/ui/components/HeroSummaryCard.kt`
- Create: `app/src/main/java/com/rojasdev/apprecconproject/ui/components/CompactStatCard.kt`
- Create: `app/src/main/java/com/rojasdev/apprecconproject/ui/components/StateViews.kt`

- [ ] **Step 1: RecconTopBar.kt**

```kotlin
package com.rojasdev.apprecconproject.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecconTopBar(
    title: String,
    accent: Color,
    onBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Black) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = accent,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}
```

- [ ] **Step 2: SectionTitle.kt**

```kotlin
package com.rojasdev.apprecconproject.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.rojasdev.apprecconproject.ui.theme.Spacing

@Composable
fun SectionTitle(
    title: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = accent
    )
}
```

- [ ] **Step 3: HeroSummaryCard.kt** (header lleno a color, 1-2 stats, CTA opcional)

```kotlin
package com.rojasdev.apprecconproject.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.VerticalDivider
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon

data class HeroStat(
    val label: String,
    val value: String
)

@Composable
fun HeroSummaryCard(
    accent: Color,
    stats: List<HeroStat>,
    modifier: Modifier = Modifier,
    onAction: (() -> Unit)? = null,
    actionLabel: String = ""
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = accent,
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                stats.forEachIndexed { index, stat ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stat.value,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            stat.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    if (index != stats.lastIndex) {
                        VerticalDivider(
                            modifier = Modifier.height(40.dp),
                            color = Color.White.copy(alpha = 0.3f)
                        )
                    }
                }
            }
            if (onAction != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = accent
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.Default.Update, contentDescription = null, modifier = Modifier.width(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(actionLabel, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
```

- [ ] **Step 4: CompactStatCard.kt** (estilo suave para Analytics)

```kotlin
package com.rojasdev.apprecconproject.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CompactStatCard(
    label: String,
    value: String,
    money: Int,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.2f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = accent)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("$${money}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Black, color = accent)
        }
    }
}
```

- [ ] **Step 5: StateViews.kt**

```kotlin
package com.rojasdev.apprecconproject.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyState(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}
```

- [ ] **Step 6: Build check**

Run: `./gradlew :app:compileDebugKotlin`
Expected: PASS.

---

### Task 6: Edge-to-edge

**Files:**
- Modify: `app/src/main/java/com/rojasdev/apprecconproject/ui/ComposeMainActivity.kt`
- Check: `app/src/main/AndroidManifest.xml`

- [ ] **Step 1: Verificar que `ComposeMainActivity` es la actividad launcher en el manifest**

Run: `Get-Content app\src\main\AndroidManifest.xml`
Expected: launcher apunta a `ComposeMainActivity` (si apunta a `MainActivity` legacy, cambiar `android.intent.action.MAIN` al `ComposeMainActivity`).

- [ ] **Step 2: Activar edge-to-edge**

```kotlin
import androidx.activity.enableEdgeToEdge

class ComposeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            RecconTheme {
                AppNavigation()
            }
        }
    }
}
```

- [ ] **Step 3: Build + correr en emulador**

Run: `./gradlew :app:assembleDebug`
Expected: PASS. Las screens ya usan `padding(padding)` de Scaffold → insets manejados.

---

### Task 7: Refactor MainScreen

**Files:**
- Modify: `app/src/main/java/com/rojasdev/apprecconproject/ui/main/MainScreen.kt`

- [ ] **Step 1: Reemplazar import de tema y colores**

```kotlin
import com.rojasdev.apprecconproject.ui.theme.RecconTheme
// eliminar: import com.rojasdev.apprecconproject.ui.theme.*
```

- [ ] **Step 2: HomeModule usa accent de módulo**

```kotlin
data class HomeModule(
    val title: String,
    val icon: ImageVector,
    val description: String,
    val module: RecconModuleColors,
    val onClick: () -> Unit
)

val modules = listOf(
    HomeModule("Recolección", Icons.Default.Agriculture, "Gestión de café diario", RecconTheme.colors.recollection) { onNavigate("recoleccion") },
    HomeModule("Jornales", Icons.Default.Engineering, "Control de días de trabajo", RecconTheme.colors.labor) { onNavigate("labor") },
    HomeModule("Contabilidad", Icons.Default.Assessment, "Reportes y finanzas", RecconTheme.colors.accounting) { onNavigate("analytics") }
)
```

- [ ] **Step 3: PriceHeader → HeroSummaryCard**

```kotlin
@Composable
fun PriceHeader(uiState: MainUiState, onNavigate: (String) -> Unit) {
    HeroSummaryCard(
        accent = RecconTheme.colors.recollection.accent,
        stats = listOf(
            HeroStat("Con Alimentación", "$${uiState.priceYesAliment}"),
            HeroStat("Sin Alimentación", "$${uiState.priceNoAliment}")
        ),
        onAction = { onNavigate("configuracion") },
        actionLabel = "ACTUALIZAR"
    )
}
```

- [ ] **Step 4: ModuleCard usa `module.module`**

```kotlin
Surface(modifier = Modifier.fillMaxHeight().width(100.dp), color = module.module.soft, shape = MaterialTheme.shapes.extraLarge) {
    Box(contentAlignment = Alignment.Center) {
        Icon(module.icon, null, modifier = Modifier.size(40.dp), tint = module.module.onSoft)
    }
}
// title color = module.module.onSoft
```

- [ ] **Step 5: WelcomeHeader — tint settings = onSurfaceVariant, coffee = colorScheme.primary**

- [ ] **Step 6: Usar Spacing tokens en paddings** (`Spacing.md`, `Spacing.lg`)

- [ ] **Step 7: Mostrar LoadingState cuando `uiState.isLoading`**

```kotlin
if (uiState.isLoading) {
    LoadingState()
} else {
    PriceHeader(uiState, onNavigate)
}
```

- [ ] **Step 8: Build check**

Run: `./gradlew :app:compileDebugKotlin`
Expected: PASS.

---

### Task 8: Refactor RecollectionScreen

**Files:**
- Modify: `app/src/main/java/com/rojasdev/apprecconproject/ui/recollection/RecollectionScreen.kt`

- [ ] **Step 1: Eliminar `import com.rojasdev.apprecconproject.ui.theme.*`; añadir `import com.rojasdev.apprecconproject.ui.theme.RecconTheme` y componentes**

- [ ] **Step 2: BottomAppBar — colores de módulo**

```kotlin
val accent = RecconTheme.colors.recollection
BottomAppBar(
    containerColor = accent.accent,
    contentColor = Color.White,
    actions = {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            icon = { Icon(Icons.Default.Payments, null) },
            label = { Text("Pagos", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = accent.accent,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.White
            )
        )
        // idem tab 1 "Personal"
    },
    floatingActionButton = {
        FloatingActionButton(onClick = onAddClick, containerColor = Color.White, contentColor = accent.accent) {
            Icon(Icons.Default.Add, "Añadir")
        }
    }
)
```

- [ ] **Step 3: Header título — `accent.onSoft`** (texto sobre background)

```kotlin
Text(
    text = if (selectedTab == 0) "RESUMEN DE PAGOS" else "GESTIÓN DE PERSONAL",
    style = MaterialTheme.typography.titleLarge,
    fontWeight = FontWeight.Black,
    color = accent.onSoft
)
Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = accent.onSoft)
```

- [ ] **Step 4: TotalSummaryHeader → HeroSummaryCard**

```kotlin
@Composable
fun TotalSummaryHeader(kg: Double, amount: Double) {
    HeroSummaryCard(
        accent = RecconTheme.colors.recollection.accent,
        stats = listOf(
            HeroStat("Total Recolectado", "${kg} Kg"),
            HeroStat("Total a Pagar", "$${amount.toInt()}")
        )
    )
}
```

- [ ] **Step 5: CollectionCard — tile `module.soft`, price `module.onSoft`**

- [ ] **Step 6: AddCollectorDialog — botón y borde `colorScheme.primary`, textos `onSurfaceVariant`**

- [ ] **Step 7: CollectorsList — tile `accent.soft`, icon `accent.onSoft`, botón ARCHIVAR `onSurfaceVariant`**

- [ ] **Step 8: EmptyState para listas vacías**

```kotlin
if (uiState.collectionTotals.isEmpty()) {
    EmptyState("No hay pagos registrados todavía")
} else {
    CollectionList(uiState.collectionTotals)
}
// y en tab Personal:
if (uiState.collectors.isEmpty()) EmptyState("No hay personal registrado") else CollectorsList(...)
```

- [ ] **Step 9: AdBannerPlaceholder — usar `surfaceVariant` en vez de `Color.Black`** (oscuro mantiene esencia publicitaria)

- [ ] **Step 10: Build check**

Run: `./gradlew :app:compileDebugKotlin`
Expected: PASS.

---

### Task 9: Refactor LaborScreen

**Files:**
- Modify: `app/src/main/java/com/rojasdev/apprecconproject/ui/labor/LaborScreen.kt`

- [ ] **Step 1: Imports — `RecconTheme` + componentes; quitar `theme.*`**

- [ ] **Step 2: BottomAppBar — módulo labor**

```kotlin
val accent = RecconTheme.colors.labor
BottomAppBar(
    containerColor = accent.accent,
    contentColor = Color.White,
    floatingActionButton = {
        FloatingActionButton(onClick = {}, containerColor = Color.White, contentColor = accent.accent) {
            Icon(Icons.Default.Add, null)
        }
    }
)
```

- [ ] **Step 3: Header — `accent.onSoft`**

- [ ] **Step 4: LaborSummaryHeader → HeroSummaryCard**

```kotlin
@Composable
fun LaborSummaryHeader(days: Double, amount: Double) {
    HeroSummaryCard(
        accent = RecconTheme.colors.labor.accent,
        stats = listOf(
            HeroStat("Días Totales", "${days.toInt()}"),
            HeroStat("Total a Pagar", "$${amount.toInt()}")
        )
    )
}
```

- [ ] **Step 5: LaborCard — tile `module.soft`, icon y price `module.onSoft`**

- [ ] **Step 6: WorkerItem — tile `module.soft`, icon `module.onSoft`**

- [ ] **Step 7: EmptyState para listas vacías** (ambos tabs)

- [ ] **Step 8: Build check**

Run: `./gradlew :app:compileDebugKotlin`
Expected: PASS.

---

### Task 10: Refactor AnalyticsScreen

**Files:**
- Modify: `app/src/main/java/com/rojasdev/apprecconproject/ui/analytics/AnalyticsScreen.kt`

- [ ] **Step 1: Imports — `RecconTheme` + componentes; quitar `theme.*`**

- [ ] **Step 2: TopAppBar → RecconTopBar**

```kotlin
RecconTopBar(
    title = "CONTABILIDAD",
    accent = RecconTheme.colors.accounting.accent,
    onBack = onBack,
    actions = {
        IconButton(onClick = { /* Export PDF */ }) {
            Icon(Icons.Default.PictureAsPdf, "Exportar")
        }
    }
)
```

- [ ] **Step 3: CalendarSection — arrows/título `accounting.onSoft`**

- [ ] **Step 4: CalendarDayItem — seleccionado `accounting.accent` bg + `White` texto; dots: recolección `recollection.accent`, jornales `labor.accent`**

- [ ] **Step 5: DaySummaryHeader — CompactStatCard**

```kotlin
@Composable
fun DaySummaryHeader(uiState: AnalyticsUiState) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.md, vertical = Spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        CompactStatCard("Recolección", "${uiState.totalKg} Kg", uiState.totalCollectionMoney.toInt(), RecconTheme.colors.recollection.onSoft, Modifier.weight(1f))
        CompactStatCard("Jornales", "${uiState.totalWorkDays.toInt()} Días", uiState.totalWorkMoney.toInt(), RecconTheme.colors.labor.onSoft, Modifier.weight(1f))
    }
}
// eliminar SummaryCard local
```

- [ ] **Step 6: SectionHeader → SectionTitle** (borrar función local)

- [ ] **Step 7: CollectionRecordItem — trailing `recollection.onSoft`, tile `recollection.soft`**

- [ ] **Step 8: WorkRecordItem — trailing `labor.onSoft`, tile `labor.soft`**

- [ ] **Step 9: Fondo `CoffeeBackground` → `MaterialTheme.colorScheme.background`**

- [ ] **Step 10: Estado vacío → EmptyState("No hay registros para este día")**

- [ ] **Step 11: Build check**

Run: `./gradlew :app:compileDebugKotlin`
Expected: PASS.

---

### Task 11: Refactor ConfigurationScreen

**Files:**
- Modify: `app/src/main/java/com/rojasdev/apprecconproject/ui/configuration/ConfigurationScreen.kt`

- [ ] **Step 1: Imports — `RecconTheme` + componentes; quitar `theme.*`**

- [ ] **Step 2: TopAppBar — container `colorScheme.surface`, título `onSurface`** (neutral, no accent de módulo)

- [ ] **Step 3: SectionHeader → SectionTitle** (usar accents recollection/labor; historial `onSurfaceVariant`)

- [ ] **Step 4: ActivePriceCard — tile `module.soft`, price y botón `module.onSoft`/`module.accent`**

- [ ] **Step 5: WorkPriceItem — icono/price `labor.onSoft`, fecha `onSurfaceVariant`**

- [ ] **Step 6: HistoryPriceCard — textos `onSurfaceVariant`, container `surfaceVariant.copy(alpha=0.4f)`**

- [ ] **Step 7: Dialogs — botón confirm `colorScheme.primary`**

- [ ] **Step 8: EmptyState si `workPrices` vacío**

- [ ] **Step 9: Build check**

Run: `./gradlew :app:compileDebugKotlin`
Expected: PASS.

---

### Task 12: Refactor ReportsScreen

**Files:**
- Modify: `app/src/main/java/com/rojasdev/apprecconproject/ui/reports/ReportsScreen.kt`

- [ ] **Step 1: Imports — `RecconTheme` + componentes; quitar `theme.*`**

- [ ] **Step 2: TopAppBar → RecconTopBar(accent = accounting.accent)**

- [ ] **Step 3: ReportTypeCard — icono `color`, tile `color.copy(alpha = 0.1f)` → pasar accents: mensual `accounting.onSoft`, semanal `OnPastelBlue` → definir `Color(0xFF1976D2)` local o token; anual `labor.onSoft`**

- [ ] **Step 4: Texto introductorio — `onSurfaceVariant`**

- [ ] **Step 5: Build check**

Run: `./gradlew :app:compileDebugKotlin`
Expected: PASS.

---

### Task 13: Verificación final

- [ ] **Step 1: Build completo + lint**

Run: `./gradlew :app:assembleDebug :app:lintDebug`
Expected: BUILD SUCCESSFUL, lint sin errores.

- [ ] **Step 2: Ejecutar app en emulador**

Run: `./gradlew :app:installDebug`
Verificar: light + dark mode (toggle en sistema), 6 pantallas, edge-to-edge (contenido no choca con status/nav bar), estados vacíos visibles.

- [ ] **Step 3: Commit**

```bash
git add docs/plans/2026-08-04-professional-ui.md app/src/main/java/com/rojasdev/apprecconproject/ui
git commit -m "feat(ui): profesionalizar pantallas Compose con tema M3 completo, tokens y componentes"
```

---

## Self-Review

**1. Cobertura:** Theme completo (T1-4) ✓, componentes (T5) ✓, edge-to-edge (T6) ✓, 6 pantallas refactorizadas (T7-12) ✓, estados carga/vacío (T7-8,9,10,11) ✓, verificación (T13) ✓.

**2. Placeholders:** Ninguno — cada paso tiene código concreto o comando exacto.

**3. Tipos:** `RecconModuleColors` (accent/onAccent/soft/onSoft) consistente en HomeModule, BottomAppBars, cards. `HeroSummaryCard`/`HeroStat`/`CompactStatCard`/`SectionTitle`/`RecconTopBar`/`EmptyState`/`LoadingState` firmas fijas y reutilizadas.

**Nota de riesgo:** Los `*` imports de `ui.theme` se eliminan por pantalla. Si alguna pantalla usa `CoffeeSecondary`/`CoffeePrimary`/`OnPastel*`/`Pastel*` en puntos no cubiertos, el build fallará y habrá que mapear al token correcto (rojo→recollection, naranja→labor, verde→accounting).
