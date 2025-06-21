package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.data.dataModel.allWorkAndCollector
import com.rojasdev.apprecconproject.databinding.ItemDatesBinding
import java.text.SimpleDateFormat
import java.util.Locale

class viewHolderItemDatesWork(var view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemDatesBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render ( itemDetail: allWorkAndCollector) {

        //binding.cvCollectionDetail.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)

        val formatDateOriginal = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("es", "CO"))
        val format = SimpleDateFormat("HH:mm", Locale("es", "CO"))
        val date = formatDateOriginal.parse(itemDetail.Fecha.toString()) // Fecha
        val timeFormat = format.format(date!!) // Hora

        binding.tvWorkDes.text = "Jornales"

        binding.tvHora.text = timeFormat

        binding.tvNameCollector.text = itemDetail.name_recolector

        binding.tvKilos.text = "${itemDetail.cantidad.toInt()}"

        binding.tvActividad.visibility = View.VISIBLE
        binding.tvActividad.text = itemDetail.actividad

        binding.image.setImageResource(R.drawable.construction)

        price.priceSplit(itemDetail.Precio.toInt()){
            binding.tvPrice.text = it
        }

        price.priceSplit(itemDetail.result.toInt()){
            binding.tvTotal.text = it
        }
    }

}