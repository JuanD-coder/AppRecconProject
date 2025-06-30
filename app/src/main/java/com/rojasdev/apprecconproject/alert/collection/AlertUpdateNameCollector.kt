package com.rojasdev.apprecconproject.alert.collection

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.ActivityRecolectionDetail
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.controllerTheme
import com.rojasdev.apprecconproject.controller.keyLIstener
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.AlertUpdateSettingBinding

class alertUpdateNameCollector(
    private var idCollector: Int,
    var name: String,
    var styleApp: Boolean?,
    var onClickListener: (RecolectoresEntity) -> Unit
) : DialogFragment() {

    private lateinit var binding: AlertUpdateSettingBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertUpdateSettingBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvSettings)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        val myListInput = listOf(
            binding.etNameCollector
        )

        binding.btReady.setOnClickListener {
            if (requireInput.validate(myListInput, requireContext())) {
                dates()
                dismiss()
            }
        }

        keyLIstener.start(binding.etNameCollector) {
            val require = requireInput.validate(myListInput, requireContext())
            if (require) {
                dates()
                dismiss()
            }
        }

        binding.fbClose.setOnClickListener {
            dismiss()
        }

        initView()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun initView() {
        binding.etNameCollector.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        binding.etNameCollector.setText(name)
        binding.tvDescription.text = getString(R.string.updateName)
        binding.tilNameCollector.setStartIconDrawable(R.drawable.ic_recolector)
        contextTheme()
    }

    private fun dates() {
        val newName = binding.etNameCollector.text.toString()

        val editNameCollector = RecolectoresEntity(
            id = idCollector,
            idTemporal = null,
            name = newName,
            state = "active"
        )
        onClickListener(editNameCollector)

        startActivity(
            Intent(
                requireContext(), ActivityRecolectionDetail::class.java
            )
                .putExtra("userId", idCollector).putExtra("userName", newName)
        )
    }

    private fun contextTheme() {
        var color: Int? = null
        controllerTheme.main(
            requireContext(),
            day = {
                color = ContextCompat.getColor(requireContext(), R.color.Orange)
            },
            night = {
                color = ContextCompat.getColor(requireContext(), R.color.OrangeDark)
            })

        if (styleApp == true) {
            binding.btReady.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.tvDescription.backgroundTintList = ColorStateList.valueOf(color!!)

            binding.fbClose.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.fbClose.invalidate()

            binding.tilNameCollector.boxStrokeColor = color!!
            binding.tilNameCollector.hintTextColor = ColorStateList.valueOf(color!!)
        } else {
            controllerTheme.main(
                requireContext(),
                day = {
                    color = ContextCompat.getColor(requireContext(), R.color.Thunderbird)
                },
                night = {
                    color = ContextCompat.getColor(requireContext(), R.color.Dark_Tan)
                }
            )
            binding.fbClose.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.fbClose.invalidate()
        }
    }
}