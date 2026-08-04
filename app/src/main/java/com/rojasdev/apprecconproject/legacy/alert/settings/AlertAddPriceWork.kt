package com.rojasdev.apprecconproject.legacy.alert.settings

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.AlertPricesWorkBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class alertAddPriceWork(
    val onClickListener: (com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity) -> Unit,
    val finished: (Boolean) -> Unit
) : androidx.fragment.app.DialogFragment() {
    private lateinit var binding: AlertPricesWorkBinding
    private var insertCollector = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertPricesWorkBinding.inflate(LayoutInflater.from(context))
        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvSettings)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        buttons()

        com.rojasdev.apprecconproject.legacy.controller.textListener.lister(
            binding.price,
            { add() },
            { finish() }
        )

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun dates(view: View) {
        val priceWork = binding.price.text.toString()
        val nameWork = binding.nameWork.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val count = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(requireContext()).SettingDao().getPriceWorkCount()
            val remainingAttempts = 5 - count
            launch(Dispatchers.Main) {
                if (count >= 5) {
                    com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(view, "Límite de precios alcanzado")
                    dismiss()
                } else {
                    val addUser =
                        com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity(
                            null,
                            nameWork,
                            priceWork.toInt(),
                            "active",
                            com.rojasdev.apprecconproject.legacy.controller.dateFormat.main()
                        )
                    com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(view, "Precio de  $nameWork guardado. Te quedan $remainingAttempts intentos. ")
                    onClickListener(addUser)
                }
            }
        }
    }

    private fun allUser() {
        if (insertCollector) {
            finished(true)
            dismiss()
        } else {
            dismiss()
        }
    }

    private fun add() {
        binding.btReady.text = getString(R.string.btnAddWork)
        val myListInput = listOf(
            binding.nameWork,
            binding.price
        )

        com.rojasdev.apprecconproject.legacy.controller.keyLIstener.start(binding.price) {
            val required = com.rojasdev.apprecconproject.legacy.controller.requireInput.validate(myListInput, requireContext())
            if (required) {
                dates(binding.btReady)
                binding.btReady.setText("")
            }
        }

        binding.btReady.setOnClickListener {
            val required = com.rojasdev.apprecconproject.legacy.controller.requireInput.validate(myListInput, requireContext())
            if (required) {
                dates(it)
                binding.price.setText("")
                binding.nameWork.setText("")
            }
        }
    }

    private fun finish() {
        binding.btReady.text = getString(R.string.finish)
        binding.btReady.setOnClickListener {
            allUser()
        }
    }

    private fun buttons() {
        binding.btnClose.setOnClickListener {
            finished(false)
            dismiss()
        }
    }
}