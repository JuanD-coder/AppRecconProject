package com.rojasdev.apprecconproject.data.dataModel

data class pdfModel(
    val PK_ID_Recolector: Int,
    val name_recolector : String,
    val Estado: String,
    val Fecha: String?,
    val actividad: String?,
    val Fk_Configuracion: Int,
    val result: Double,
    val total: Double
)
