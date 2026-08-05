package com.rojasdev.apprecconproject.legacy.alert.report

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.AlertCreatePdfBinding
import java.io.File

class alert_create_pdf(
    private var pdf: String,
    private var uri: Uri,
    var finished: () -> Unit): androidx.fragment.app.DialogFragment() {

    private lateinit var binding: AlertCreatePdfBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AlertCreatePdfBinding.inflate(LayoutInflater.from(context))

        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvWelcome)
        createFolderPermission()

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val animator = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, 100)
            animator.duration = 5000
            animator.start()

        binding.progressBar.isIndeterminate = true

        when (pdf) {
            getString(R.string.year) -> {binding.textView.text = getString(R.string.yearLoadingPdf)
                starTimer {
                    com.rojasdev.apprecconproject.legacy.pdf.generateYearPDF(
                        requireContext(),
                        resources
                    ) {
                        dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        dismiss()
                        finished()
                    }.generatePdfN(uri)
                }
            }

            getString(R.string.week) -> {
                binding.textView.text = getString(R.string.weekLoadingPdf)
                starTimer {
                    com.rojasdev.apprecconproject.legacy.pdf.generatePdfSemanal(
                        requireContext(),
                        resources
                    ) {
                        dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        dismiss()
                        finished()
                    }.generatePdfN(uri)
                }
            }

            else -> {
                binding.textView.text = getString(R.string.monthLoadingPdf)
                starTimer {
                    com.rojasdev.apprecconproject.legacy.pdf.generateMonthPDF(
                        requireContext(),
                        resources
                    ) {
                        dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        dismiss()
                        finished()
                    }.generatePdfN(uri)
                }
            }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.onBackAlert(dialog,requireContext(),"")
        dialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        return dialog
    }

    private fun starTimer(ready : () -> Unit) {
        object: android.os.CountDownTimer(900,1){
            override fun onTick(p0: Long) {}
            override fun onFinish() {
               ready()
            }
        }.start()
    }

    @SuppressLint("SetWorldReadable", "SetWorldWritable", "SuspiciousIndentation")
    private fun createFolderPermission() {
        // Ruta donde se creará la carpeta
        val pdfFolderPath = "/Users/Cristian/AndroidStudioProjects/AppRecconProject/mica"

        // Crea la carpeta si no existe
        val pdfFolder = File(pdfFolderPath)
            pdfFolder.mkdirs()

        // Otorga permisos de lectura y escritura a la carpeta
        pdfFolder.setReadable(true, false)
        pdfFolder.setWritable(true, false)

    }
}