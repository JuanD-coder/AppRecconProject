package com.rojasdev.apprecconproject.legacy.alert.report

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import com.rojasdev.apprecconproject.databinding.AlertOpenPdfAppBinding

class alertErrorOpenPdf : androidx.fragment.app.DialogFragment(){
    private lateinit var binding: AlertOpenPdfAppBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertOpenPdfAppBinding.inflate(LayoutInflater.from(context))
                  com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvOpenPdf)
        val builder = AlertDialog.Builder(requireActivity())
            builder.setView(binding.root)

        binding.btnClose.setOnClickListener{
            dismiss()
        }


        val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}