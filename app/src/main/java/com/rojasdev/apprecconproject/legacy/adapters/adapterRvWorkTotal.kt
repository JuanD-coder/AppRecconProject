package com.rojasdev.apprecconproject.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class adapterRvWorkTotal(
    private var items:List<com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector>,
    private var color: Int?,
    private val onClickListener: (com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector) -> Unit
) : RecyclerView.Adapter<com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvWorkTotal>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvWorkTotal {
        return _root_ide_package_.com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvWorkTotal(
            LayoutInflater.from(parent.context).inflate(R.layout.item_collecion, parent, false)
        )
    }

    override fun onBindViewHolder(holder: com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvWorkTotal, position: Int) {
        val item = items[position]
        holder.render(item,color,onClickListener)

    }

    override fun getItemCount(): Int = items.size
}