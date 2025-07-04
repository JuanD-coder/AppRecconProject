package com.rojasdev.apprecconproject.fragments.report

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.ReportsAdapter
import com.rojasdev.apprecconproject.alert.messagin.alertMessage
import com.rojasdev.apprecconproject.alert.report.alert_create_pdf
import com.rojasdev.apprecconproject.controller.ReportStorageHelper
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.databinding.FragmentPdfBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val FragmentExcelReport.CREATE_EXCEL_REQUEST_CODE get() = 101

@Suppress("DEPRECATION")
class FragmentExcelReport : Fragment() {
    private var _binding: FragmentPdfBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReportsAdapter

    private var fileDate: String = ""
    private var tempFileName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPdfBinding.inflate(inflater, container, false)
        Log.d("FragmentExcelReport", "Fragment cargado correctamente")
        fileDate = SimpleDateFormat("yyyy-MM-dd", Locale("es", "CO")).format(Date())

        animatedAlert.animatedCv(binding.cardReports)
        loadReports()

        binding.btnGenerate.setOnClickListener {
            createExcel(fileDate)
        }

        return binding.root
    }

    private fun createExcel(date: String) {
        val fileName = "${getString(R.string.report)}_$date.xlsx"
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }

        tempFileName = fileName

        try {
            startActivityForResult(intent, CREATE_EXCEL_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "No se encontró una app para crear archivos $e",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_EXCEL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->

                requireContext().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                try {
                    alert_create_pdf(pdf = "exel", uri = uri, name = tempFileName) {
                        loadReports()
                        openExcelFile(uri)
                    }.show(parentFragmentManager, "dialog")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Error al generar el archivo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded", "UseKtx")
    private fun openExcelFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            alertMessage(
                getString(R.string.txtMessageOneInstall),
                getString(R.string.txtMessageTwoInstall),
                getString(R.string.btnOpenShop),
                getString(R.string.btnFinish),
                getString(R.string.txtErrorOpen)
            ) {
                if (it == "yes") {
                    val marketIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/search?q=excel%20viewer&c=apps")
                    )
                    startActivity(marketIntent)
                }
            }.show(parentFragmentManager, "dialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadReports() {
        val reports = ReportStorageHelper.getReports(requireContext())

        if (reports.isEmpty()) {
            binding.tvEmptyReports.visibility = View.VISIBLE
            binding.imgNoExcel.visibility = View.VISIBLE
            binding.rvReports.visibility = View.GONE
        } else {
            binding.tvEmptyReports.visibility = View.GONE
            binding.rvReports.visibility = View.VISIBLE
            binding.imgNoExcel.visibility = View.GONE

            adapter = ReportsAdapter(reports) { report ->
                val uri = report.uri.toUri()
                val docFile = DocumentFile.fromSingleUri(requireContext(), uri)

                if (docFile?.exists() == true && docFile.canRead()) {
                    openExcelFile(uri)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "El informe ya no está disponible",
                        Toast.LENGTH_SHORT
                    ).show()
                    ReportStorageHelper.removeReport(requireContext(), report)
                    loadReports()
                }
            }

            binding.rvReports.adapter = adapter
            binding.rvReports.layoutManager = LinearLayoutManager(requireContext())
        }
    }

}