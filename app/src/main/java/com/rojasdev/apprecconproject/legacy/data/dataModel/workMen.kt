package com.rojasdev.apprecconproject.legacy.data.dataModel

data class workMen(
    val PK_ID_Recolector: Int,
    val name_recolector : String,
    val PK_ID_Trabajo: Int,
    val cantidad : Int,
    val result: Double,
    val Precio: Double,
    val Estado: String,
    val Alimentacion: String,
    val Fecha: String,
    val Fk_Configuracion: Int
)
