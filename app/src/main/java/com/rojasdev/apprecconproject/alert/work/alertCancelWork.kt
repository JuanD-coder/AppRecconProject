package com.rojasdev.apprecconproject.alert.work

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.adapterRvCancelWork
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.controllerTheme
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataModel.workMen
import com.rojasdev.apprecconproject.data.dataModel.workTotalCollector
import com.rojasdev.apprecconproject.databinding.AlertCancelCollectionBinding

class alertCancelWork(
    private var collectionTotal: List<workTotalCollector>,
    val collection: List<workMen>,
    var onClickListener: (Int) -> Unit
): DialogFragment() {
    private lateinit var adapter: adapterRvCancelWork
    private lateinit var binding: AlertCancelCollectionBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCancelCollectionBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        buttons()

        binding.tvNameCollector.text = collectionTotal[0].name_recolector


        binding.tvKg.text = collectionTotal[0].days_work.toString()
        price.priceSplit(collectionTotal[0].total.toInt()){
            binding.tvTotalPrice.text = it
        }

        initViewWork()

        dates()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
    private fun dates() {
        adapter = adapterRvCancelWork(collection)
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
        controllerTheme.main(
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