package com.rojasdev.apprecconproject.legacy.viewHolders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.databinding.ItemCollectionCancelBinding

class viewHolderCancelCollection( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemCollectionCancelBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render(
        item: com.rojasdev.apprecconproject.legacy.data.dataModel.collectorCollection
    ){
        if (item.Alimentacion == "yes"){
            binding.tvAliment.text = "si ${item.Precio.toInt()}"
        }else{
            binding.tvAliment.text = "no ${item.Precio.toInt()}"
        }

        binding.tvKg.text = "${item.Cantidad}kg"
        com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(item.result.toInt()){
            binding.tvPrice.text = it
        }
    }

}