package com.rojasdev.apprecconproject.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class adapterRvCollectionTotal(
    private var items:List<com.rojasdev.apprecconproject.legacy.data.dataModel.collecionTotalCollector>,
    private var color: Int?,
    private val onClickListener: (com.rojasdev.apprecconproject.legacy.data.dataModel.collecionTotalCollector) -> Unit
) : RecyclerView.Adapter<com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvCollectionTotal>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvCollectionTotal {
        return com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvCollectionTotal(
            LayoutInflater.from(parent.context).inflate(R.layout.item_collecion, parent, false)
        )
    }

    override fun onBindViewHolder(holder: com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvCollectionTotal, position: Int) {
        val item = items[position]
        holder.render(item,color,onClickListener)

    }

    override fun getItemCount(): Int = items.size
}