package com.rojasdev.apprecconproject.pdf

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.itextpdf.awt.geom.Rectangle
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class
generateMonthPDF(
    var context: Context,
    var resources: Resources,
    var location: () -> Unit
) {
     // Get phone date
     private val calendar = Calendar.getInstance().time
     private val formatOriginal = SimpleDateFormat("yyyy-MM", Locale("es", "CO"))
     private val format = SimpleDateFormat("MMMM 'del Año' yyyy", Locale("es", "CO"))

    private val date = formatOriginal.format(calendar)
    private val fechaParseada: Date = try {
                    formatOriginal.parse(date) ?: Date()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Date()
                }

    val titlePdf = context.getString(R.string.titlePdfMonth)

    fun generatePdfN(uri: Uri){
        CoroutineScope(Dispatchers.IO).launch {
            val query1 = AppDataBase.getInstance(context).RecolectoresDao().getPdfInfo("${date}%", "yes")
            val query2 = AppDataBase.getInstance(context).RecolectoresDao().getPdfInfo("${date}%", "no")
            val yesAlimentTotal = AppDataBase.getInstance(context).RecolectoresDao().getTotalPdf("${date}%", "yes")
            val noAlimentTotal = AppDataBase.getInstance(context).RecolectoresDao().getTotalPdf("${date}%", "no")
            val queryWork = AppDataBase.getInstance(context).RecolectoresDao().getPdfInfoWork("${date}%")
            val workTotal = AppDataBase.getInstance(context).RecolectoresDao().getTotalPdfWork("${date}%")
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