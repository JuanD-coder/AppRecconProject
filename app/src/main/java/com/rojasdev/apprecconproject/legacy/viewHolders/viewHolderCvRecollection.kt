package com.rojasdev.apprecconproject.legacy.viewHolders

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.ItemRvRecolectionBinding

class viewHolderCvRecollection(var view: View ): RecyclerView.ViewHolder(view) {

    private val binding = ItemRvRecolectionBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render(
        itemDetail: com.rojasdev.apprecconproject.legacy.data.dataModel.collectorCollection,
        onClickListenerUpdate: (com.rojasdev.apprecconproject.legacy.data.dataModel.collectorCollection) -> Unit
    ) {
        binding.cvCollectionDetail.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)

        val date = com.rojasdev.apprecconproject.legacy.controller.dateFormat.format(itemDetail.Fecha)

        binding.tvDate.text = date.first
        binding.tvTime.text = date.second
        binding.tvKgDetail.text = "${itemDetail.Cantidad} Kg"

        binding.btnUpdate.setOnClickListener {
            onClickListenerUpdate(itemDetail)
        }

        com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(itemDetail.Precio.toInt()){
            binding.tvPrice.text = "Precio: $it"
        }

        if (itemDetail.Alimentacion == "yes") {
            binding.tvFeending.text = "Alimentacion: Si"
        } else {
            binding.tvFeending.text = "Alimentacion: No"
        }

    }
}