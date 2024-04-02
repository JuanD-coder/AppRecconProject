package com.rojasdev.apprecconprojectPro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import com.rojasdev.apprecconprojectPro.viewHolders.viewHolderCvCollectors
import com.rojasdev.apprecconprojectPro.viewHolders.viewHolderLotes

class adapterRvLotes(
private var items:List<LoteEntity>,
private val onClickListenerUpdate: (LoteEntity) -> Unit,
private val onClickListenerDelete: (LoteEntity) -> Unit) : RecyclerView.Adapter<viewHolderLotes>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderLotes {
        return viewHolderLotes(LayoutInflater.from(parent.context).inflate(R.layout.item_lotes,parent,false,))
    }

    override fun onBindViewHolder(holder: viewHolderLotes, position: Int) {
        val item = items[position]
        holder.render(item,onClickListenerUpdate,onClickListenerDelete)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}