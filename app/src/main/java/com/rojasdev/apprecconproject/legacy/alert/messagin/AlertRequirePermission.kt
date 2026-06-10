package com.rojasdev.apprecconproject.legacy.alert.messagin

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.AlertRequirePermissionBinding

class alertRequirePermission(
    var onClickListener: () -> Unit
): androidx.fragment.app.DialogFragment() {

    private lateinit var binding: AlertRequirePermissionBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertRequirePermissionBinding.inflate(LayoutInflater.from(context))
        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvWelcome)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)


        binding.btInit.setOnClickListener {
            dismiss()
            onClickListener()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.go))
        return dialog
    }

}