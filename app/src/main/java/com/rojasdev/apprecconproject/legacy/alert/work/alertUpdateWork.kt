package com.rojasdev.apprecconproject.legacy.alert.work

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import com.rojasdev.apprecconproject.databinding.AlertAddWorkBinding

class alertUpdateWork(
    private var collector: String,
    private var work: com.rojasdev.apprecconproject.legacy.data.entities.WorkEntity,
    var prices: List<com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity>,
    var onClickListener: (com.rojasdev.apprecconproject.legacy.data.entities.WorkEntity) -> Unit
): androidx.fragment.app.DialogFragment()  {

    private lateinit var adapterSpiner : com.rojasdev.apprecconproject.legacy.adapters.adapterSwiper
    private lateinit var binding: AlertAddWorkBinding
    private var cantidad = work.amount

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertAddWorkBinding.inflate(LayoutInflater.from(context))

        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        binding.tvDescription.text = collector

        initViewUpdate()

        binding.etWorkDay.setText(cantidad.toString())

        buttons()

        binding.btPlus.setOnClickListener { plus() }
        binding.btMinius.setOnClickListener { minius() }

        adapterSpiner =
            _root_ide_package_.com.rojasdev.apprecconproject.legacy.adapters.adapterSwiper(
                requireContext(),
                prices
            )

        new()

        binding.sPrice.adapter = adapterSpiner

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun buttons() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        val myListInput = listOf(
            binding.etWork,
            binding.etWorkDay
        )

        binding.btReady.setOnClickListener {
            val require = _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.requireInput.validate(myListInput,requireContext())
            if (require){
                dates()
                dismiss()
            }
        }
    }

    private fun dates() {
        val work = binding.etWork.text.toString()
        val workDay = binding.etWorkDay.text.toString().toInt()
        val price = binding.sPrice.selectedItemId

        val workRegister =
            _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.entities.WorkEntity(
                null,
                workDay,
                work,
                _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.dateFormat.main(),
                "active",
                this.work.collector,
                price.toInt()
            )

        onClickListener(workRegister)
    }

    private fun plus(){
        if (cantidad > 0 ){
            cantidad += 1
            binding.etWorkDay.setText(cantidad.toString())
        }
    }

    private fun minius(){
        if (cantidad > 1 ){
            cantidad -= 1
            binding.etWorkDay.setText(cantidad.toString())
        }
    }

    private fun initViewUpdate(){
        binding.etWork.setText(work.total)
}

    private fun new(){

        val indiceSeleccionado = (0 until adapterSpiner.count).indexOfFirst {
            val item = adapterSpiner.getItemId(it)
            item == work.setting.toLong()
        }

        if (indiceSeleccionado != -1) {
            adapterSpiner.setSelectedItemId(indiceSeleccionado)
        }
    }

}