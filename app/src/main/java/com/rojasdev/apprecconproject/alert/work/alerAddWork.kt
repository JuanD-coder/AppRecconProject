package com.rojasdev.apprecconproject.alert.work

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.adapters.adapterSwiper
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.dateFormat
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.data.entities.WorkEntity
import com.rojasdev.apprecconproject.databinding.AlertAddWorkBinding

class alerAddWork (
    private var collector: RecolectoresEntity,
    var prices: List<SettingEntity>,
    var onClickListener: (WorkEntity) -> Unit
): DialogFragment()  {

    private lateinit var adapterSpiner : adapterSwiper
    private lateinit var binding: AlertAddWorkBinding
    private var cantidad = 1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertAddWorkBinding.inflate(LayoutInflater.from(context))

        animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        binding.tvDescription.text = collector.name


        binding.etWorkDay.setText(cantidad.toString())

        buttons()

        binding.btPlus.setOnClickListener { plus() }
        binding.btMinius.setOnClickListener { minius() }

        adapterSpiner = adapterSwiper(
            requireContext(),
            prices
        )

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
            val require = requireInput.validate(myListInput,requireContext())
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

        val workRegister = WorkEntity(
            null,
            workDay,
            work,
            dateFormat.main(),
            "active",
            collector.id!!,
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
}