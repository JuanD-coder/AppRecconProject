package com.rojasdev.apprecconproject.alert

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.databinding.AlertDeleteBinding

class alertDeleteCollector(
    private val nameCollector :String,
    val onClickListener: () -> Unit ): DialogFragment() {

    private lateinit var binding: AlertDeleteBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertDeleteBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
            builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        animatedAlert.animatedInit(binding.cvRecolector)

        binding.tvDetailDelete.text = nameCollector

        binding.brYes.setOnClickListener {
            onClickListener()
            dismiss()
        }

        binding.btNo.setOnClickListener {
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