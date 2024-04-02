package com.rojasdev.apprecconprojectPro.viewHolders

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.controller.price
import com.rojasdev.apprecconprojectPro.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconprojectPro.databinding.ItemRvAllRecolectionDateBinding
import java.text.SimpleDateFormat
import java.util.Locale

class viewHolderItemDate(var view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemRvAllRecolectionDateBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render ( itemDetail: allCollecionAndCollector ) {

        binding.cvCollectionDetail.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)

        val getDate = itemDetail.Fecha
        val formatDateOriginal = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("es", "CO"))
        val format = SimpleDateFormat("'Hora: ' HH:mm", Locale("es", "CO"))
        val date = formatDateOriginal.parse(getDate.toString()) // Fecha
        val timeFormat = format.format(date!!) // Hora

            binding.tvDate.text = timeFormat
            binding.tvNameCollector.text = itemDetail.name_recolector
            binding.tvKgDetail.text = "${itemDetail.Cantidad} Kg"

            price.priceSplit(itemDetail.Precio.toInt()){
                binding.tvPrice.text = "Precio: $it"
            }

            if (itemDetail.Alimentacion == "yes") {
                binding.tvFeending.text = "Alimentacion: Si"
            } else {
                binding.tvFeending.text = "Alimentacion: No"
            }

            if (itemDetail.Estado == "active"){
                price.priceSplit(itemDetail.result.toInt()){
                    binding.tvPaid.text = "Total a Pagar: $it"
                }
            } else {
                price.priceSplit(itemDetail.result.toInt()){
                    binding.tvPaid.text = "Total Pagado: $it"
                }
            }


    }

}