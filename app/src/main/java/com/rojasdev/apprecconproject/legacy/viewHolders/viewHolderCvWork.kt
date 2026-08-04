package com.rojasdev.apprecconproject.legacy.viewHolders

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.ItemRvRecolectionBinding

class viewHolderCvWork(var view: View ): RecyclerView.ViewHolder(view) {

    private val binding = ItemRvRecolectionBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render(
        itemDetail: com.rojasdev.apprecconproject.legacy.data.dataModel.workSettings,
        onClickListenerUpdate: (com.rojasdev.apprecconproject.legacy.data.dataModel.workSettings) -> Unit
    ) {
        binding.cvCollectionDetail.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)

        binding.image.setImageResource(R.drawable.construction)

        com.rojasdev.apprecconproject.legacy.controller.controllerTheme.main(
            view.context,
            day = {
                binding.layout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.Orange))
                binding.btnUpdate.setTextColor(ContextCompat.getColor(view.context, R.color.Orange))
            },
            night = {
                binding.layout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.OrangeDark))
                binding.btnUpdate.setTextColor(ContextCompat.getColor(view.context, R.color.OrangeDark))
            }
        )

        binding.tvKgTxt.text = view.context.getString(R.string.workDescription)

        val date = com.rojasdev.apprecconproject.legacy.controller.dateFormat.format(itemDetail.Fecha)

        binding.tvDate.text = date.first
        binding.tvTime.text = date.second

        val params = binding.tvKgDetail.layoutParams
        params.height = 240
        binding.tvKgDetail.layoutParams = params

        binding.tvKgDetail.text = itemDetail.actividad

        binding.btnUpdate.setOnClickListener {
            onClickListenerUpdate(itemDetail)
        }

        com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(itemDetail.Precio){
            binding.tvPrice.text = "Precio: $it"
        }

        binding.tvFeending.text = "Jornales: ${itemDetail.cantidad}"
    }
}
