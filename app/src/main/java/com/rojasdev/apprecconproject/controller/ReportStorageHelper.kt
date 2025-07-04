package com.rojasdev.apprecconproject.controller

import android.content.Context
import androidx.core.content.edit
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.rojasdev.apprecconproject.data.dataModel.GeneratedReport

object ReportStorageHelper {
    private const val PREF_NAME = "report_storage"
    private const val KEY_REPORTS = "generated_reports"

    fun saveReport(context: Context, report: GeneratedReport) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()

        val existingReports = getReports(context).toMutableList()
        existingReports.add(0, report) // agregar al inicio

        val json = gson.toJson(existingReports)
        prefs.edit { putString(KEY_REPORTS, json) }
    }

    fun getReports(context: Context): List<GeneratedReport> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_REPORTS, null) ?: return emptyList()

        val type = object : TypeToken<List<GeneratedReport>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun removeReport(context: Context, reportToRemove: GeneratedReport) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val reports = getReports(context).toMutableList()

        reports.removeAll { it.uri == reportToRemove.uri }

        val json = Gson().toJson(reports)
        prefs.edit { putString(KEY_REPORTS, json) }

        //context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit { remove(KEY_REPORTS) } all remove
    }
}
