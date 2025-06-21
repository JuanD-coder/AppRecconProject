package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataModel.workMen
import com.rojasdev.apprecconproject.databinding.ItemCollectionCancelBinding

class viewHolderCancelWork( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemCollectionCancelBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render(
        item: workMen
    ){


        price.priceSplit(item.Precio.toInt()){
            binding.tvAliment.text = it
        }

        binding.tvKg.text = item.cantidad.toString()

        price.priceSplit(item.result.toInt()){
            binding.tvPrice.text = it
        }
    }

}