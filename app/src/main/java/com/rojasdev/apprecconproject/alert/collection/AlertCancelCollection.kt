package com.rojasdev.apprecconproject.alert.collection

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
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.adapterRvCancelCollection
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.controllerTheme
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataModel.collectionTotal
import com.rojasdev.apprecconproject.databinding.AlertCancelCollectionBinding

class alertCancelCollection(
    private var collectionTotal: collectionTotal,
    var onClickListener: () -> Unit
) : DialogFragment() {
    private lateinit var adapter: adapterRvCancelCollection
    private lateinit var binding: AlertCancelCollectionBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCancelCollectionBinding.inflate(LayoutInflater.from(context))

        animatedAlert.animatedInit(binding.cvRecolector)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        buttons()
        dates()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun dates() {
        var color: Int? = null
        controllerTheme.main(
            requireContext(),
            day = {
                color = ContextCompat.getColor(requireContext(), R.color.Thunderbird)
            },
            night = {
                color = ContextCompat.getColor(requireContext(), R.color.Dark_Tan)
            }
        )

        binding.tvPriceKg.text = "Precio por kilogramo: $${collectionTotal.price}"
        binding.tvTotalKg.text = "Total recolectado: ${collectionTotal.total_kg} Kg"

        price.priceSplit(collectionTotal.price_total.toInt()) {
            binding.tvTotalToPay.text = "Total a pagar: ${it}"
        }

        binding.btnClose.backgroundTintList = ColorStateList.valueOf(color!!)
        binding.btnClose.invalidate()
    }

    private fun buttons() {

        binding.btnConfirm.setOnClickListener {
            onClickListener()
            dismiss()
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

    }
}