package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.dataModel.workTotalCollector
import com.rojasdev.apprecconproject.viewHolders.viewHolderCvCollectionTotal
import com.rojasdev.apprecconproject.viewHolders.viewHolderCvWorkTotal

class adapterRvWorkTotal(
    private var items:List<workTotalCollector>,
    private var color: Int?,
    private val onClickListener: (workTotalCollector) -> Unit
) : RecyclerView.Adapter<viewHolderCvWorkTotal>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderCvWorkTotal {
        return viewHolderCvWorkTotal(LayoutInflater.from(parent.context).inflate(R.layout.item_collecion, parent,false))
    }

    override fun onBindViewHolder(holder: viewHolderCvWorkTotal, position: Int) {
        val item = items[position]
        holder.render(item,color,onClickListener)

    }

    override fun getItemCount(): Int = items.size
}