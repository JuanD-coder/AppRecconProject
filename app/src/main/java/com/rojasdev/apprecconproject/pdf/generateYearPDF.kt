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
import java.util.Locale

class generateYearPDF(
    var context: Context,
    var resources: Resources,
    var location: () -> Unit
) {

    private val calendar = Calendar.getInstance().time
    private val formatOriginal = SimpleDateFormat("yyyy", Locale("es", "ES"))
    private val dateYear = formatOriginal.format(calendar)

    private var txtFont: Font = FontFactory.getFont("arial", 12f, Font.BOLD)

    @SuppressLint("SuspiciousIndentation")
    fun generateYearPdf(uri: Uri){
        val titlePdf = context.getString(R.string.titlePdfYear)
        fun generatePdfN(uri: Uri){
            CoroutineScope(Dispatchers.IO).launch {
                val query1 =
                    AppDataBase.getInstance(context).RecolectoresDao().getPdfInfo("${dateYear}%", "yes")
                val query2 =
                    AppDataBase.getInstance(context).RecolectoresDao().getPdfInfo("${dateYear}%", "no")
                val yesAlimentTotal = AppDataBase.getInstance(context).RecolectoresDao()
                    .getTotalPdf("${dateYear}%", "yes")
                val noAlimentTotal =
                    AppDataBase.getInstance(context).RecolectoresDao().getTotalPdf("${dateYear}%", "no")
                val queryWork =
                    AppDataBase.getInstance(context).RecolectoresDao().getPdfInfoWork("${dateYear}%")
                val workTotal =
                    AppDataBase.getInstance(context).RecolectoresDao().getTotalPdfWork("${dateYear}%")
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
                    ) {
                        location()
                    }.generatePfd(uri)
                }
            }
        }
    }
}