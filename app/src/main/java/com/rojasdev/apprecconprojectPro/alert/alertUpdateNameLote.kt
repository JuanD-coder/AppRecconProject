package com.rojasdev.apprecconprojectPro.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconprojectPro.ActivityLotes
import com.rojasdev.apprecconprojectPro.ActivityRecolectionDetail
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.controller.keyLIstener
import com.rojasdev.apprecconprojectPro.controller.requireInput
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.databinding.AlertRecolectonBinding
import com.rojasdev.apprecconprojectPro.databinding.AlertUpdateSettingBinding

class alertUpdateNameLote (
    private var idLote: Int,
    var name: String,
    var onClickListener: (LoteEntity) -> Unit): DialogFragment() {

    private lateinit var binding: AlertRecolectonBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertRecolectonBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val myListInput = listOf(
            binding.yesAddRecolector,
            binding.type
        )

        binding.btAddRecolector.setOnClickListener {
            if (requireInput.validate(myListInput,requireContext())) {
                dates()
                dismiss()
            }
        }

        keyLIstener.start(binding.yesAddRecolector){
            val require = requireInput.validate(myListInput,requireContext())
            if (require){
                dates()
                dismiss()
            }
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        initView()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun initView() {
        binding.tilInputAddType.visibility = View.VISIBLE
    }

    private fun dates() {
        val newName = binding.yesAddRecolector.text.toString()
        val newType = binding.type.text.toString()

        val editNameCollector = LoteEntity(
            idLote,
            newName,
            newType,
            true,
            1,
        )
        onClickListener(editNameCollector)

        startActivity(Intent(requireContext(), ActivityLotes::class.java))
    }
}