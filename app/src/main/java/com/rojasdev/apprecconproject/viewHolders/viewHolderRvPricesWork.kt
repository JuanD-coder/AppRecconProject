package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.ItemPriceWorkBinding


class viewHolderRvPricesWork( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemPriceWorkBinding.bind(view)

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun render(
        item: SettingEntity,
        onClickListener: (SettingEntity) -> Unit,
    ){
        binding.lyItem.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)

        binding.tvTitle.text = "${view.context.getString(R.string.priceWork)} por ${item.feeding}"

        price.priceSplit(item.cost){
            binding.tvPrice.text = it
        }

        binding.btReady.setOnClickListener {
            onClickListener(item)
        }
    }

}