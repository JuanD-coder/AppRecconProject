package com.rojasdev.apprecconproject.alert.collection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.dateFormat
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.AlertCollectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class alertCollectionUpdate(
    private val PK_ID_Recollection: Int,
    private val PK_ID_Recolector: Int,
    private val quantity: Double,
    private val nameCollector: String,
    private val onClickListener: (RecollectionEntity) -> Unit,
) : DialogFragment() {
    private lateinit var binding: AlertCollectionBinding
    private var settingsId: Int? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCollectionBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        animatedAlert.animatedInit(binding.cvRecolector)

        binding.tvDescription.text = nameCollector

        val myListInput = listOf(binding.etKg)

        binding.etKg.setText(quantity.toString())

        binding.btReady.setOnClickListener {
            val require = requireInput.validate(myListInput, requireContext())
            if (require) {
                CoroutineScope(Dispatchers.IO).launch {
                    val query = AppDataBase.getInstance(requireContext()).SettingDao().getAliment()
                    if (query.isNotEmpty() && query[0].Id != null) {
                        settingsId = query[0].Id
                        launch(Dispatchers.Main) {
                            dates()
                        }
                    }
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

        val collection = RecollectionEntity(
            PK_ID_Recollection,
            kg.toDouble(),
            dateFormat.main(),
            "active",
            PK_ID_Recolector,
            settingsId!!
        )

        onClickListener(collection)
    }

}