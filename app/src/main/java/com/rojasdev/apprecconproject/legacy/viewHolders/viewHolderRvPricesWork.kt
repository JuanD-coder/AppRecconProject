package com.rojasdev.apprecconproject.legacy.viewHolders

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.ItemPriceWorkBinding


class viewHolderRvPricesWork( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemPriceWorkBinding.bind(view)

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun render(
        item: com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity,
        onClickListener: (com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity) -> Unit,
    ){
        binding.lyItem.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)

        binding.tvTitle.text = "${view.context.getString(R.string.priceWork)} por ${item.feeding}"

        com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(item.cost){
            binding.tvPrice.text = it
        }

        binding.btReady.setOnClickListener {
            onClickListener(item)
        }
    }

}