package com.rojasdev.apprecconproject.legacy.alert.work

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.AlertCancelCollectionBinding

class alertCancelWork(
    private var collectionTotal: List<com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector>,
    val collection: List<com.rojasdev.apprecconproject.legacy.data.dataModel.workMen>,
    var onClickListener: (Int) -> Unit
): androidx.fragment.app.DialogFragment() {
    private lateinit var adapter: com.rojasdev.apprecconproject.legacy.adapters.adapterRvCancelWork
    private lateinit var binding: AlertCancelCollectionBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCancelCollectionBinding.inflate(LayoutInflater.from(context))
        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        buttons()

        binding.tvNameCollector.text = collectionTotal[0].name_recolector


        binding.tvKg.text = collectionTotal[0].days_work.toString()
        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(collectionTotal[0].total.toInt()){
            binding.tvTotalPrice.text = it
        }

        initViewWork()

        dates()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
    private fun dates() {
        adapter =
            _root_ide_package_.com.rojasdev.apprecconproject.legacy.adapters.adapterRvCancelWork(
                collection
            )
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun buttons (){

        binding.btnReady.setOnClickListener {
            onClickListener(collectionTotal[0].PK_ID_Recolector)
            dismiss()
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnFinish.setOnClickListener {
            dismiss()
        }

    }

    private fun initViewWork(){
        binding.tvAliment4.text = getString(R.string.daysWork)
        binding.tvAliment2.text = getString(R.string.workPrice)
        binding.btnReady.setText(getString(R.string.workCancel))
        initColor()
    }

    private fun initColor(){
        var color : Int? = null
        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.controllerTheme.main(
            requireContext(),
            day = {
                color = ContextCompat.getColor(requireContext(), R.color.Orange)
            },
            night = {
                color = ContextCompat.getColor(requireContext(), R.color.OrangeDark)
            }
        )

        binding.tvNameCollector.backgroundTintList = ColorStateList.valueOf(color!!)

        binding.btnReady.backgroundTintList = ColorStateList.valueOf(color!!)
        binding.btnFinish.backgroundTintList = ColorStateList.valueOf(color!!)

        binding.btnClose.backgroundTintList = ColorStateList.valueOf(color!!)
        binding.btnClose.invalidate()
    }
}