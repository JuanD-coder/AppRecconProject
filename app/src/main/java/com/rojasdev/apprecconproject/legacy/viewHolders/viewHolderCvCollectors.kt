package com.rojasdev.apprecconproject.legacy.viewHolders

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.ItemCollectorBinding

class viewHolderCvCollectors( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemCollectorBinding.bind(view)

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun render(
        item: com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity,
        list: List<Long>,
        onClickListenerNext: (com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) -> Unit,
        onClickListenerDelete: (com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) -> Unit,
        onClickListenerKg: (com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) -> Unit
    ){
        binding.cvCollector.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)
        binding.tvNameCollector.text = item.name

        if (item.state == "work-active"){
            com.rojasdev.apprecconproject.legacy.controller.controllerTheme.main(
                view.context,
                day = {
                    binding.layout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.Orange))
                    binding.fbAddKg.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.Orange))
                },
                night = {
                    binding.layout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.OrangeDark))
                    binding.fbAddKg.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.OrangeDark))
                }
            )
            binding.fbAddKg.setImageResource(R.drawable.ic_edit)


            binding.tv.text = view.context.getString(R.string.Work)
        }

        val result = item.id!!.toLong() in list

        if (result){
            if (item.state == "work-active"){
                binding.fbDeleteCollector.setImageResource(R.drawable.construction)
            }else{
                binding.fbDeleteCollector.setImageResource(R.drawable.ic_bolsa_de_cafe)
            }
            binding.tvDeleteAndDetail.text = "Detalle"
            binding.fbDeleteCollector.setOnClickListener {
                onClickListenerNext(item)
                animationOnCLick()
            }
            binding.viewHeaderBackground.setOnClickListener {
                onClickListenerNext(item)
                animationOnCLick()
            }
        }else{
            binding.fbDeleteCollector.setImageResource(R.drawable.ic_delete)
            binding.tvDeleteAndDetail.text = "Eliminar"
            binding.fbDeleteCollector.setOnClickListener {
                onClickListenerDelete(item)
                animationOnCLick()
            }
            binding.viewHeaderBackground.setOnClickListener {
                onClickListenerDelete(item)
                animationOnCLick()
            }
        }

        binding.fbAddKg.setOnClickListener {
            onClickListenerKg(item)
            animationOnCLick()
        }

    }

     // Animation
    private fun animationOnCLick() { com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedClick(binding.cvCollector) }

}