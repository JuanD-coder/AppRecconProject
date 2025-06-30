package com.rojasdev.apprecconproject.customCalendar.days

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.customCalendar.dataModelDay
import com.rojasdev.apprecconproject.databinding.ItemDayBinding

class viewHolderDays( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemDayBinding.bind(view)

    @SuppressLint("ResourceAsColor")
    fun render(
        hoy : String,
        item: dataModelDay,
        collection: List<String>,
        onClickListenerNext: (Triple<String,String,Boolean>) -> Unit
    ) {
        if (hoy == item.dayMonth){
            marcarDay()
            onClickListenerNext(Triple(item.dateTime,item.dayMonth,false))
        }else{
            anularMarcaDay()
        }

        if (item.dayMonth == "null"){
            binding.lyDay.visibility = View.GONE
        }
        binding.monday.text = item.dayMonth

        activity(item, collection, binding.viewCollection)

        binding.lyDay.setOnClickListener {
            onClickListenerNext(Triple(item.dateTime,item.dayMonth,true))
        }
    }

    private fun marcarDay() {
        binding.monday.setBackgroundResource(R.drawable.sircle)
        binding.monday.setTextColor(ContextCompat.getColor(view.context,R.color.white))
        binding.monday.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context,R.color.Hippie_Green))
    }

    private fun anularMarcaDay() {
        binding.monday.setBackgroundResource(R.drawable.none)
        binding.monday.setTextColor(ContextCompat.getColor(view.context,R.color.black))
    }

    private fun activity(item: dataModelDay, list: List<String>,view: View){
        val result = item.dateTime in list
        if (result){
            view.visibility = View.VISIBLE
        }
    }
}