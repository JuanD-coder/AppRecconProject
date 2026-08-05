# Tareas de Modernización UI (Legacy Essence First)

## 0. Cimientos del Sistema de Diseño
- [ ] Ajustar `Color.kt` con constantes exactas de `colors.xml` (Hippie_Green, Thunderbird, Orange, Cinnabar)
- [ ] Configurar tipografía `Comfortaa` en `Type.kt`
- [ ] Crear tokens de espaciado en `Dimens.kt`

## 1. Pantalla de Analíticas (AnalyticsScreen)
*Basada en `fragment_informe.xml` y `activity_informes.xml`*
- [ ] Implementar `GetAnalyticsDataUseCase` y refactorizar ViewModel
- [ ] Rediseñar Header Hero con fondo `Hippie_Green` y textos grandes (35sp)
- [ ] Refinar Calendario con domingos en `Cinnabar` y fuente `Comfortaa`
- [ ] Integrar iconos legacy (`ic_recolector`, `ic_vacio`)

## 2. Pantalla Principal (MainScreen)
*Basada en `activity_main_module.xml`*
- [ ] Evolucionar el diseño de módulos para reflejar la jerarquía original
- [ ] Actualizar `PriceHeader` con la estética legacy

## 3. Pantalla de Recolección (RecollectionScreen)
*Basada en `activity_recolection.xml`, `activity_recolection_detail.xml` y `fragment_collectors_and_collecion.xml`*
- [ ] Aplicar paleta `Thunderbird` (Rojo intenso)
- [ ] Recrear listas de personal y totales con estética legacy

## 4. Pantalla de Configuración (ConfigurationScreen)
*Basada en `activity_settings.xml`*
- [ ] Adaptar el estilo de ajustes a Material 3 manteniendo el orden y claridad original

## 5. Reportes (ReportsScreen)
*Basada en `fragment_pdf.xml`*
- [ ] Estilizar la visualización y exportación de reportes
