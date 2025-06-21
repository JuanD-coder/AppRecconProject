package com.rojasdev.apprecconproject.data.dataModel

data class allWorkAndCollector(
    val PK_ID_Recolector: Int,
    val name_recolector : String?,
    val PK_ID_Trabajo: Int,
    val cantidad : Double,
    val result: Double,
    val Precio: Double,
    val Fecha: String?,
    val Fk_Configuracion: Int,
    val actividad : String?,
)
