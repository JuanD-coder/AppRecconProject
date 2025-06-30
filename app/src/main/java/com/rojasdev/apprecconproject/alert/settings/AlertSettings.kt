package com.rojasdev.apprecconproject.alert.settings

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
) : DialogFragment() {
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
        animatedAlert.onBackAlert(dialog, requireContext(), getString(R.string.requireDates))
        return dialog
    }

    private fun buttons() {
        binding.btReady.setOnClickListener {
            val require = requireInput.validate(listOf(binding.nowAliment), requireContext())
            if (require) {
                saveSettings()
                dismiss()
            }
        }
    }

    private fun saveSettings() {
        val nowAliment = binding.nowAliment.text.toString()
        val setting = SettingEntity(
            Id = null,
            cost = nowAliment.toInt(),
            status = "active",
            date = dateFormat.main()
        )

        onClickListener(setting)
    }
}
