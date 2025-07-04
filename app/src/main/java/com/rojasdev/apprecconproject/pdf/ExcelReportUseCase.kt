package com.rojasdev.apprecconproject.pdf

import android.content.Context
import android.net.Uri
import android.util.Log
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.ExcelCollectorReportData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExcelReportUseCase(var context: Context, var location: () -> Unit) {

    private var dateWeek = getStartAndEndOfWeek()

    fun getDateExcel(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDataBase.getInstance(context).RecolectoresDao()
            val rawData = dao.getWeekExel(dateWeek.first, dateWeek.second)

            val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val sdfDay = SimpleDateFormat("EEEE", Locale("es", "ES"))

            val reportData = rawData
                .groupBy { it.id to it.name }
                .map { (key, items) ->
                    val (id, name) = key

                    val diasMap = items.groupBy {
                        val day = sdfInput.parse(it.date)
                        sdfDay.format(day!!).lowercase()
                    }.mapValues { it.value.sumOf { item -> item.cantidad } }

                    val lunes = diasMap["lunes"] ?: 0
                    val martes = diasMap["martes"] ?: 0
                    val miercoles = diasMap["miércoles"] ?: 0
                    val jueves = diasMap["jueves"] ?: 0
                    val viernes = diasMap["viernes"] ?: 0
                    val sabado = diasMap["sábado"] ?: 0
                    val totalKg = lunes + martes + miercoles + jueves + viernes + sabado

                    ExcelCollectorReportData(
                        id = id,
                        name = name,
                        lunes = lunes,
                        martes = martes,
                        miercoles = miercoles,
                        jueves = jueves,
                        viernes = viernes,
                        sabado = sabado,
                        totalKg = totalKg,
                        price = items.first().price
                    )
                }

            withContext(Dispatchers.Main) {
                Log.d("ExcelReportUseCase", "Llamando a ExcelGenerator")
                ExcelGenerator(context = context, data = reportData, dateWeek = dateWeek) {
                    location()
                }.generate(uri = uri)
            }
        }
    }

    private fun getStartAndEndOfWeek(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Start of the week (Monday)
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        val offset = if (currentDay == Calendar.SUNDAY) 6 else currentDay - Calendar.MONDAY
        calendar.add(Calendar.DATE, -offset)
        val startDate = dateFormat.format(calendar.time)

        // End of the week (Saturday)
        calendar.add(Calendar.DATE, 6)
        val endDate = dateFormat.format(calendar.time)

        return startDate to endDate
    }
}