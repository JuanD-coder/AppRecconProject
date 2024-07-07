package com.rojasdev.apprecconproject.alert

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.databinding.AlertHelpBinding

class alertHelp(
    var onClickListener: () -> Unit
): DialogFragment() {
    private lateinit var binding: AlertHelpBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertHelpBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cv)
        val builder = AlertDialog.Builder(requireActivity())
            builder.setView(binding.root)


        adsBanner.initLoadAds(binding.banner)

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