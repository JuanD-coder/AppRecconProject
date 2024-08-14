package com.rojasdev.apprecconproject.pdf

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class generatePdfSemanal(
    var context: Context,
    var resources: Resources, //resourses permite acceder a la imagen para el pdf
    var location: () -> Unit //location corresponde a la ubicacion del pdf
) {
    //dateWeek me guarda la fecha de inicio y de fin de la semana
    private var dateWeek = getStarAndEndWeek()

    private val titlePdf = context.getString(R.string.titlePdfYear)

    private fun getStarAndEndWeek(): Pair<String, String> {
        val calendar = Calendar.getInstance()

        // Encontrar el día de la semana actual
        val diaSemanaActual = calendar.get(Calendar.DAY_OF_WEEK)

        // Calcular la cantidad de días para llegar al lunes anterior (considerando que domingo es 1 y lunes es 2)
        val days = when (diaSemanaActual) {
            Calendar.MONDAY -> {
                0
            }
            Calendar.TUESDAY -> {
                1
            }
            Calendar.WEDNESDAY -> {
                2
            }
            Calendar.THURSDAY -> {
                3
            }
            Calendar.FRIDAY -> {
                4
            }
            Calendar.SATURDAY -> {
                5
            }
            else -> {
                6
            }
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd ",Locale.getDefault())
        val currentDate = Calendar.getInstance()

        val endWeek = dateFormat.format(currentDate.time)

        currentDate.add(Calendar.DATE, - days)
        val startWeek = dateFormat.format(currentDate.time)

        val horaStar = "00:00:00"
        val horaEnd = "23:59:59"

        return Pair(startWeek+horaStar, endWeek.toString()+horaEnd)
    }


    private fun finish(document: Document) {
        val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 16f, BaseColor.BLACK)
        val titlePdf = Paragraph("\n${context.getString(R.string.pdfFinish)}", titleFont)
            titlePdf.alignment = Element.ALIGN_CENTER
        document.add(titlePdf)
        document.close()

        location()
    }


    fun generatePdfN(uri: Uri){
        CoroutineScope(Dispatchers.IO).launch {
            val query1 = AppDataBase.getInstance(context).RecolectoresDao()
                .getWeekPdf(
                    dateWeek.first,
                    dateWeek.second,
                    "yes"
                )
            val query2 = AppDataBase.getInstance(context).RecolectoresDao()
                .getWeekPdf(
                    dateWeek.first,
                    dateWeek.second,
                    "no"
                )
            val yesAlimentTotal = AppDataBase.getInstance(context).SettingDao()
                .getTotalPdfWeek(
                    dateWeek.first,
                    dateWeek.second,
                    "yes"
                )
            val noAlimentTotal = AppDataBase.getInstance(context).SettingDao()
                .getTotalPdfWeek(
                    dateWeek.first,
                    dateWeek.second,
                    "no"
                )
            val queryWork = AppDataBase.getInstance(context).RecolectoresDao()
                .getWeekPdfWork(
                    dateWeek.first,
                    dateWeek.second
                )
            val workTotal = AppDataBase.getInstance(context).SettingDao()
                .getTotalPdfWeekWork(
                    dateWeek.first,
                    dateWeek.second
                )

            val active = AppDataBase.getInstance(context).SettingDao().getAlimentState("active")
            val archive = AppDataBase.getInstance(context).SettingDao().getAlimentState("archived")
            launch(Dispatchers.Main) {
                generatePDF(
                    titlePdf,
                    context,
                    resources,
                    active,
                    archive,
                    query1,
                    query2,
                    yesAlimentTotal,
                    noAlimentTotal,
                    queryWork,
                    workTotal,
                ){
                    location()
                }.generatePfd(uri)
            }
        }
    }


}