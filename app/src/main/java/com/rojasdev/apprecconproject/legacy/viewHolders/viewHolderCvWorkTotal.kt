package com.rojasdev.apprecconproject.legacy.viewHolders

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.ItemCollecionBinding

class viewHolderCvWorkTotal( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemCollecionBinding.bind(view)

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun render(
        item: com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector,
        color: Int?,
        onClickListener: (com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector) -> Unit
    ){
        binding.cv.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)
        binding.tvNameCollector.text = item.name_recolector
        binding.tvKg.text = item.days_work.toString()
        binding.textView6.text = view.context.getString(R.string.daysWork)
        binding.btReady.setText(view.context.getString(R.string.workCancel))

        if(color != null){
            binding.btReady.backgroundTintList = ColorStateList.valueOf(color)
            binding.tvNameCollector.backgroundTintList = ColorStateList.valueOf(color)
        }

        com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(item.total.toInt()){
            binding.tvTotalPrice.text = it
        }

        binding.btReady.setOnClickListener {
            onClickListener(item)
        }
    }

}