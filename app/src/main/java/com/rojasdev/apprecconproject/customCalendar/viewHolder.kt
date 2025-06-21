package com.rojasdev.apprecconproject.customCalendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.customCalendar.days.adapterDays
import com.rojasdev.apprecconproject.databinding.ItemDateBinding

class viewHolder( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemDateBinding.bind(view)

    @SuppressLint("ResourceAsColor")
    fun render(
        hoy : String,
        item: List<dataModelDay>,
        collection: List<String>,
        work: List<String>,
        onClickListenerNext: (Triple<String,String,Boolean>)-> Unit
    ){
        dates(hoy,item,collection,work){
            onClickListenerNext(it)
        }
    }

    private fun dates (
        hoy : String,
        week: List<dataModelDay>,
        list: List<String>,
        work: List<String>,
        onClickListener: (Triple<String,String,Boolean>) -> Unit) {
        binding.rvDays.isNestedScrollingEnabled = false
        binding.rvDays.apply {

            if (week.size < 7) {
                if(week[0].dayMonth == "1"){
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = adapterDays(hoy,addListWeek(week,week.size), list, work) {
                        onClickListener(it)
                    }
                }else{
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = adapterDays(hoy,week, list, work) {
                        onClickListener(it)
                    }
                }
            } else {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = adapterDays(hoy,week, list, work) {
                    onClickListener(it)
                }
            }
        }
    }

    private fun addListWeek(list: List<dataModelDay>, size: Int): List<dataModelDay> {
        val miList = list.toMutableList()
        val daysFalse = 6 - size
        val nullDay = dataModelDay(
            "null",
            "null",
            "null",
        )

        val newItemLis : MutableList<dataModelDay> = mutableListOf(nullDay)

        for (it in 1 .. daysFalse){
            val nullDay = dataModelDay(
                "null",
                "null",
                "null",
            )
            newItemLis.add(nullDay)
        }



        miList.addAll(0, newItemLis.toList())

        return miList.toList()
    }
}