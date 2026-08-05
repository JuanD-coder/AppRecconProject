# Reglas de Desarrollo del Proyecto

## Principio de Continuidad Legacy
**TODO** cambio o nueva funcionalidad creada con tecnologías modernas (Jetpack Compose, Hilt, Clean Architecture) **DEBE** respetar la esencia del directorio `legacy`.

1. **Comportamiento Idéntico:** La lógica de negocio y el flujo de usuario deben ser casi idénticos a la versión original para no confundir a los usuarios acostumbrados.
2. **Interfaz Basada en el Origen:** El diseño en Compose debe basarse fielmente en los layouts XML originales (`activity_main_module.xml`, etc.), mejorando la estética con Material Design 3 pero manteniendo la estructura y jerarquía visual conocida.
3. **Referencia Obligatoria:** Antes de codificar cualquier vista nueva, es obligatorio analizar su contraparte en el paquete `legacy`.
