package com.rojasdev.apprecconproject.customCalendar

import java.util.Calendar

object montAndYear {
    val month = arrayOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    fun listYear(): Array<Int> {
        val añoActual = Calendar.getInstance().get(Calendar.YEAR)
        return (2023..añoActual).toList().toTypedArray()
    }

    val year = arrayOf(
        2023,2024
    )
}