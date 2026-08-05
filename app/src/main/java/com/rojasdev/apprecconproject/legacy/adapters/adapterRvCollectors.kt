package com.rojasdev.apprecconproject.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class adapterRvCollectors(
    private var items:List<com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity>,
    private var list: List<Long>,
    private val onClickListenerNext: (com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) -> Unit,
    private val onClickListenerDelete: (com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) -> Unit,
    private val onClickListenerKg: (com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) -> Unit) : RecyclerView.Adapter<com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvCollectors>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvCollectors {
        return com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvCollectors(
            LayoutInflater.from(parent.context).inflate(R.layout.item_collector, parent, false)
        )
    }

    override fun onBindViewHolder(holder: com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvCollectors, position: Int) {
        val item = items[position]
        holder.render(item,list,onClickListenerNext,onClickListenerDelete,onClickListenerKg)
    }

    override fun getItemCount(): Int = items.size
}