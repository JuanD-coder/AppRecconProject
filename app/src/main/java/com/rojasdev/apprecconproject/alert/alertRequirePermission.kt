package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.databinding.AlertRequirePermissionBinding
import com.rojasdev.apprecconproject.databinding.AlertWelcomeBinding

class alertRequirePermission(
    var onClickListener: () -> Unit
): DialogFragment() {

    private lateinit var binding: AlertRequirePermissionBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertRequirePermissionBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvWelcome)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)


        binding.btInit.setOnClickListener {
            dismiss()
            onClickListener()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.go))
        return dialog
    }

}