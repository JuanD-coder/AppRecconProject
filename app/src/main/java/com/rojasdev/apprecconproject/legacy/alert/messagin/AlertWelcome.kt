package com.rojasdev.apprecconproject.legacy.alert.messagin

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.AlertWelcomeBinding

class alertWelcome(
    var onClickListener: () -> Unit
): androidx.fragment.app.DialogFragment() {

    private lateinit var binding: AlertWelcomeBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertWelcomeBinding.inflate(LayoutInflater.from(context))
        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvWelcome)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.btInit.setOnClickListener {
            dismiss()
            onClickListener()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.go))
        return dialog
    }

}