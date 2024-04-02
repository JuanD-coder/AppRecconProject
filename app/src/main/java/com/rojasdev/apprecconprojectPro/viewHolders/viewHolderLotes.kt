package com.rojasdev.apprecconprojectPro.viewHolders

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.databinding.ItemLotesBinding

class viewHolderLotes(
    var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemLotesBinding.bind(view)

    @SuppressLint("ResourceAsColor")
    fun render(
        item: LoteEntity,
        onClickListenerUpdate: (LoteEntity) -> Unit,
        onClickListenerDelete: (LoteEntity) -> Unit
    ){
        binding.cvCollector.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)
        binding.tvName.text = item.name
        binding.tvType.text = item.type

        binding.fbUpdate.setOnClickListener {
            onClickListenerUpdate(item)
        }

        binding.fbDelete.setOnClickListener {
            onClickListenerDelete(item)
        }

    }

}