package com.rojasdev.apprecconprojectPro.viewHolders

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.controller.price
import com.rojasdev.apprecconprojectPro.data.dataModel.lotesAndCollection
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.data.entities.SettingEntity
import com.rojasdev.apprecconprojectPro.databinding.ItemLotesBinding
import com.rojasdev.apprecconprojectPro.databinding.ItemLotesProductionBinding
import com.rojasdev.apprecconprojectPro.databinding.ItemSettingsBinding

class viewHolderLotesp( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemLotesProductionBinding.bind(view)

    fun render(
        item: lotesAndCollection
    ){
        binding.tvLoteKg.text = item.totalCollection.toString()+"Kg"
        binding.tvNameLote.text = item.name_lote
    }

}