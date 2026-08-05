# 🚀 Hoja de Ruta: Modernización de AppRecconProject

Este documento detalla los pasos necesarios para transformar la aplicación de una arquitectura basada en Activities/XML a un stack moderno de Android (Jetpack Compose, Clean Architecture, Hilt).

## 0. Fase de Preparación (Limpieza y Cimientos)
- [ x ] **Configurar Version Catalog (`libs.versions.toml`):** Mover todas las dependencias de los archivos `build.gradle` a un catálogo centralizado para gestionar versiones de forma moderna.
- [  ] **Habilitar Edge-to-Edge:** Configurar la app para que el contenido se dibuje debajo de las barras de estado y navegación desde el inicio (`enableEdgeToEdge()`).
- [ ] **Configurar Hilt (DI):** Instalar y configurar Hilt para la inyección de dependencias en toda la app.
- [ ] **Migrar a Material 3:** Actualizar el sistema de diseño para usar componentes modernos y colores dinámicos.

## 1. Capa de Datos y Dominio (Clean Architecture)
- [ ] **Modelos de Dominio y Mappers:** Crear clases de datos puras (Kotlin) que representen el negocio, separadas de las `@Entity` de Room. Implementar mappers para convertir entre capas.
- [ ] **Refactorizar Room:** Asegurar que los DAOs devuelvan `Flow` en lugar de `LiveData` o llamadas síncronas.
- [ ] **Patrón Repository:** Implementar interfaces de repositorio en la capa de dominio y sus implementaciones en la capa de datos.
- [ ] **Casos de Uso (Use Cases):** Extraer la lógica de negocio de las Activities/Controllers a clases pequeñas y testeables (ej. `SaveRecolectionUseCase`).

## 2. Capa de Presentación (Jetpack Compose)
- [ ] **Sistema de Diseño Compose:** Crear `Theme.kt`, `Color.kt` y `Typography.kt` basados en Material 3.
- [ ] **Migración Progresiva de Pantallas:** 
    - Empezar por pantallas sencillas (`ActivitySettings`).
    - Continuar con listas y detalles (`ActivityWork`, `ActivityRecolection`).
- [ ] **Componentes Reutilizables:** Crear una librería interna de componentes (botones, cards de recolección, diálogos personalizados).

## 3. Arquitectura Reactiva (MVVM + Flow)
- [ ] **Migrar LiveData a StateFlow:** Usar `StateFlow` y `SharedFlow` en los ViewModels para manejar el estado de la UI de forma reactiva.
- [ ] **UI State Pattern:** Definir una única clase de estado por pantalla (ej. `WorkUiState`) que incluya estados de carga, error y éxito.
- [ ] **Lifecycle-aware collection:** Asegurar que la recolección de flujos en Compose use `collectAsStateWithLifecycle()`.

## 4. Navegación Moderna
- [ ] **Single Activity Architecture:** Migrar hacia una única `MainActivity` que contenga el `NavHost`.
- [ ] **Navigation Compose (Type-Safe):** Implementar la navegación usando objetos y clases serializables (disponible en Navigation 2.8.0+).

---

## 💡 Sugerencias Estratégicas

1. **Modularización Gradual:** 
   - Una vez establecida la arquitectura, considera separar la app en módulos: `:core` (utilidades), `:data` (repositorios/DB), y `:feature:X` para cada funcionalidad.

2. **Reemplazo de iTextG:**
   - La librería `itextg` es antigua. Evalúa migrar a la API nativa `PdfDocument` de Android para generar informes, lo que reducirá el tamaño del APK y posibles problemas de licencias.

3. **Pruebas Unitarias (Testing):**
   - Con Clean Architecture, ahora es posible (y recomendado) testear los `UseCases` y `ViewModels` sin necesidad de emuladores, usando JUnit y MockK.

4. **Eliminar Código Heredado (Legacy):**
   - A medida que migres pantallas a Compose, elimina los archivos XML de layout y las Activities correspondientes para reducir la deuda técnica.
