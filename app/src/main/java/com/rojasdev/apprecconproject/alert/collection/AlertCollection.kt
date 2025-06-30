package com.rojasdev.apprecconproject.alert.collection

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.dateFormat
import com.rojasdev.apprecconproject.controller.keyLIstener
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.AlertCollectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class alertCollection(
    private var collector: RecolectoresEntity,
    var onClickListener: (RecollectionEntity) -> Unit
) : DialogFragment() {
    private var settingsId: Int? = null
    private lateinit var binding: AlertCollectionBinding
    private lateinit var tts: TextToSpeech

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCollectionBinding.inflate(LayoutInflater.from(context))
        tts = TextToSpeech(context) {}
        animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        binding.tvDescription.text = collector.name

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

        keyLIstener.start(binding.etKg) {
            validateAndProcessInput(myListInput)
        }

        binding.btReady.setOnClickListener {
            validateAndProcessInput(myListInput)
        }

    }

    private fun validateAndProcessInput(myListInput: List<TextInputEditText>) {
        val require = requireInput.validate(myListInput, requireContext())
        if (require) {
            CoroutineScope(Dispatchers.IO).launch {
                val query = AppDataBase.getInstance(requireContext()).SettingDao().getAliment()
                if (query.isNotEmpty() && query[0].Id != null) {
                    settingsId = query[0].Id
                    launch(Dispatchers.Main) {
                        dates()
                        dismiss()
                    }
                }
            }
        }
    }

    private fun dates() {
        val kg = binding.etKg.text.toString()

        val collection = RecollectionEntity(
            id = null,
            total = kg.toDouble(),
            date = dateFormat.main(),
            state = "active",
            collector = collector.id!!,
            setting = settingsId!!
        )

        onClickListener(collection)
    }
}