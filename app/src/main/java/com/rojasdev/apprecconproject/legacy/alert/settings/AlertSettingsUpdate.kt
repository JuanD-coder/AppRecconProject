package com.rojasdev.apprecconproject.legacy.alert.settings

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.AlertUpdateSettingBinding

class alertSettingsUpdate(
    private var description: String,
    private var fending: String,
    private var idSetting: Int,
    var price: Int,
    private var style: Boolean?,
    var onClickListener: (com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity) -> Unit ): androidx.fragment.app.DialogFragment() {

    private lateinit var binding: AlertUpdateSettingBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertUpdateSettingBinding.inflate(LayoutInflater.from(context))
        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvSettings)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        contextTheme()

        val myListInput = listOf(
            binding.etNameCollector,
        )

        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        binding.btReady.setOnClickListener {
            val require = _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.requireInput.validate(myListInput,requireContext())
            if (require){
                dates()
                dismiss()
            }
        }

        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.keyLIstener.start(binding.etNameCollector){
            val require = _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.requireInput.validate(myListInput,requireContext())
            if (require){
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
        binding.etNameCollector.inputType = InputType.TYPE_CLASS_NUMBER
        binding.etNameCollector.setText(price.toString())
        binding.tvDescription.text = description

        if (fending == "yes"){
            binding.etNameCollector.setHint(R.string.yesFeeding)
            binding.tilNameCollector.setStartIconDrawable(R.drawable.ic_alimentacion)
        }else{
            binding.tilNameCollector.setStartIconDrawable(R.drawable.ic_no_alimentacion)
            binding.etNameCollector.setHint(R.string.notFeeding)
        }
    }

    private fun dates() {
        val yesAliment = binding.etNameCollector.text.toString()

        val configAlimentYes =
            _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity(
                idSetting,
                fending,
                yesAliment.toInt(),
                "active",
                _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.dateFormat.main()
            )

        onClickListener(configAlimentYes)
    }

    private fun contextTheme() {
        var color: Int? = null
        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.controllerTheme.main(requireContext(),
            day = {
                color = ContextCompat.getColor(requireContext(), R.color.Orange)
            },
            night = {
                color = ContextCompat.getColor(requireContext(), R.color.OrangeDark)
            })

        if (style == true){
            binding.btReady.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.tvDescription.backgroundTintList = ColorStateList.valueOf(color!!)

            binding.fbClose.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.fbClose.invalidate()

            binding.tilNameCollector.boxStrokeColor = color!!
            binding.tilNameCollector.hintTextColor = ColorStateList.valueOf(color!!)
        }else{
            binding.fbClose.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.Dark_Tan))
            binding.fbClose.invalidate()
        }
    }
}