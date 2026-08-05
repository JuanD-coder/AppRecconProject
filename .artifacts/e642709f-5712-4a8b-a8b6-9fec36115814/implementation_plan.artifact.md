# Refinamiento de AnalyticsScreen: Legacy Essence First

Este plan detalla los ajustes necesarios para que la pantalla de Analíticas (`AnalyticsScreen.kt`) sea una evolución visual fiel del `fragment_informe.xml` original, respetando la regla crítica de mantener el "alma" del paquete `legacy`.

## User Review Required

> [!IMPORTANT]
> Se reemplazarán los colores "Pastel" actuales por los tonos intensos originales (`Hippie_Green`, `Cinnabar`). Esto hará que la interfaz se sienta más "pesada" y técnica, alineada con la versión XML que los usuarios ya conocen.

## Proposed Changes

### [Componente] Tematización y ADN Visual
Ajuste de los cimientos para soportar la identidad legacy.

#### [MODIFY] [Color.kt](file:///C:/Users/Usuario/Project/AndroidStudioProjects/AppRecconProject/app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Color.kt)
- Agregar constantes para colores legacy: `HippieGreenLegacy` (#4F864F), `CinnabarLegacy` (#E63933), `TomThumbLegacy` (#065C19).
- Actualizar el token de `accounting` para usar estos colores en lugar de los verdes estándar de Material.

#### [MODIFY] [Type.kt](file:///C:/Users/Usuario/Project/AndroidStudioProjects/AppRecconProject/app/src/main/java/com/rojasdev/apprecconproject/ui/theme/Type.kt)
- Configurar la `FontFamily` para `Comfortaa` y asignarla a los estilos de texto (Headline, Title, Body).

---

### [Componente] Pantalla de Analíticas
Rediseño de la pantalla para "traducir" el XML a Compose.

#### [MODIFY] [AnalyticsScreen.kt](file:///C:/Users/Usuario/Project/AndroidStudioProjects/AppRecconProject/app/src/main/java/com/rojasdev/apprecconproject/ui/analytics/AnalyticsScreen.kt)
- **TopAppBar**: Cambiar color a `HippieGreenLegacy` y usar fuente `Comfortaa`.
- **Header de Totales**: Reconstruir el área superior para que use el fondo verde sólido, con textos de Kg y Dinero en blanco, grandes (35sp) y centrados, imitando el diseño de `tvShowDates` y `tvShowPay`.
- **Calendario**:
    - Aplicar `CinnabarLegacy` exclusivamente a la columna de los domingos.
    - Usar la fuente `Comfortaa` en los nombres de los meses y días.
    - Añadir divisores negros sutiles (`0.5.dp`) para evocar la cuadrícula del original.
- **Iconografía**: Reemplazar iconos genéricos por `ic_recolector` y `ic_vacio` usando `painterResource`.

## Verification Plan

### Manual Verification
- **Contraste Visual**: Abrir `AnalyticsScreen` y comparar lado a lado con un screenshot de la versión Legacy (`fragment_informe.xml`).
- **Navegación**: Verificar que el cambio de meses mantenga la integridad visual del calendario.
- **Dark Mode**: Asegurar que los colores legacy tengan una variante legible en modo oscuro (ajustando la luminancia si es necesario).
