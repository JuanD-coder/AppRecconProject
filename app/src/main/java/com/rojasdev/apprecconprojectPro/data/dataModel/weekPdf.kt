package com.rojasdev.apprecconprojectPro.data.dataModel

data class weekPdf(
    val PK_ID_Recolector: Int,
    val name_recolector : String,
    val result: Double,
    val total: Double,
    val Estado: String,
    val Alimentacion: String,
    val Fecha: String?,
    val Fk_Configuracion: Int
)
