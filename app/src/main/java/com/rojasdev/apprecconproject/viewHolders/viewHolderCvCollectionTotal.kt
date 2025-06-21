package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.databinding.ItemCollecionBinding

class viewHolderCvCollectionTotal( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemCollecionBinding.bind(view)

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun render(
        item: collecionTotalCollector,
        color: Int?,
        onClickListener: (collecionTotalCollector) -> Unit
    ){
        binding.cv.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)
        binding.tvNameCollector.text = item.name_recolector
        binding.tvKg.text = "${item.kg_collection} kg"

        if(color != null){
            binding.btReady.backgroundTintList = ColorStateList.valueOf(color)
            binding.tvNameCollector.backgroundTintList = ColorStateList.valueOf(color)
        }

        price.priceSplit(item.price_total.toInt()){
            binding.tvTotalPrice.text = it
        }

        binding.btReady.setOnClickListener {
            onClickListener(item)
        }
    }

}