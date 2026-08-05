package com.rojasdev.apprecconproject.legacy.alert.settings

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.AlertSettinsBinding

class alertSettings(
    var onClickListener: (com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity) -> Unit
): androidx.fragment.app.DialogFragment() {
    private lateinit var binding: AlertSettinsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertSettinsBinding.inflate(LayoutInflater.from(context))
        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvSettings)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        buttons()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.requireDates))
        return dialog
    }

    private fun buttons (){
        val myListInput = listOf(
            binding.etNameCollector,
            binding.nowAliment
        )

        binding.btReady.setOnClickListener {
                val require = com.rojasdev.apprecconproject.legacy.controller.requireInput.validate(myListInput,requireContext())
                if (require){
                    dates()
                    dismiss()
                }
            }

    }

    private fun dates() {
        val yesAliment = binding.etNameCollector.text.toString()
        val nowAliment = binding.nowAliment.text.toString()

        val configAlimentYes =
            com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity(
                null,
                "yes",
                yesAliment.toInt(),
                "active",
                com.rojasdev.apprecconproject.legacy.controller.dateFormat.main()
            )
        val configAlimentNow =
            com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity(
                null,
                "no",
                nowAliment.toInt(),
                "active",
                com.rojasdev.apprecconproject.legacy.controller.dateFormat.main()
            )

        onClickListener(configAlimentNow)
        onClickListener(configAlimentYes)
    }
}


