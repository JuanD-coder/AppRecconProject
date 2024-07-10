package com.rojasdev.apprecconproject.alert.settings

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.customSnackBar
import com.rojasdev.apprecconproject.controller.dateFormat
import com.rojasdev.apprecconproject.controller.keyLIstener
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.controller.textListener
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.AlertPricesWorkBinding

class alertAddPriceWork(
    val onClickListener: (SettingEntity) -> Unit,
    val finished: (Boolean) -> Unit
): DialogFragment() {
    var pricesInsert = 4
    private lateinit var binding: AlertPricesWorkBinding
    private var insertCollector = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertPricesWorkBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvSettings)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        buttons()

        textListener.lister(
            binding.price,
            { add() },
            { finish() }
        )

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun dates(view: View) {
        if (pricesInsert != 0){
            pricesInsert = pricesInsert - 1
            insertCollector = true
            val priceWork = binding.price.text.toString()
            var nameWork = binding.nameWork.text.toString()
            val addUser = SettingEntity(
                null,
                nameWork,
                priceWork.toInt(),
                "active",
                dateFormat.main()
            )
            customSnackBar.showCustomSnackBar(view,"Precio de  $nameWork guardado. pruedes guardar $pricesInsert mas")
            onClickListener(addUser)
        }else{
            customSnackBar.showCustomSnackBar(view,"Limite de precios")
            dismiss()
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

    private fun add(){
        binding.btReady.text = getString(R.string.btnAddWork)
        val myListInput = listOf(
            binding.nameWork,
            binding.price
        )

        keyLIstener.start(binding.price){
            val required = requireInput.validate(myListInput,requireContext())
            if (required){
                dates(binding.btReady)
                binding.btReady.setText("")
            }
        }

        binding.btReady.setOnClickListener {
            val required = requireInput.validate(myListInput,requireContext())
            if (required){
                dates(it)
                binding.price.setText("")
                binding.nameWork.setText("")
            }
        }
    }
    private fun finish(){
        binding.btReady.text = getString(R.string.finish)
        binding.btReady.setOnClickListener {
            allUser()
        }
    }

    private fun buttons (){
        binding.btnClose.setOnClickListener{
            finished(false)
            dismiss()
        }
    }
}