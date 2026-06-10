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

private val dataBase: Any = TODO()

class generateYearPDF(
    var context: Context,
    var resources: Resources,
    var location: () -> Unit
) {

    private val calendar = Calendar.getInstance().time
    private val formatOriginal = SimpleDateFormat("yyyy", Locale("es", "ES"))
    private val dateYear = formatOriginal.format(calendar)

        val titlePdf = context.getString(R.string.titlePdfYear)
        fun generatePdfN(uri: Uri){
            CoroutineScope(Dispatchers.IO).launch {
                val query1 =
                    _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao().getPdfInfo("${dateYear}%", "yes")
                val query2 =
                    _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao().getPdfInfo("${dateYear}%", "no")
                val yesAlimentTotal = _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao()
                    .getTotalPdf("${dateYear}%", "yes")
                val noAlimentTotal =
                    _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao().getTotalPdf("${dateYear}%", "no")
                val queryWork =
                    _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao().getPdfInfoWork("${dateYear}%")
                val workTotal =
                    _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).RecolectoresDao().getTotalPdfWork("${dateYear}%")
                val active = _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).SettingDao().getAlimentState("active")
                val archive = _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(context).SettingDao().getAlimentState("archived")
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