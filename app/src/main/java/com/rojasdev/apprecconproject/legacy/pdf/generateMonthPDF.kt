package com.rojasdev.apprecconproject.legacy.pdf

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import com.rojasdev.apprecconproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
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

    private val date = formatOriginal.format(calendar)
    val titlePdf = context.getString(R.string.titlePdfMonth)

    fun generatePdfN(uri: Uri){
        CoroutineScope(Dispatchers.IO).launch {
            val query1 = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.getInstance(context).RecolectoresDao().getPdfInfo("${date}%", "yes")
            val query2 = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao().getPdfInfo("${date}%", "no")
            val yesAlimentTotal = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao().getTotalPdf("${date}%", "yes")
            val noAlimentTotal = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao().getTotalPdf("${date}%", "no")
            val queryWork = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao().getPdfInfoWork("${date}%")
            val workTotal = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao().getTotalPdfWork("${date}%")
            val active = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).SettingDao().getAlimentState("active")
            val archive = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).SettingDao().getAlimentState("archived")
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