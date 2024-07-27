package com.rojasdev.apprecconproject.customCalendar.days

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.customCalendar.dataModelDay


class adapterDays (
    private var hoy : String,
    private var items:List<dataModelDay>,
    private var collection:List<String>,
    private var work:List<String>,
    private val onClickListenerNext: (Triple<String,String,Boolean>) -> Unit ) : RecyclerView.Adapter<viewHolderDays>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderDays {
        return viewHolderDays(LayoutInflater.from(parent.context).inflate(R.layout.item_day,parent,false))
    }

    override fun onBindViewHolder(holder: viewHolderDays, position: Int) {
        renderView(holder,position)
    }

    private fun renderView(holder: viewHolderDays,position: Int) {
        val item = items[position]
        holder.render(hoy,item,collection,work,onClickListenerNext)
    }

    override fun getItemCount(): Int = items.size


}