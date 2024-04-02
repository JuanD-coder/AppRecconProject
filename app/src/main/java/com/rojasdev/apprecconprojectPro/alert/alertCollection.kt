package com.rojasdev.apprecconprojectPro.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconprojectPro.adapters.adapterSpiner
import com.rojasdev.apprecconprojectPro.databinding.AlertCollectionBinding
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.controller.connected
import com.rojasdev.apprecconprojectPro.controller.controllerCheckBox
import com.rojasdev.apprecconprojectPro.controller.requireInput
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecollectionEntity
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Locale

class alertCollection (
    var collector: RecolectoresEntity,
    var lotes: List<LoteEntity>,
    var onClickListener: (RecollectionEntity) -> Unit,
    val finished: (Boolean) -> Unit
): DialogFragment() {
    var firebase: Boolean = true
    private lateinit var adapterSpiner : adapterSpiner
    private var settingsId: Int? = null
    private lateinit var binding: AlertCollectionBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCollectionBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        initSwich()

        initCoponentView()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun initCoponentView() {
        val myListInput = listOf(
            binding.etKg
        )

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

        binding.btReady.setOnClickListener {
            val require = requireInput.validate(myListInput,requireContext())
            if (require){
                controllerCheckBox.checkBoxFun(
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

        adapterSpiner = adapterSpiner(
            requireContext(),
            lotes
        )

        binding.sLotes.adapter = adapterSpiner
    }

    private fun initSwich() {
        if (connected.isConnected(requireContext())){
            binding.switchDates.isChecked = true
        }else{
            binding.switchDates.isChecked = false
            binding.switchDates.visibility = View.GONE
        }

        binding.switchDates.setOnCheckedChangeListener { buttonView, isChecked ->
            firebase = if (isChecked){
                true
            }else{
                false
            }
        }
    }

    private fun dates() {
        val kg = binding.etKg.text.toString()
        val date = Calendar.getInstance().time
        val formatDate = "EEEE MMMM dd 'del' yyyy '  Hora: ' HH:mm:ss"
        val formato = SimpleDateFormat(formatDate, Locale("es", "CO"))
        val dateFormat = formato.format(date)
        val idLote = binding.sLotes.selectedItemId

        val collection = RecollectionEntity(
            null,
            kg.toDouble(),
            dateFormat.toString(),
            "active",
            collector.id!!,
            settingsId!!,
            idLote.toInt(),
            false
        )

        onClickListener(collection)
        finished(firebase)
        dismiss()
    }
}