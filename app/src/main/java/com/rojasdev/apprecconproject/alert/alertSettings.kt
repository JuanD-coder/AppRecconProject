package com.rojasdev.apprecconproject.alert

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
import com.rojasdev.apprecconproject.controller.dateFormat
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.AlertSettinsBinding

class alertSettings(
    var onClickListener: (SettingEntity) -> Unit
): DialogFragment() {
    private lateinit var binding: AlertSettinsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertSettinsBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvSettings)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        buttons()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.requireDates))
        return dialog
    }

    private fun buttons (){
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

        onClickListener(configAlimentNow)
        onClickListener(configAlimentYes)
    }
}


