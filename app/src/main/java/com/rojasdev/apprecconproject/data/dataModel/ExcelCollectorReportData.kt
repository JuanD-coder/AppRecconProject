package com.rojasdev.apprecconproject.data.dataModel

data class ExcelCollectorReportData(
    val id: Int,
    val name: String,
    val lunes: Int,
    val martes: Int,
    val miercoles: Int,
    val jueves: Int,
    val viernes: Int,
    val sabado: Int,
    val totalKg: Int,
    val price: Int
)

data class RecolectorConRecoleccionRaw(
    val id: Int,
    val name: String,
    val date: String,
    val cantidad: Int,
    val price: Int
)

data class GeneratedReport(
    val name: String,
    val uri: String,
    val date: String
)

