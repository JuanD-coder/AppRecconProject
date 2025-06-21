package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.databinding.ItemDatesBinding
import com.rojasdev.apprecconproject.databinding.ItemRvAllRecolectionDateBinding
import java.text.SimpleDateFormat
import java.util.Locale

class viewHolderItemDate(var view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemDatesBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render ( itemDetail: allCollecionAndCollector ) {

        //binding.cvCollectionDetail.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)

        val formatDateOriginal = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("es", "CO"))
        val format = SimpleDateFormat("HH:mm", Locale("es", "CO"))
        val date = formatDateOriginal.parse(itemDetail.Fecha.toString()) // Fecha
        val timeFormat = format.format(date!!) // Hora

        binding.tvHora.text = timeFormat

        binding.tvNameCollector.text = itemDetail.name_recolector

        binding.tvKilos.text = "${itemDetail.Cantidad} \n Kg"

        price.priceSplit(itemDetail.Precio.toInt()){
            binding.tvPrice.text = it
        }

        price.priceSplit(itemDetail.result.toInt()){
            binding.tvTotal.text = it
        }
    }

}

/*



            binding.tvDate.text = timeFormat
            binding.tvNameCollector.text = itemDetail.name_recolector




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

 */