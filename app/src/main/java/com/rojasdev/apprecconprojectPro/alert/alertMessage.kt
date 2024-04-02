package com.rojasdev.apprecconprojectPro.alert

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.databinding.AlertInfoBinding

class alertMessage(
    private val messageA: String,
    private val messageB: String,
    private val btnYes: String,
    private val btnNo: String,
    val message: String,
    var onClickListener: (String) -> Unit ): DialogFragment() {

    private lateinit var binding: AlertInfoBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertInfoBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvWelcome)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)


        binding.tvMessage.text = message
        binding.btYes.text = btnYes
        binding.btNo.text = btnNo
        binding.tvMessageA.text = messageA
        binding.tvMessageB.text = messageB

        buttons()

        val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.requireDates))
        return dialog
    }

    private fun buttons (){
        binding.btYes.setOnClickListener {
            onClickListener("yes")
            dismiss()
        }
        binding.btNo.setOnClickListener {
            onClickListener("no")
            dismiss()


        }
    }
}