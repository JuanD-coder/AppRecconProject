package com.rojasdev.apprecconproject.legacy.alert.collection

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import com.rojasdev.apprecconproject.databinding.AlertCollectionBinding

class alertCollection (
    private var collector: com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity,
    var onClickListener: (com.rojasdev.apprecconproject.legacy.data.entities.RecollectionEntity) -> Unit
): androidx.fragment.app.DialogFragment() {
    private var settingsId: Int? = null
    private lateinit var binding: AlertCollectionBinding
    private lateinit var tts: TextToSpeech

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCollectionBinding.inflate(LayoutInflater.from(context))
        tts = TextToSpeech(context){}
        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        binding.tvDescription.text = collector.name

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

        buttons()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun buttons() {
            binding.btnClose.setOnClickListener {
                dismiss()
            }

            val myListInput = listOf(
                binding.etKg
            )

            com.rojasdev.apprecconproject.legacy.controller.keyLIstener.start(binding.etKg){
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
                        dismiss()
                    }
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
                        dismiss()
                    }
                }
            }

    }

    private fun dates() {
        val kg = binding.etKg.text.toString()

        val collection =
            com.rojasdev.apprecconproject.legacy.data.entities.RecollectionEntity(
                null,
                kg.toDouble(),
                com.rojasdev.apprecconproject.legacy.controller.dateFormat.main(),
                "active",
                collector.id!!,
                settingsId!!
            )

        onClickListener(collection)
    }
}