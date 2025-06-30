package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.databinding.ItemCollectionCancelBinding

class viewHolderCancelCollection(var view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemCollectionCancelBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render(
        item: collectorCollection
    ) {

        binding.tvAliment.text = "Precio: ${item.Precio.toInt()}"

        binding.tvKg.text = "${item.Cantidad}kg"
        price.priceSplit(item.result.toInt()) {
            binding.tvPrice.text = it
        }
    }

}