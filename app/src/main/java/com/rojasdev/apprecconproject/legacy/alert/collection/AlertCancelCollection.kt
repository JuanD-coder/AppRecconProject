package com.rojasdev.apprecconproject.legacy.alert.collection

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

class alertCancelCollection (
    private var collectionTotal: List<com.rojasdev.apprecconproject.legacy.data.dataModel.collecionTotalCollector>,
    val collection: List<com.rojasdev.apprecconproject.legacy.data.dataModel.collectorCollection>,
    var onClickListener: (Int) -> Unit
): androidx.fragment.app.DialogFragment() {
    private lateinit var adapter: com.rojasdev.apprecconproject.legacy.adapters.adapterRvCancelCollection
    private lateinit var binding: AlertCancelCollectionBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCancelCollectionBinding.inflate(LayoutInflater.from(context))
        com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        buttons()

        binding.tvNameCollector.text = collectionTotal[0].name_recolector


        binding.tvKg.text = "${collectionTotal[0].kg_collection} Kg"
        com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(collectionTotal[0].price_total.toInt()){
            binding.tvTotalPrice.text = it
        }

        dates()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
    private fun dates() {
        var color : Int? = null
        com.rojasdev.apprecconproject.legacy.controller.controllerTheme.main(
            requireContext(),
            day = {
                color = ContextCompat.getColor(requireContext(), R.color.Thunderbird)
            },
            night = {
                color = ContextCompat.getColor(requireContext(), R.color.Dark_Tan)
            }
        )

        binding.btnClose.backgroundTintList = ColorStateList.valueOf(color!!)
        binding.btnClose.invalidate()

        adapter =
            com.rojasdev.apprecconproject.legacy.adapters.adapterRvCancelCollection(
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
}