package com.rojasdev.apprecconproject.legacy.alert.collection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.AlertDeleteBinding

class alertDeleteCollector(
    private val nameCollector :String,
    private val styleApp : Boolean?,
    val onClickListener: () -> Unit ): androidx.fragment.app.DialogFragment() {

    private lateinit var binding: AlertDeleteBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertDeleteBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
            builder.setView(binding.root)

        com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvRecolector)

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

        contextTheme()

        val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun contextTheme() {
        var color: Int? = null
        com.rojasdev.apprecconproject.legacy.controller.controllerTheme.main(requireContext(),
            day = {
                color = ContextCompat.getColor(requireContext(), R.color.Orange)
            },
            night = {
                color = ContextCompat.getColor(requireContext(), R.color.OrangeDark)
            })

        if (styleApp == true){
            binding.btNo.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.brYes.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.tvDescription.backgroundTintList = ColorStateList.valueOf(color!!)

            binding.btnClose.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.btnClose.invalidate()
        }else{
            binding.btnClose.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.Dark_Tan))
            binding.btnClose.invalidate()
        }
    }
}