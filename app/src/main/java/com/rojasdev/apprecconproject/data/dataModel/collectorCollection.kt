package com.rojasdev.apprecconproject.data.dataModel

data class collectorCollection(
    val PK_ID_Recolector: Int,
    val name_recolector : String,
    val id_temporal_en_recolector: Int,
    val PK_ID_Recoleccion: Int,
    val Cantidad : Double,
    val result: Double,
    val Precio: Double,
    val Estado: String,
    val Fecha: String,
    val Fk_Configuracion: Int
)
