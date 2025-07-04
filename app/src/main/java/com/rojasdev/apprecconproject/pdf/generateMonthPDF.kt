package com.rojasdev.apprecconproject.pdf

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class generateMonthPDF(
    var context: Context,
    var resources: Resources,
    var location: () -> Unit
) {
    // Get phone date
    private val calendar = Calendar.getInstance().time
    private val formatOriginal = SimpleDateFormat("yyyy-MM", Locale("es", "CO"))

    private val date = formatOriginal.format(calendar)
    val titlePdf = context.getString(R.string.titlePdfMonth)

    fun generatePdfN(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val recolectoresDao = AppDataBase.getInstance(context).RecolectoresDao()
            val settingDao = AppDataBase.getInstance(context).SettingDao()
            val datePattern = "${date}%"

            val query1 = recolectoresDao.getPdfInfo(datePattern)
            val query2 = recolectoresDao.getPdfInfo(datePattern)
            val yesAlimentTotal = recolectoresDao.getTotalPdf(datePattern)
            val noAlimentTotal = recolectoresDao.getTotalPdf(datePattern)
            val active = settingDao.getAlimentState("active")
            val archive = settingDao.getAlimentState("archived")

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
                    noAlimentTotal
                ) {
                    location()
                }.generatePfd(uri)
            }
        }
    }

}