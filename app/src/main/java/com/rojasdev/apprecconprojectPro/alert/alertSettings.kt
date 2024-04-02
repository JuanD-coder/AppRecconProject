package com.rojasdev.apprecconprojectPro.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.databinding.AlertSettinsBinding
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.controller.dateFormat
import com.rojasdev.apprecconprojectPro.controller.requireInput
import com.rojasdev.apprecconprojectPro.data.entities.SettingEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class alertSettings(
    var onClickListener: (List<SettingEntity>) -> Unit
): DialogFragment() {
    private lateinit var binding: AlertSettinsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertSettinsBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvSettings)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val myListInput = listOf(
            binding.yesAliment,
            binding.nowAliment
        )

        binding.btReady.setOnClickListener {
            val require = requireInput.validate(myListInput,requireContext())
            if (require){
                dates()
                dismiss()
            }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.requireDates))
        return dialog
    }

    private fun dates() {
        val yesAliment = binding.yesAliment.text.toString()
        val nowAliment = binding.nowAliment.text.toString()

        val configAlimentYes = SettingEntity(
            null,
            "yes",
            yesAliment.toInt(),
            "active",
            dateFormat.main()
        )
        val configAlimentNow = SettingEntity(
            null,
            "no",
            nowAliment.toInt(),
            "active",
            dateFormat.main()
        )

        val newSettings = mutableListOf(
            configAlimentYes,
            configAlimentNow
        )

        onClickListener(newSettings)
    }
}


