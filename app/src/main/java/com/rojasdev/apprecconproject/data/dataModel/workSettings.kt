package com.rojasdev.apprecconproject.data.dataModel

data class workSettings(
    val PK_ID_Trabajo: Int?,
    val cantidad: Int,
    val actividad: String,
    val Fecha: String,
    val Estado: String?,
    val Fk_recolector:Int,
    val Fk_Configuracion:Int,
    val Precio:Int
)
