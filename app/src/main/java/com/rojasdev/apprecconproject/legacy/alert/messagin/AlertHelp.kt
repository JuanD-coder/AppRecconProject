package com.rojasdev.apprecconproject.legacy.alert.messagin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import com.rojasdev.apprecconproject.databinding.AlertHelpBinding

class alertHelp(
    var onClickListener: () -> Unit
): androidx.fragment.app.DialogFragment() {
    private lateinit var binding: AlertHelpBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertHelpBinding.inflate(LayoutInflater.from(context))
        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cv)
        val builder = AlertDialog.Builder(requireActivity())
            builder.setView(binding.root)


        com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        binding.lyChat.setOnClickListener {
            onClickListener()
            dismiss()
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}