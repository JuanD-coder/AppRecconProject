package com.rojasdev.apprecconproject.legacy.viewHolders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.databinding.ItemCollectionCancelBinding

class viewHolderCancelWork( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemCollectionCancelBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render(
        item: com.rojasdev.apprecconproject.legacy.data.dataModel.workMen
    ){


        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(item.Precio.toInt()){
            binding.tvAliment.text = it
        }

        binding.tvKg.text = item.cantidad.toString()

        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(item.result.toInt()){
            binding.tvPrice.text = it
        }
    }

}