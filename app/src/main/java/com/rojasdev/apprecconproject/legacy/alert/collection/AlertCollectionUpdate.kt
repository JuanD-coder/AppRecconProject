package com.rojasdev.apprecconproject.legacy.alert.collection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import com.rojasdev.apprecconproject.databinding.AlertCollectionBinding

class alertCollectionUpdate(
    private val PK_ID_Recollection: Int,
    private val PK_ID_Recolector: Int,
    private val feeding: String,
    private val quantity: Double,
    private val nameCollector: String,
    private val onClickListener: (com.rojasdev.apprecconproject.legacy.data.entities.RecollectionEntity) -> Unit,
): androidx.fragment.app.DialogFragment() {
    private lateinit var binding: AlertCollectionBinding
    private var settingsId: Int? = null
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCollectionBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
            builder.setView(binding.root)

        com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvRecolector)

        binding.tvDescription.text = nameCollector

        val myListInput = listOf( binding.etKg )

        if(feeding == "yes"){
            binding.cbYes.isChecked = true
        } else {
            binding.cbNo.isChecked = true
        }

        binding.etKg.setText(quantity.toString())

        binding.cbYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbNo.isChecked = false
            }
        }

        binding.cbNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbYes.isChecked = false
            }
        }

        binding.btReady.setOnClickListener {
            val require = com.rojasdev.apprecconproject.legacy.controller.requireInput.validate(myListInput,requireContext())
            if (require){
                com.rojasdev.apprecconproject.legacy.controller.controllerCheckBox.checkBoxFun(
                    binding.cbNo,
                    binding.cbYes,
                    binding.tvAliment,
                    requireContext()
                ){
                    settingsId = it
                    dates()
                }
            }
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun dates() {
        dismiss()
        val kg = binding.etKg.text.toString()

        val collection =
            com.rojasdev.apprecconproject.legacy.data.entities.RecollectionEntity(
                PK_ID_Recollection,
                kg.toDouble(),
                com.rojasdev.apprecconproject.legacy.controller.dateFormat.main(),
                "active",
                PK_ID_Recolector,
                settingsId!!
            )

        onClickListener(collection)
    }

}